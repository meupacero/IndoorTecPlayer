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

    public PlayList() {
    }

    @Ignore
    public PlayList(int id, String storage, String tamanho, String tipo, String data, String nome) {
        this.id = id;
        this.storage = storage;
        this.tamanho = tamanho;
        this.tipo = tipo;
        this.data = data;
        this.nome = nome;
    }

    @Ignore
    public static final String VIDEO = "video";

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static String getVIDEO() {
        return VIDEO;
    }

    public static DateFormat getFormatter() {
        return formatter;
    }

    public static DateFormat getFormatterOld() {
        return formatterOld;
    }
}
