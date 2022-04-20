package indoortec.com.home;

import android.content.Context;

import dagger.Component;
import indoortec.com.appdeps.HasAppDeps;
import indoortec.com.controllercontract.ControllerDeps;

@Component(dependencies = {ControllerDeps.class}, modules = {HomeModule.class})
public interface HomeComponent {
    void inject(PlayerFragment playerFragment);

    @Component.Factory
    interface Factory {
        default HomeComponent create(Context context){
            ControllerDeps controllerDeps = castControllerDeps(context);
            return create(controllerDeps);
        }
        HomeComponent create(ControllerDeps controllerDeps);
    }

    static ControllerDeps castControllerDeps(Context context){
        try {
            HasAppDeps hasAppDeps = (HasAppDeps) context.getApplicationContext();
            return hasAppDeps.getControllerDeps();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("A aplicação precisa implementar a interface HasAppDeps");
        }
    }
}
