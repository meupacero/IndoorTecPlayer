package indoortec.com.repositorio.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import indoortec.com.entity.Usuario;

@Database(entities = {Usuario.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase;

    public static AppDatabase get(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,"IndoorTec").allowMainThreadQueries().build();
        }
        return appDatabase;
    }

    public abstract RoomUsuarioDao getUsuarioDao();
}
