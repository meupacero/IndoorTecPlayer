package indoortec.com.entity;

public class Conexao {
    public String dataUltimaAlteracao;
    public String informacoes;
    public boolean reproduzindo;
    public String storage;

    public Conexao() {
    }

    public Conexao(String dataUltimaAlteracao, String informacoes, boolean reproduzindo, String storage) {
        this.dataUltimaAlteracao = dataUltimaAlteracao;
        this.informacoes = informacoes;
        this.reproduzindo = reproduzindo;
        this.storage = storage;
    }

    public String getDataUltimaAlteracao() {
        return dataUltimaAlteracao;
    }

    public void setDataUltimaAlteracao(String dataUltimaAlteracao) {
        this.dataUltimaAlteracao = dataUltimaAlteracao;
    }

    public String getInformacoes() {
        return informacoes;
    }

    public void setInformacoes(String informacoes) {
        this.informacoes = informacoes;
    }

    public boolean isReproduzindo() {
        return reproduzindo;
    }

    public void setReproduzindo(boolean reproduzindo) {
        this.reproduzindo = reproduzindo;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }
}
