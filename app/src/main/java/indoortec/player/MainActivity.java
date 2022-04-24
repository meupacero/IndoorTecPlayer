package indoortec.player;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import indoortec.com.sessao.SessaoFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SessaoFragment fragment = new SessaoFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.root, fragment).commit();
    }
}