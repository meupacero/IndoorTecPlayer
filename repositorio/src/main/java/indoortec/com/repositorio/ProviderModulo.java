package indoortec.com.repositorio;

import dagger.Binds;
import dagger.Module;
import indoortec.com.providercontract.PlayListProvider;
import indoortec.com.repositorio.provider.ProviderPlayList;

@Module
public interface ProviderModulo {
    @Binds
    PlayListProvider playlistProvider(ProviderPlayList providerPlayList);
}
