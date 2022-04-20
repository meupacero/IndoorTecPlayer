package indoortec.com.manager;

import dagger.Binds;
import dagger.Module;
import indoortec.com.managercontract.UsuarioManager;

@Module
public interface ManagerModule {

    @Binds
    UsuarioManager usuarioManager(ManagerUsuario managerUsuario);
}
