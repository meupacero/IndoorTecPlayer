package indoortec.player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import indoortec.com.home.PlayerFragment;
import indoortec.com.sessao.SessaoFragment;

public class MainActivity extends AppCompatActivity implements androidx.lifecycle.Observer<Boolean> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onChanged(false);
    }

    @Override
    public void onChanged(Boolean usuarioLogado) {
        Fragment fragment = usuarioLogado ? new PlayerFragment() : new SessaoFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.root, fragment).commit();
    }
}