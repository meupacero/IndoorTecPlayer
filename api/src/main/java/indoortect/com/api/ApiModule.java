package indoortect.com.api;

import dagger.Binds;
import dagger.Module;
import indoortec.com.apicontract.ApiIndoorTec;

@Module
public interface ApiModule {
    @Binds
    ApiIndoorTec apiIndoorTec(IndoorTecApi indoorTecApi);

    @Binds
    InterpretadorImpl interpretadorImpl(Interpretador interpretador);

    @Binds
    ApiImpl apiImpl(Api api);
}
