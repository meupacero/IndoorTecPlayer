package indoortect.com.api;

import javax.inject.Inject;

import indoortec.com.apicontract.ApiColetor;

public class ColetorApi implements ApiColetor {

    private final InterpretadorImpl interpretadorImpl;

    @Inject
    public ColetorApi(InterpretadorImpl interpretadorImpl) {
        this.interpretadorImpl = interpretadorImpl;
    }

    @Override
    public void sincroniza() {
        interpretadorImpl.sincroniza();
    }
}
