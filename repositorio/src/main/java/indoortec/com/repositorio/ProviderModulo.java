package indoortec.com.repositorio;

import dagger.Binds;
import dagger.Module;
import indoortec.com.providercontract.PlayListProvider;
import indoortec.com.providercontract.UsuarioProvider;
import indoortec.com.repositorio.provider.ProviderPlayList;
import indoortec.com.repositorio.provider.ProviderUsuario;

@Module
public interface ProviderModulo {
    @Binds
    PlayListProvider playlistProvider(ProviderPlayList providerPlayList);

    @Binds
    UsuarioProvider usuarioProvider(ProviderUsuario providerUsuario);
}
