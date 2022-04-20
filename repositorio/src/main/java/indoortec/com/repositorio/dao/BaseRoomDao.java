package indoortec.com.repositorio.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import java.util.List;

public interface BaseRoomDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(T object);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<T> object);

    @Delete
    void remove(T object);
}
