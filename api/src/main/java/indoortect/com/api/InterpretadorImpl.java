package indoortect.com.api;

import com.google.firebase.database.DataSnapshot;

import maqplan.com.observer.Observer;

public interface InterpretadorImpl {
    void sincronizaPlayList(Observer<DataSnapshot> observable);
}
