package indoortec.com.entity;

public class ApiMidia {
    public String data;
    public String midia;
    public String miniatura;
    public String nome;
    public String tamanho;
    public String tipo;

    public ApiMidia() {
    }

    public ApiMidia(String data, String midia, String miniatura, String nome, String tamanho, String tipo) {
        this.data = data;
        this.midia = midia;
        this.miniatura = miniatura;
        this.nome = nome;
        this.tamanho = tamanho;
        this.tipo = tipo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMidia() {
        return midia;
    }

    public void setMidia(String midia) {
        this.midia = midia;
    }

    public String getMiniatura() {
        return miniatura;
    }

    public void setMiniatura(String miniatura) {
        this.miniatura = miniatura;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
}
