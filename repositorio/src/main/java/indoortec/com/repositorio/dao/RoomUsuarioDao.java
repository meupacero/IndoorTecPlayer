package indoortec.com.repositorio.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;

import indoortec.com.entity.Usuario;

@Dao
public abstract class RoomUsuarioDao implements BaseRoomDao<Usuario> {
    @Query("SELECT * FROM usuario")
    public abstract DataSource.Factory<Integer, Usuario> fetchAll();
}
