package indoortect.com.api;

import com.google.firebase.database.DataSnapshot;

import javax.inject.Inject;

import indoortec.com.apicontract.Action;
import maqplan.com.observer.Observer;

public class Interpretador implements InterpretadorImpl {
    private final ApiImpl api;

    @Inject
    public Interpretador(ApiImpl api) {
        this.api = api;
    }

    @Override
    public void sincronizaPlayList(Observer<DataSnapshot> observable){
        api.referenciaPlaylist().execute(new Action() {
            @Override
            protected void response(DataSnapshot dataSnapshot) {
                observable.observer(dataSnapshot);
            }
        });
    }
}
