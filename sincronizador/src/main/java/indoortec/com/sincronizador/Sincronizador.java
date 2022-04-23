package indoortec.com.sincronizador;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import indoortec.com.apicontract.ApiIndoorTec;
import indoortec.com.entity.PlayList;
import indoortec.com.providercontract.PlayListProvider;
import indoortec.com.synccontract.SyncPlaylist;
import indoortec.com.observer.Execute;
import indoortec.com.observer.Observer;

@SuppressLint("StaticFieldLeak")
@Singleton
public class Sincronizador implements SyncPlaylist {
    private final ApiIndoorTec api;
    private boolean sincronizando,revizao;
    private final PlayListProvider playListProvider;
    private final StorageReference storageReference;
    private Observer<Execute> executeObserver;
    private final Handler handler = new Handler();
    private final Runnable runnable = this::sincronizaPlaylist;
    private final String TAG = getClass().getName();
    private final List<String> nao_existe = new ArrayList<>();

    @Inject
    public Sincronizador(ApiIndoorTec api, PlayListProvider playListProvider) {
        this.api = api;
        this.playListProvider = playListProvider;
        storageReference = FirebaseStorage.getInstance().getReference();
        sincronizaPlaylist();
    }

    @Override
    public void setObserver(Observer<Execute> executeObserver) {
        this.executeObserver = executeObserver;
    }

    @Override
    public void validarPlayList() {

        Log.d(TAG,"INICIANDO VALIDAÇÃO DA LISTA DE REPRODUÇÃO");

        api.sincronizaPlaylist(observable -> {
            observable = validarPlayList(observable);

            List<PlayList> playListsLocal = playListProvider.fetchAll();

            Log.d(TAG,"COMPARANDO LISTA DE REPRODUÇÃO COM SERVIDOR");

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
                Log.d(TAG,"POSSIVEIS DADOS NÃO COLETADOS. FAZENDO REVIZÃO");

                revizao = false;
                sincronizaPlaylist();
            } else sincronizado();
        });
    }

    private void sincronizado() {
        Log.d(TAG,"------------VALIDAÇÃO CONCLUIDA--------------");

        api.removerMidiasCorrompidas(nao_existe);

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,60 * 1000L);
    }

    private void sincronizaPlaylist() {
        if (sincronizando) {
            revizao = true;

            Log.d(TAG,"JA EXISTE UMA SINCRONIZAÇÃO EM ANDAMENTE, AO FINAL SERA FEITO UMA REVIZÃO");
            return;
        }

        sincronizando = true;

        api.sincronizaPlaylist(nuvemPlaylist -> {
            nuvemPlaylist = validarPlayList(nuvemPlaylist);

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

            Log.d(TAG,"EXISTEM " + pendentePlaylist.size() + "ITENS A SEREM BAIXADOS");

            baixarMidias(0,nuvemPlaylist,localPlaylist,pendentePlaylist);
        });
    }

    private void baixarMidias(int position,List<PlayList> nuvemPlaylist, List<PlayList> localPlaylist, List<PlayList> pendentePlaylist) {
        if (revizao) {
            revizao = false;
            sincronizando = false;

            Log.d(TAG,"POSSIVEIS DADOS NÃO COLETADOS. FAZENDO REVIZÃO");

            sincronizaPlaylist();
            return;
        }

        if (position < pendentePlaylist.size()) {

            PlayList itemDownload = pendentePlaylist.get(position);

            File file = new File("",itemDownload.storage);

            Log.d(TAG,"INICIANDO DOWNLOAD DA MIDIA : "+itemDownload.storage);

            if (file.exists() && file.length() == Integer.parseInt(itemDownload.tamanho)) {
                Log.d(TAG,"ESTE AQUIVO JA FOI BAIXADO : "+itemDownload.storage + "PULANDO DOWNLOAD");
                baixarMidias(position + 1 ,nuvemPlaylist,localPlaylist,pendentePlaylist);
            } else if (nao_existe.contains(itemDownload.storage)){
                Log.d(TAG,"ESTA MIDIA NÃO EXISTE NO STORAGE");
                baixarMidias(position + 1 ,nuvemPlaylist,localPlaylist,pendentePlaylist);
            }else {
                storageReference.child(itemDownload.storage).getFile(file).addOnSuccessListener(taskSnapshot -> {
                    Log.d(TAG,"DOWNLOAD CONCLUIDO");
                    baixarMidias(position + 1 ,nuvemPlaylist,localPlaylist,pendentePlaylist);
                }).addOnFailureListener(e -> {
                    Log.d(TAG,"ERRO AO BAIXAR MIDIA : "+e.getMessage());
                    e.printStackTrace();

                    if (e.getMessage() != null && e.getMessage().contains("Object does not exist at location")){
                        nao_existe.add(itemDownload.storage);
                        nuvemPlaylist.remove(itemDownload);
                    }

                    baixarMidias(position + 1 ,nuvemPlaylist,localPlaylist,pendentePlaylist);
                });
            }
        } else atualizaPlaylist(nuvemPlaylist,localPlaylist);
    }

    private void atualizaPlaylist(List<PlayList> nuvemPlaylist, List<PlayList> localPlaylist) {
        Log.d(TAG,"CRIANDO PENDENCIA PARA ATUALIZAR APÓS REPRODUÇÃO DE MIDIA");

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

    private List<PlayList> validarPlayList(List<PlayList> playListList) {
        List<PlayList> pendentes = new ArrayList<>();

        for (PlayList playList : playListList){
            if (!nao_existe.contains(playList.storage))
                pendentes.add(playList);
        }
        return pendentes;
    }

}
