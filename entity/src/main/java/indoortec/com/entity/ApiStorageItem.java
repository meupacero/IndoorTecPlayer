package indoortec.com.entity;

public class ApiStorageItem {
    private String size;
    private String storage;
    private String tipo;
    public String nomeTemporario;

    public ApiStorageItem() {
    }

    public ApiStorageItem(String size, String storage, String tipo) {
        this.size = size;
        this.storage = storage;
        this.tipo = tipo;
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
}
