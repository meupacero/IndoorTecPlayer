package indoortec.com.manager;

import javax.inject.Inject;

import indoortec.com.managercontract.UsuarioManager;
import indoortec.com.repositorio.provider.ProviderUsuario;
import indoortec.com.synccontract.SyncUsuario;

public class ManagerUsuario implements UsuarioManager {
    private final ProviderUsuario providerUsuario;
    private final SyncUsuario syncUsuario;

    @Inject
    public ManagerUsuario(ProviderUsuario providerUsuario, SyncUsuario syncUsuario) {
        this.providerUsuario = providerUsuario;
        this.syncUsuario = syncUsuario;
    }
}
