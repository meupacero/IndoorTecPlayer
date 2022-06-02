package indoortec.com.sincronizador;

import java.util.ArrayList;
import java.util.List;

import indoortec.com.entity.PlayList;
import indoortec.com.observer.Execute;
import indoortec.com.observer.Observer;
import indoortec.com.providercontract.PlayListProvider;

public class LimpaMidias implements Execute {
    private final PlayListProvider providerPlaylist;

    public LimpaMidias(PlayListProvider providerPlaylist) {
        this.providerPlaylist = providerPlaylist;
    }

    @Override
    public void execute(Observer<List<PlayList>> action) {
        providerPlaylist.removeAll();
        action.observer(new ArrayList<>());
    }
}
