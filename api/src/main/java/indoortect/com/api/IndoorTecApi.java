package indoortect.com.api;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import indoortec.com.apicontract.ApiIndoorTec;
import indoortec.com.entity.PlayList;
import indoortec.com.entity.Usuario;
import indoortec.com.observer.Observer;

@Singleton
public class IndoorTecApi implements ApiIndoorTec {

    private final InterpretadorImpl interpretadorImpl;

    @Inject
    public IndoorTecApi(InterpretadorImpl interpretadorImpl) {
        this.interpretadorImpl = interpretadorImpl;
    }

    @Override
    public void sincronizaPlaylist(Observer<List<PlayList>> listObserver) {
        interpretadorImpl.sincronizaPlayList(listObserver);
    }

    @Override
    public void removerMidiasCorrompidas(List<String> playlistCorrompida) {
        interpretadorImpl.removerMidiasCorrompidas(playlistCorrompida);
    }

    @Override
    public void logar(Usuario usuario,Observer<Object> observer) {
        interpretadorImpl.logar(usuario,observer);
    }

}
