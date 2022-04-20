package indoortec.com.sincronizador;
import android.annotation.SuppressLint;
import android.os.Handler;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import indoortec.com.apicontract.ApiIndoorTec;
import indoortec.com.entity.PlayList;
import indoortec.com.providercontract.PlayListProvider;
import indoortec.com.synccontract.SyncPlaylist;
import maqplan.com.observer.Execute;
import maqplan.com.observer.Observer;

@SuppressLint("StaticFieldLeak")
public class Sincronizador implements SyncPlaylist {
    private final ApiIndoorTec api;
    private boolean sincronizando,revizao;
    private final PlayListProvider playListProvider;
    private final StorageReference storageReference;
    private Observer<Execute> executeObserver;
    private final Handler handler = new Handler();
    private final Runnable runnable = this::sincronizaPlaylist;

    @Inject
    public Sincronizador(ApiIndoorTec api, PlayListProvider playListProvider) {
        this.api = api;
        this.playListProvider = playListProvider;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void setObserver(Observer<Execute> executeObserver) {
        this.executeObserver = executeObserver;
    }

    @Override
    public void validarPlayList() {
        api.sincronizaPlaylist(observable -> {
            List<PlayList> playListsLocal = playListProvider.fetchAll();

            if (observable.size() != playListsLocal.size()) {
                revizao = true;
            } else if (observable.size() > 0) {
                for (int x = 0; x < observable.size(); x++){
                    PlayList local = playListsLocal.get(x);
                    PlayList nuvem = observable.get(x);
                    if (!local.storage.equals(nuvem.storage)){
                        revizao = true;
                        break;
                    }
                }
            }

            sincronizando = false;

            if (revizao) {
                revizao = false;
                sincronizaPlaylist();
            } else sincronizado();
        });
    }

    private void sincronizado() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,60 * 1000L);
    }

    private void sincronizaPlaylist() {
        if (sincronizando) {
            revizao = true;
            return;
        }

        sincronizando = true;

        api.sincronizaPlaylist(nuvemPlaylist -> {
            if (nuvemPlaylist.size() == 0){
                limpaPlaylist();
                return;
            }

            List<PlayList> localPlaylist = playListProvider.fetchAll();

            List<PlayList> midiasDownload = new ArrayList<>();

            for (int x = 0; x < nuvemPlaylist.size(); x ++){

                PlayList nuvemMidia = nuvemPlaylist.get(x);

                boolean baixar = true;

                for (int y = 0; y < localPlaylist.size(); y ++){

                    PlayList localMidia = localPlaylist.get(y);

                    if (localMidia.storage.equals(nuvemMidia.storage)) {

                        Date dataLocal = localMidia.parseToDate();
                        Date dataNuvem = nuvemMidia.parseToDate();

                        if (dataLocal != null && dataNuvem != null && dataNuvem.getTime() <= dataLocal.getTime()) {
                            baixar = false;
                        }

                        break;
                    }
                }
                if (baixar)
                    midiasDownload.add(nuvemMidia);
            }

            List<PlayList> pendentePlaylist = new ArrayList<>();

            for (PlayList nuvemMidia : midiasDownload) {

                boolean download = true;

                for (PlayList localMidia : localPlaylist){

                    if (nuvemMidia.storage.equals(localMidia.storage)) {

                        download = false;

                        break;
                    }
                }

                if (download)
                    pendentePlaylist.add(nuvemMidia);
            }

            baixarMidias(0,nuvemPlaylist,localPlaylist,pendentePlaylist);
        });
    }

    private void baixarMidias(int position,List<PlayList> nuvemPlaylist, List<PlayList> localPlaylist, List<PlayList> pendentePlaylist) {
        if (revizao) {
            revizao = false;
            sincronizando = false;
            sincronizaPlaylist();
            return;
        }

        if (position < pendentePlaylist.size()) {

            PlayList itemDownload = pendentePlaylist.get(position);

            File file = new File("",itemDownload.storage);

            if (file.exists() && file.length() == Integer.parseInt(itemDownload.tamanho)) {
                baixarMidias(position + 1 ,nuvemPlaylist,localPlaylist,pendentePlaylist);
            } else {
                storageReference.child(itemDownload.storage).getFile(file).addOnSuccessListener(taskSnapshot -> {
                    baixarMidias(position + 1 ,nuvemPlaylist,localPlaylist,pendentePlaylist);
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    baixarMidias(position + 1 ,nuvemPlaylist,localPlaylist,pendentePlaylist);
                });
            }
        } else atualizaPlaylist(nuvemPlaylist,localPlaylist);
    }

    private void atualizaPlaylist(List<PlayList> nuvemPlaylist, List<PlayList> localPlaylist) {
        SincronizaDados sincronizaDados = new SincronizaDados(nuvemPlaylist,localPlaylist,playListProvider);
        if (executeObserver != null){
            executeObserver.observer(sincronizaDados);
        }
    }

    private void limpaPlaylist() {
        LimpaMidias limpaMidias = new LimpaMidias();
        if (executeObserver != null){
            executeObserver.observer(limpaMidias);
        }
    }
}
