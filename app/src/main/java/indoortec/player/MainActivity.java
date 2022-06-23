package indoortec.player;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import indoortec.com.home.PlayerFragment;
import indoortec.com.sessao.SessaoFragment;
import indoortec.player.application.App;
import indoortec.player.application.AutoStartService;

public class MainActivity extends AppCompatActivity implements androidx.lifecycle.Observer<Boolean> {

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @SuppressLint("NewApi")
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.getInstance().setContext(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        onChanged(false);
    }

    @Override
    public void onChanged(Boolean usuarioLogado) {
        Fragment fragment = usuarioLogado ? new PlayerFragment() : new SessaoFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.root, fragment).commit();
    }


    @Override
    protected void onStart() {
        super.onStart();
        App.getInstance().activityVisivel();
        if (App.getInstance().autoStartNaoIniciado()) {
            AutoStartService.init(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.getInstance().activityPausada();
    }
}