package indoortec.com.controller;

import dagger.Binds;
import dagger.Module;
import indoortec.com.controllercontract.UsuarioController;

@Module
public interface ControllerModule {

    @Binds
    UsuarioController usuarioController(ControllerUsuario controllerUsuario);
}
