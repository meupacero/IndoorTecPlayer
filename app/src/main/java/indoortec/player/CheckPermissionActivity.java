package indoortec.player;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;

import indoortec.player.databinding.ActivityCheckPermissionBinding;

public class CheckPermissionActivity extends Activity {
    private ActivityCheckPermissionBinding binding;

    boolean isFirstCheck = true;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(getBinding().getRoot());
        initView();
    }

    public ActivityCheckPermissionBinding getBinding() {
        if (binding == null)
            binding = ActivityCheckPermissionBinding.inflate(getLayoutInflater());
        return binding;
    }

    private void initView() {
        this.getBinding().rlRoot.setVisibility(View.GONE);
    }

    public void onResume() {
        super.onResume();
        checkPermission();
    }

    private void checkPermission() {
        ArrayList<String> checkPermissions = PermissionUtils.checkPermissions(KEY.PERMISSIONS);
        if (checkPermissions.size() == 0) {
            goToNextPage();
            return;
        }
        Iterator<String> it = checkPermissions.iterator();
        String str = "";
        while (it.hasNext()) {
            str = str.concat("♣ " + it.next().replace("android.permission.", "").replace("_", " ") + "<br/>");
        }
        if (Build.VERSION.SDK_INT >= 24) {
            getBinding().tvMessage.setText(Html.fromHtml(str, 63));
        } else {
            getBinding().tvMessage.setText(Html.fromHtml(str));
        }
        getBinding().btnGainPermission.setOnClickListener(view -> {
            if (PermissionUtils.isManualSettingPermission(CheckPermissionActivity.this, KEY.PERMISSIONS)) {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.parse("package:" + CheckPermissionActivity.this.getPackageName()));
                CheckPermissionActivity.this.startActivityForResult(intent, 9012);
                return;
            }
            PermissionUtils.requestPermissions(CheckPermissionActivity.this, KEY.PERMISSIONS);
        });
        if (this.isFirstCheck) {
            this.isFirstCheck = false;
            PermissionUtils.requestPermissions(this, KEY.PERMISSIONS);
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        checkPermission();
        getBinding().rlRoot.setVisibility(View.VISIBLE);
    }

    private void goToNextPage() {
        finish();
    }
}
