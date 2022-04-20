package indoortect.com.api;

import dagger.Binds;
import dagger.Module;
import indoortec.com.apicontract.ApiColetor;

@Module
public interface ApiModule {
    @Binds
    ApiColetor apiColetor(ColetorApi coletorApi);

    @Binds
    InterpretadorImpl interpretadorImpl(Interpretador interpretador);

    @Binds
    ApiImpl apiImpl(Api api);
}
