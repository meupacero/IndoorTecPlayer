package indoortect.com.api;

import java.util.List;

import indoortec.com.entity.ApiMidia;
import indoortec.com.entity.ApiStorageItem;
import indoortec.com.entity.Usuario;
import indoortec.com.observer.Observer;

public interface InterpretadorImpl {
    void sincronizar(Observer<List<ApiStorageItem>> observable, Observer<Exception> exceptionObserver);

    void removerMidiasCorrompidas(List<String> playlistCorrompida,Observer<Exception> exceptionObserver);

    void logar(Usuario usuario,Observer<Object> observer);

    void configuraApi(String deviceId, String uid_user);

    void pesquisaMidia(String midiaId, Observer<ApiMidia> observer, Observer<Exception> exceptionObserver);

    void download(ApiStorageItem itemDownload,Observer<Boolean> observer,Observer<Exception> exceptionObserver);
}
