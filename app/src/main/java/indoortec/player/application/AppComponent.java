package indoortec.player.application;

import android.content.Context;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import indoortec.com.apicontract.ApiDeps;
import indoortec.com.controller.ControllerModule;
import indoortec.com.controllercontract.ControllerDeps;
import indoortec.com.manager.ManagerModule;
import indoortec.com.managercontract.ManagerDeps;
import indoortec.com.providercontract.ProviderDeps;
import indoortec.com.repositorio.ProviderModulo;
import indoortec.com.repositorio.RoomModulo;
import indoortec.com.sincronizador.SincronizadorModule;
import indoortect.com.api.ApiModule;

@Singleton
@Component(modules = {
        ApiModule.class,
        ProviderModulo.class,
        RoomModulo.class,
        ControllerModule.class,
        ManagerModule.class,
        SincronizadorModule.class
})
public interface AppComponent extends ApiDeps, ProviderDeps, ControllerDeps, ManagerDeps {
    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Context context);
    }
}
