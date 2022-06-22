package indoortec.com.sincronizador;

public interface DatabaseResult<T,U> {
    void onSucesso(T response);
    void onErro(U exception);
}
