package indoortect.com.api;

import indoortect.com.api.firebase.FirebaseRequest;

public interface ApiImpl {
    FirebaseRequest referenciaPlaylist();
    FirebaseRequest usuarioAuth();
    void configuraApi(String deviceId, String uid_user, String uid_grupo);
}
