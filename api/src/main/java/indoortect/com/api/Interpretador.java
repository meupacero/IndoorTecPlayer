package indoortect.com.api;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import indoortec.com.apicontract.Action;
import indoortec.com.entity.PlayList;
import indoortec.com.observer.Observer;

@Singleton
public class Interpretador implements InterpretadorImpl {
    private final ApiImpl api;

    @Inject
    public Interpretador(ApiImpl api) {
        this.api = api;
    }

    @Override
    public void sincronizaPlayList(Observer<List<PlayList>> observable){
        api.referenciaPlaylist().execute(new Action() {
            @Override
            protected void response(DataSnapshot dataSnapshot) {
                observable.observer(parsePlayList(dataSnapshot));
            }
        });
    }

    @Override
    public void removerMidiasCorrompidas(List<String> playlistCorrompida) {
        api.referenciaPlaylist().execute(new Action() {
            @Override
            protected void response(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String storage = String.valueOf(snapshot.child("storage").getValue());
                    for (String string : playlistCorrompida){
                        if (storage.equals(string)) {
                            snapshot.getRef().removeValue();
                        }
                    }
                }
            }
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
                item.data = String.valueOf(snapshot.child("data").getValue());
                item.nome = String.valueOf(snapshot.child("nome").getValue());
                item.storage = String.valueOf(snapshot.child("storage").getValue());
                item.tamanho = String.valueOf(snapshot.child("tamanho").getValue());
                item.tipo = String.valueOf(snapshot.child("tipo").getValue());

                playLists.add(item);
            }
        }
        return playLists;
    }
}
