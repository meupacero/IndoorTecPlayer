package indoortec.com.sessao;

import android.content.Context;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import indoortec.com.appdeps.HasAppDeps;
import indoortec.com.controllercontract.ControllerDeps;

@Singleton
@Component(dependencies = {ControllerDeps.class}, modules = {SessaoModule.class})
public interface SessaoComponent {
    void inject(SessaoFragment playerFragment);

    @Component.Factory
    interface Factory {
        default SessaoComponent create(Context context){
            ControllerDeps controllerDeps = castControllerDeps(context);
            return create(controllerDeps,context);
        }
        SessaoComponent create(ControllerDeps controllerDeps,@BindsInstance Context context);
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
