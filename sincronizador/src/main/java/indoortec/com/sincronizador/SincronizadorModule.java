package indoortec.com.sincronizador;


import dagger.Binds;
import dagger.Module;
import indoortec.com.synccontract.SyncPlaylist;

@Module
public interface SincronizadorModule {
    @Binds
    SyncPlaylist sincroniza(Sincronizador sincronizador);
}
