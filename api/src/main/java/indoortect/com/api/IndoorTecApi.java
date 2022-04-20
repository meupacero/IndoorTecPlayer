package indoortect.com.api;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import indoortec.com.apicontract.ApiIndoorTec;
import indoortec.com.entity.PlayList;
import maqplan.com.observer.Observer;

public class IndoorTecApi implements ApiIndoorTec {

    private final InterpretadorImpl interpretadorImpl;

    @Inject
    public IndoorTecApi(InterpretadorImpl interpretadorImpl) {
        this.interpretadorImpl = interpretadorImpl;
    }

    @Override
    public void sincronizaPlaylist(Observer<List<PlayList>> listObserver) {
        interpretadorImpl.sincronizaPlayList(observable -> {
            List<PlayList> playLists = parsePlayList(observable);
            listObserver.observer(playLists);
        });
    }

    private List<PlayList> parsePlayList(DataSnapshot dataSnapshot) {
        List<PlayList> playLists = new ArrayList<>();
        if (dataSnapshot.exists()) {
            int count = 0;

            for (DataSnapshot snapshot : dataSnapshot.child("playlist").getChildren()) {
                count++;
                PlayList item = new PlayList();
                item.id = count;
                item.storage = String.valueOf(snapshot.child("nome").getValue());
                item.tamanho = String.valueOf(snapshot.child("tamanho").getValue());
                item.tipo = String.valueOf(snapshot.child("tipo").getValue());

                playLists.add(item);
            }
        }
        return playLists;
    }
}
