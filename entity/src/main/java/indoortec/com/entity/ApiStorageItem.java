package indoortec.com.entity;

import java.util.ArrayList;
import java.util.List;

public class ApiStorageItem {
    private String size;
    private String storage;
    private String tipo;
    public String nomeTemporario;
    private List<String> quebraRegra;

    public ApiStorageItem() {
    }

    public ApiStorageItem(String size, String storage, String tipo,List<String> quebraRegra) {
        this.size = size;
        this.storage = storage;
        this.tipo = tipo;
        this.quebraRegra = quebraRegra;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<String> getQuebraRegra() {
        if (quebraRegra == null)
            quebraRegra = new ArrayList<>();
        return quebraRegra;
    }

    public void setQuebraRegra(List<String> quebraRegra) {
        this.quebraRegra = quebraRegra;
    }
}
