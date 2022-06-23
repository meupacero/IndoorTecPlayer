package indoortec.player.application;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import indoortec.player.MainActivity;

public class BootReceiver extends BroadcastReceiver {
    @SuppressLint({"UnsafeProtectedBroadcastReceiver", "WrongConstant"})
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("xxx","onReceive");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent launchIntentForPackage = new Intent(context,MainActivity.class);
            launchIntentForPackage.setFlags(268435456);
            context.startActivity(launchIntentForPackage);
        }
    }
}
