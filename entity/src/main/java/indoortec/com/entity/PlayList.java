package indoortec.com.entity;

import android.annotation.SuppressLint;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "playlist")
public class PlayList {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String storage;
    public String tamanho;
    public String tipo;
    public String data;
    public String nome;
    
    @Ignore
    public static final String VIDEO = "Video";

    @SuppressLint("SimpleDateFormat")
    @Ignore
    private static final DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @SuppressLint("SimpleDateFormat")
    @Ignore
    private static final DateFormat formatterOld = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Ignore
    public Date parseToDate() {
        Date date = null;
        try {
            date = formatter.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                date = formatterOld.parse(data);
            } catch (ParseException er) {
                e.printStackTrace();
            }
        }
        return date;
    }
}
