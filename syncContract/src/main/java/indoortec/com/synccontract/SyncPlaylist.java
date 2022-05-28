package indoortec.com.synccontract;

import indoortec.com.entity.Usuario;
import indoortec.com.observer.Observer;

public interface SyncPlaylist {

    void validarPlayList();

    void logar(Usuario usuario,Observer<Object> viewModelObserver);

    void setObserver(Observer<Object> observer);
}
