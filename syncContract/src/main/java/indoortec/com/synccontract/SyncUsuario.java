package indoortec.com.synccontract;

import java.util.List;

import indoortec.com.entity.Usuario;

public interface SyncUsuario {
    void sincroniza(Usuario usuario);
    void sincroniza(List<Usuario> usuarios);
}
