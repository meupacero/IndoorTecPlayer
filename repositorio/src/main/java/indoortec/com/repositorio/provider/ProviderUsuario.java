package indoortec.com.repositorio.provider;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import indoortec.com.entity.Usuario;
import indoortec.com.providercontract.UsuarioProvider;
import indoortec.com.repositorio.dao.RoomUsuarioDao;

@Singleton
public class ProviderUsuario implements UsuarioProvider {
    private final RoomUsuarioDao roomUsuarioDao;

    @Inject
    public ProviderUsuario(RoomUsuarioDao roomUsuarioDao) {
        this.roomUsuarioDao = roomUsuarioDao;
    }

    @Override
    public void gravar(Usuario usuairo) {
        roomUsuarioDao.removeAll();
        roomUsuarioDao.insert(usuairo);
    }

    @Override
    public Usuario usuarioLogado() {
        List<Usuario> usuarios  = roomUsuarioDao.fetch();
        if (usuarios.size() > 0)
            return usuarios.get(0);
        return null;
    }
}
