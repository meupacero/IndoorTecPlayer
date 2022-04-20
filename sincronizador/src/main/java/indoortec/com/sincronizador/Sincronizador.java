package indoortec.com.sincronizador;
import android.annotation.SuppressLint;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import indoortec.com.apicontract.ApiColetor;
import indoortec.com.entity.Usuario;
import indoortec.com.synccontract.SyncUsuario;


@SuppressLint("StaticFieldLeak")
public class Sincronizador implements SyncUsuario {
    private final ApiColetor api;

    @Inject
    public Sincronizador(ApiColetor api) {
        this.api = api;
    }

    @Override
    public void sincroniza(Usuario usuario) {
        sincronizaUsuario(Collections.singletonList(usuario));
    }

    @Override
    public void sincroniza(List<Usuario> usuarios) {
        sincronizaUsuario(usuarios);
    }

    private void sincronizaUsuario(List<Usuario> usuarios) {

    }
}
