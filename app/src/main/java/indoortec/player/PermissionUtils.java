package indoortec.player;

import android.app.Activity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import indoortec.player.application.AppIndoorTec;

public class PermissionUtils {
    public static final int REQUEST_PERMISSION = 1010;

    public static ArrayList<String> checkPermissions(String[] strArr) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (strArr == null) {
            return arrayList;
        }
        for (String str : strArr) {
            if (ContextCompat.checkSelfPermission(AppIndoorTec.getContext(), str) != 0) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    public static void requestPermissions(Activity activity, String[] strArr) {
        if (activity != null) {
            ActivityCompat.requestPermissions(activity, strArr, 1010);
        }
    }

    public static boolean isManualSettingPermission(Activity activity, String[] strArr) {
        for (String shouldShowRequestPermissionRationale : strArr) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, shouldShowRequestPermissionRationale)) {
                return false;
            }
        }
        return true;
    }
}
