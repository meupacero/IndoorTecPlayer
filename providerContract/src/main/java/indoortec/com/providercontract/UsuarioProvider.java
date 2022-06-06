package indoortec.com.providercontract;

import indoortec.com.entity.Usuario;

public interface UsuarioProvider {
    void gravar(Usuario usuairo);
    Usuario usuarioLogado();
    void remove();
}
