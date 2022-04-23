package indoortec.com.apicontract;

import java.util.List;

import indoortec.com.entity.PlayList;
import maqplan.com.observer.Observer;

public interface ApiIndoorTec {
    void sincronizaPlaylist(Observer<List<PlayList>> listObserver);

    void removerMidiasCorrompidas(List<String> playlistCorrompida);
}
