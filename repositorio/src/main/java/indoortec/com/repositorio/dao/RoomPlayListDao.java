package indoortec.com.repositorio.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import indoortec.com.entity.PlayList;

@Dao
public abstract class RoomPlayListDao implements BaseRoomDao<PlayList> {
    @Query("SELECT * FROM playlist")
    public abstract List<PlayList> fetchAll();

    @Query("delete from playlist")
    public abstract void removeAll();
}
