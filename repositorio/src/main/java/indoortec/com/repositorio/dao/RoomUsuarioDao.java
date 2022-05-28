package indoortec.com.repositorio.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import indoortec.com.entity.Usuario;

@Dao
public abstract class RoomUsuarioDao implements BaseRoomDao<Usuario> {
    @Query("SELECT * FROM usuario limit 1")
    public abstract List<Usuario> fetch();

    @Query("delete from usuario")
    public abstract void removeAll();
}
