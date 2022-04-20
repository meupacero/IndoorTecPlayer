package indoortec.com.synccontract;

import maqplan.com.observer.Execute;
import maqplan.com.observer.Observer;

public interface SyncPlaylist {
    void setObserver(Observer<Execute> executeObserver);
    void validarPlayList();
}
