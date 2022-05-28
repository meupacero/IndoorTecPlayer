package indoortec.com.apicontract;

import java.util.List;

import indoortec.com.entity.PlayList;
import indoortec.com.entity.Usuario;
import indoortec.com.observer.Observer;

public interface ApiIndoorTec {
    void sincronizaPlaylist(Observer<List<PlayList>> listObserver);

    void removerMidiasCorrompidas(List<String> playlistCorrompida);

    void logar(Usuario usuario,Observer<Object> observer);
}
