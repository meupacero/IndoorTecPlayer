package indoortec.com.sincronizador;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import indoortec.com.entity.ApiStorageItem;
import indoortec.com.entity.PlayList;
import indoortec.com.providercontract.PlayListProvider;
import indoortec.com.observer.Execute;
import indoortec.com.observer.Observer;

public class SincronizaDados implements Execute {
    private final List<ApiStorageItem> nuvemPlaylist;
    private final List<PlayList> localPlaylist;
    private final PlayListProvider providerPlaylist;

    public SincronizaDados(List<ApiStorageItem> nuvemPlaylist, List<PlayList> localPlaylist, PlayListProvider providerPlaylist) {
        this.nuvemPlaylist = nuvemPlaylist;
        this.localPlaylist = localPlaylist;
        this.providerPlaylist = providerPlaylist;
    }

    @Override
    public void execute(Observer<List<PlayList>> observer) {
        File root = new File(Environment.getExternalStorageDirectory(),"indoortec");

        if (!root.exists()){
            root.mkdir();
            root.mkdirs();
        }

        File pendencias = new File(root,"pendencias");

        if (!pendencias.exists()){
            pendencias.mkdir();
            pendencias.mkdirs();
        }

        File playlist = new File(root,"playlist");

        if (!playlist.exists()){
            playlist.mkdir();
            playlist.mkdirs();
        }

        for (ApiStorageItem midiaNuvem : nuvemPlaylist) {

            boolean existe = false;

            for (PlayList midiaLocal : localPlaylist) {

                if (midiaLocal.storage.equals(midiaNuvem.getStorage())) {
                    existe = true;
                    break;
                }

            }

            if (!existe) {
                moverParaPlaylist(midiaNuvem,pendencias,playlist);
            }
        }

        for (PlayList midiaLocal : localPlaylist) {

            boolean existe = false;

            for (ApiStorageItem midiaNuvem : nuvemPlaylist) {

                if (midiaLocal.storage.equals(midiaNuvem.getStorage())) {
                    existe = true;
                    break;
                }
            }

            if (!existe)
                removerMidiaLocal(midiaLocal,playlist);
        }

        List<PlayList> playLists = new ArrayList<>();

        int countId = 0;

        for (ApiStorageItem midiaNuvem : nuvemPlaylist) {
            File file = new File(playlist,midiaNuvem.getStorage());

            String fileSize = String.valueOf(file.length());

            if (fileSize.equals(midiaNuvem.getSize())) {
                PlayList playListItem = new PlayList();
                countId++;
                playListItem.id = countId;
                playListItem.storage = midiaNuvem.getStorage();
                playListItem.tipo = midiaNuvem.getTipo();
                playListItem.tamanho = midiaNuvem.getSize();
                playLists.add(playListItem);
            }
        }

        providerPlaylist.removeAll();
        providerPlaylist.insert(playLists);

        observer.observer(playLists);
    }

    private void removerMidiaLocal(PlayList midiaLocal,File playlist) {
        File file = new File(playlist,midiaLocal.storage);
        file.delete();
    }

    private void moverParaPlaylist(ApiStorageItem midiaNuvem,File pendencias,File playlist) {
        File pendente = new File(pendencias, midiaNuvem.getStorage());
        File reproduzir = new File(playlist, midiaNuvem.getStorage());
        pendente.renameTo(reproduzir);
    }
}