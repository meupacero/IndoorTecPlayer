package indoortec.com.sincronizador;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import indoortec.com.entity.PlayList;
import indoortec.com.providercontract.PlayListProvider;
import maqplan.com.observer.Execute;
import maqplan.com.observer.Observer;

public class SincronizaDados implements Execute {
    private final List<PlayList> nuvemPlaylist, localPlaylist;
    private final PlayListProvider providerPlaylist;

    public SincronizaDados(List<PlayList> nuvemPlaylist, List<PlayList> localPlaylist, PlayListProvider providerPlaylist) {
        this.nuvemPlaylist = nuvemPlaylist;
        this.localPlaylist = localPlaylist;
        this.providerPlaylist = providerPlaylist;
    }

    @Override
    public void execute(Observer<List<PlayList>> observer) {
        for (PlayList midiaNuvem : nuvemPlaylist) {

            boolean existe = false;

            for (PlayList midiaLocal : localPlaylist) {

                if (midiaLocal.storage.equals(midiaNuvem.storage)) {
                    existe = true;
                    break;
                }

            }

            if (!existe) {
                File file = new File("",midiaNuvem.storage);
                file.renameTo(new File(""));
            }
        }

        for (PlayList midiaLocal : localPlaylist) {

            boolean existe = false;

            for (PlayList midiaNuvem : nuvemPlaylist) {

                if (midiaLocal.storage.equals(midiaNuvem.storage)) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                File file = new File("",midiaLocal.storage);
                file.delete();
            }
        }

        List<PlayList> playLists = new ArrayList<>();

        int countId = 0;

        for (PlayList midiaNuvem : nuvemPlaylist) {
            File file = new File("",midiaNuvem.storage);

            String fileSize = String.valueOf(file.length());

            if (fileSize.equals(midiaNuvem.tamanho)) {
                countId++;
                midiaNuvem.id = countId;
                playLists.add(midiaNuvem);
            }
        }

        providerPlaylist.removeAll();
        providerPlaylist.insert(playLists);

        observer.observer(playLists);
    }
}