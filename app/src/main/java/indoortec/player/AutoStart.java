package indoortec.player;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.widget.Toast;

public class AutoStart extends BroadcastReceiver {

    private static final String packName = "indoortec.player";
    private static final int disabled = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
    private static final int enabled = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    public static final int DELAY_SECONDS = 120;
    private static final Handler handle = new Handler();

    public static void enable(Context context) {
        bloquearApp(enabled,context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Toast.makeText(context, "120 segundos para bloqueio de aplicação", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            handle.postDelayed(()->{
                bloquearApp(disabled,context);
                handle.postDelayed(()->bloquearApp(enabled,context), DELAY_SECONDS * 1000L);
            }, 5 * 1000L);
        }
    }

    private static void bloquearApp(int enabled,Context context) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        ComponentName compName = new ComponentName(packName, packName + ".AliasActivity");
        pm.setComponentEnabledSetting(compName, enabled, PackageManager.DONT_KILL_APP);
    }
}
