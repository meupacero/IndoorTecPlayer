package indoortec.com.synccontract;

import indoortec.com.entity.Conexao;
import indoortec.com.entity.Usuario;
import indoortec.com.observer.Observer;

public interface SyncPlaylist {


    void logar(Usuario usuario,Observer<Object> viewModelObserver);

    void setObserver(Observer<Object> observer);

    void sincronizar();

    Usuario usuarioLogado(String deviceId);

    void enviarDados(Conexao conexao, Observer<Boolean> voidObserver, Observer<Exception> exceptionObserver);

    boolean usuarioLogado();
}
