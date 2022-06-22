package indoortect.com.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import indoortec.com.apicontract.ApiIndoorTec;
import indoortec.com.entity.ApiMidia;
import indoortec.com.entity.ApiStorageItem;
import indoortec.com.entity.Conexao;
import indoortec.com.entity.Usuario;
import indoortec.com.observer.Observer;

@Singleton
public class IndoorTecApi implements ApiIndoorTec {

    private final InterpretadorImpl interpretadorImpl;
    public static final List<String> nao_existe = new ArrayList<>();

    @Inject
    public IndoorTecApi(InterpretadorImpl interpretadorImpl) {
        this.interpretadorImpl = interpretadorImpl;
    }

    @Override
    public void sincronizar(Observer<List<ApiStorageItem>> observable, Observer<Exception> exceptionObserver) {
        interpretadorImpl.sincronizar(observable,exceptionObserver);
    }

    @Override
    public void removerMidiasCorrompidas(Observer<Exception> exceptionObserver) {
        interpretadorImpl.removerMidiasCorrompidas(nao_existe,exceptionObserver);
    }

    @Override
    public void logar(Usuario usuario,Observer<Object> observer) {
        interpretadorImpl.logar(usuario,observer);
    }

    @Override
    public void deslogar() {
        interpretadorImpl.deslogar();
    }

    @Override
    public void configuraApi(String deviceId, String uid_user) {
        interpretadorImpl.configuraApi(deviceId,uid_user);
    }

    @Override
    public void pesquisaMidia(String midiaId, Observer<ApiMidia> observer, Observer<Exception> exceptionObserver) {
        interpretadorImpl.pesquisaMidia(midiaId,observer,exceptionObserver);
    }

    @Override
    public void download(ApiStorageItem itemDownload,Observer<Boolean> observer, Observer<Exception> exceptionObserver) {
        interpretadorImpl.download(itemDownload,observer,exceptionObserver);
    }

    @Override
    public boolean existe(String storage) {
        return !nao_existe.contains(storage);
    }

    @Override
    public void enviarDados(Conexao conexao, Observer<Boolean> voidObserver, Observer<Exception> exceptionObserver) {
        interpretadorImpl.enviarDados(conexao,voidObserver,exceptionObserver);
    }

    @Override
    public void isRemove(Observer<Exception> exceptionObserver,Observer<Boolean> observer) {
        interpretadorImpl.isRemove(exceptionObserver, observer);
    }

    @Override
    public void enviarLog(ArrayList<String> strings) {
        interpretadorImpl.enviarLog(strings);
    }

    @Override
    public void funcionalidades(Observer<Map<String, Object>> observer) {
        interpretadorImpl.funcionalidades(observer);
    }
}
