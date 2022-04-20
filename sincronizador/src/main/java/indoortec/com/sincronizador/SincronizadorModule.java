package indoortec.com.sincronizador;


import dagger.Binds;
import dagger.Module;
import indoortec.com.synccontract.SyncUsuario;

@Module
public interface SincronizadorModule {
    @Binds
    SyncUsuario sincroniza(Sincronizador sincronizador);
}
