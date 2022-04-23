package indoortec.com.sincronizador;

import java.util.ArrayList;
import java.util.List;

import indoortec.com.entity.PlayList;
import maqplan.com.observer.Execute;
import maqplan.com.observer.Observer;

public class LimpaMidias implements Execute {

    @Override
    public void execute(Observer<List<PlayList>> action) {
        action.observer(new ArrayList<>());
    }
}
