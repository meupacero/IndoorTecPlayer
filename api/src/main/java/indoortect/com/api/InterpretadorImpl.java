package indoortect.com.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import indoortec.com.entity.ApiMidia;
import indoortec.com.entity.ApiStorageItem;
import indoortec.com.entity.Conexao;
import indoortec.com.entity.Usuario;
import indoortec.com.observer.Observer;

public interface InterpretadorImpl {
    void sincronizar(Observer<List<ApiStorageItem>> observable, Observer<Exception> exceptionObserver);

    void removerMidiasCorrompidas(List<String> playlistCorrompida,Observer<Exception> exceptionObserver);

    void logar(Usuario usuario,Observer<Object> observer);

    void configuraApi(String deviceId, String uid_user);

    void pesquisaMidia(String midiaId, Observer<ApiMidia> observer, Observer<Exception> exceptionObserver);

    void download(ApiStorageItem itemDownload,Observer<Boolean> observer,Observer<Exception> exceptionObserver);

    void enviarDados(Conexao conexao, Observer<Boolean> voidObserver, Observer<Exception> exceptionObserver);

    void isRemove(Observer<Exception> exceptionObserver, Observer<Boolean> remove);

    void deslogar();

    void enviarLog(ArrayList<String> strings);

    void funcionalidades(Observer<Map<String, Object>> observer);
}
