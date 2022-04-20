package indoortec.com.controller;

import javax.inject.Inject;

import indoortec.com.controllercontract.UsuarioController;
import indoortec.com.providercontract.UsuarioProvider;

public class ControllerUsuario implements UsuarioController {
    private final UsuarioProvider usuarioProvider;

    @Inject
    public ControllerUsuario(UsuarioProvider usuarioProvider) {
        this.usuarioProvider = usuarioProvider;
    }
}
