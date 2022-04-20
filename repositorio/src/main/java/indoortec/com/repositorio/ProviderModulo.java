package indoortec.com.repositorio;

import dagger.Binds;
import dagger.Module;
import indoortec.com.providercontract.UsuarioProvider;
import indoortec.com.repositorio.provider.ProviderUsuario;

@Module
public interface ProviderModulo {
    @Binds
    UsuarioProvider usuarioProvider(ProviderUsuario providerUsuario);
}
