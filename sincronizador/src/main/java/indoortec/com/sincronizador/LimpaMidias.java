package indoortec.com.sincronizador;

import maqplan.com.observer.Action;
import maqplan.com.observer.Execute;

public class LimpaMidias implements Execute {
    private boolean run = false;

    @Override
    public void execute(Action action) {
        run = true;
    }

    @Override
    public boolean isRun() {
        return run;
    }
}
