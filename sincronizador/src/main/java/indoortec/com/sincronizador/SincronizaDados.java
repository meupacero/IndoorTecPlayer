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
    private final List<String> logs;

    public SincronizaDados(List<ApiStorageItem> nuvemPlaylist, List<PlayList> localPlaylist, PlayListProvider providerPlaylist, List<String> logs) {
        this.nuvemPlaylist = nuvemPlaylist;
        this.localPlaylist = localPlaylist;
        this.providerPlaylist = providerPlaylist;
        this.logs = logs;
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

        logs.add("------------------");
        logs.add("Itens no servidor : "+nuvemPlaylist.size());
        logs.add("Itens local : "+localPlaylist.size());

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
}