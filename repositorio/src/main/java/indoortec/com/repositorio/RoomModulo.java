package indoortec.com.repositorio;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import indoortec.com.repositorio.dao.AppDatabase;
import indoortec.com.repositorio.dao.RoomUsuarioDao;

@Module
public class RoomModulo {
    @Singleton
    @Provides
    AppDatabase appDatabase(Context context){
        return AppDatabase.get(context);
    }

    @Singleton
    @Provides
    RoomUsuarioDao roomUsuarioDao(AppDatabase appDatabase){
        return appDatabase.getUsuarioDao();
    }
}
