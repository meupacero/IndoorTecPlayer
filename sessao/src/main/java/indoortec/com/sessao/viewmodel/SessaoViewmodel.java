package indoortec.com.sessao.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessaoViewmodel extends ViewModel  {

    @Inject
    public SessaoViewmodel() {
        Log.d("xxx","SessaoViewmodel");
    }
}
