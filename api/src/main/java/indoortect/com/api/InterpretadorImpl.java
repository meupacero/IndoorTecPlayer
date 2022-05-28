package indoortect.com.api;

import java.util.List;

import indoortec.com.entity.PlayList;
import indoortec.com.entity.Usuario;
import indoortec.com.observer.Observer;

public interface InterpretadorImpl {
    void sincronizaPlayList(Observer<List<PlayList>> observable);

    void removerMidiasCorrompidas(List<String> playlistCorrompida);

    void logar(Usuario usuario,Observer<Object> observer);
}
