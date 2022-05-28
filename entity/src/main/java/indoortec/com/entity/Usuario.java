package indoortec.com.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String usuario;
    public String senha;
    public String deviceId;
    public String account_uid;
    public boolean logado;
    public String uid_grupo;
}
