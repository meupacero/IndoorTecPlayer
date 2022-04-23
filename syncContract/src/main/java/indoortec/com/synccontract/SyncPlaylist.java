package indoortec.com.synccontract;

import indoortec.com.observer.Execute;
import indoortec.com.observer.Observer;

public interface SyncPlaylist {
    void setObserver(Observer<Execute> executeObserver);
    void validarPlayList();
}
