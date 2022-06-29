package indoortec.player;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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
    }

    private void inicializar() {
        if (Build.VERSION.SDK_INT < 23 || PermissionUtils.checkPermissions(KEY.PERMISSIONS).size() == 0) {
            onChanged(false);
        } else {
            startActivity(new Intent(this, CheckPermissionActivity.class));
        }
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

        inicializar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        overlayPermission();
    }

    private void overlayPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permissão de sobreposição")
                    .setCancelable(false)
                    .setMessage("Este aplicativo precisa dessa permissão de sobreposição para funcionar corretamente. Permita esta permissão na configuração do dispositivo")
                    .setPositiveButton("Confirmar", (dialogInterface, i) -> {
                        try {
                            MainActivity.this.startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION",
                                    Uri.parse("package:" + MainActivity.this.getPackageName())), 123);
                        } catch (Exception unused) {
                            Toast.makeText(MainActivity.this.getApplicationContext(), "A permissão não concedida pelo usuário", Toast.LENGTH_LONG).show();
                        }
                    }).setNegativeButton("Cancelar", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }).show();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 123 && Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            overlayPermission();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.getInstance().activityPausada();
    }
}