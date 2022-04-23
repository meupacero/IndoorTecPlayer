package indoortect.com.api;

import java.util.List;

import indoortec.com.entity.PlayList;
import maqplan.com.observer.Observer;

public interface InterpretadorImpl {
    void sincronizaPlayList(Observer<List<PlayList>> observable);

    void removerMidiasCorrompidas(List<String> playlistCorrompida);
}
