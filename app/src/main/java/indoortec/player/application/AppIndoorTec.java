package indoortec.player.application;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import indoortec.com.apicontract.ApiDeps;
import indoortec.com.appdeps.HasAppDeps;
import indoortec.com.controllercontract.ControllerDeps;
import indoortec.com.managercontract.ManagerDeps;
import indoortec.com.providercontract.ProviderDeps;


public class AppIndoorTec extends Application implements HasAppDeps {
    private static AppComponent appComponent;
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("xxx","onCreate");
        context = getApplicationContext();
    }

    public AppComponent getApplicationComponent() {
        if (appComponent == null)
            appComponent = DaggerAppComponent.factory().create(this);
        return appComponent;
    }

    @Override
    public ApiDeps getApiDeps() {
        return getApplicationComponent();
    }

    @Override
    public ProviderDeps getProviderDeps() {
        return getApplicationComponent();
    }

    @Override
    public ControllerDeps getControllerDeps() {
        return getApplicationComponent();
    }

    @Override
    public ManagerDeps getManagerDeps() {
        return getApplicationComponent();
    }
}
