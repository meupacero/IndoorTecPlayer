package indoortec.player;

import java.util.ArrayList;
import java.util.Arrays;

public class KEY {

    public static final String[] PERMISSIONS = new ArrayList<>(Arrays.asList(
            "android.permission.INTERNET",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.CHANGE_WIFI_STATE",
            "android.permission.RECEIVE_BOOT_COMPLETED",
            "android.permission.SYSTEM_ALERT_WINDOW",
            "android.permission.WAKE_LOCK",
            "com.google.android.c2dm.permission.RECEIVE"
    )).toArray(new String[0]);
}
