package indoortec.com.appdeps;

import indoortec.com.apicontract.ApiDeps;
import indoortec.com.controllercontract.ControllerDeps;
import indoortec.com.managercontract.ManagerDeps;
import indoortec.com.providercontract.ProviderDeps;

public interface HasAppDeps {
    ApiDeps getApiDeps();
    ProviderDeps getProviderDeps();
    ControllerDeps getControllerDeps();
    ManagerDeps getManagerDeps();
}
