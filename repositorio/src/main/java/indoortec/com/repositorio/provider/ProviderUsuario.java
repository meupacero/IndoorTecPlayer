package indoortec.com.repositorio.provider;

import javax.inject.Inject;

import indoortec.com.providercontract.UsuarioProvider;
import indoortec.com.repositorio.dao.RoomUsuarioDao;

public class ProviderUsuario implements UsuarioProvider {
    private final RoomUsuarioDao roomUsuarioDao;

    @Inject
    public ProviderUsuario(RoomUsuarioDao roomUsuarioDao) {
        this.roomUsuarioDao = roomUsuarioDao;
    }
}
