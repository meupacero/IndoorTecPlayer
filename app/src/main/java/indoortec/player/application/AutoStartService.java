package indoortec.player.application;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import indoortec.player.MainActivity;

public class AutoStartService extends Service {
    private static final int FREQUENCIA = 60;
    private ScheduledExecutorService scheduler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void init(Context context) {
        Intent intent1 = new Intent(context,AutoStartService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent1);
        } else context.startService(intent1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (App.getInstance().autoStartNaoIniciado()){
            App.getInstance().iniciarAutoStart();
            scheduler = Executors.newSingleThreadScheduledExecutor();
            Runnable runnable = this::autoStart;
            scheduler.scheduleAtFixedRate(runnable, FREQUENCIA, FREQUENCIA, TimeUnit.SECONDS);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void autoStart() {
        if (!App.getInstance().isVisible()){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        AlarmManager alarmManager = ((AlarmManager) getSystemService(Context.ALARM_SERVICE));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + (FREQUENCIA * 1000L));
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), PendingIntent.getService(getApplicationContext(), 0, new Intent(getApplicationContext(), AutoStartService.class), 0));
        if (scheduler != null) scheduler.shutdown();
        super.onTaskRemoved(rootIntent);
    }
}
