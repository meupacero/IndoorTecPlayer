package indoortec.com.apicontract;

import com.google.firebase.database.DataSnapshot;

public abstract class Action implements Runnable{
    public DataSnapshot dataSnapshot;
    public boolean timout = false;
    public boolean sucesso = false;

    public Action() {
    }

    @Override
    public void run() {
        response(dataSnapshot);
    }

    protected abstract void response(DataSnapshot dataSnapshot);
}
