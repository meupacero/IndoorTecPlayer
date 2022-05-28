package indoortect.com.api;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;
import javax.inject.Singleton;

import indoortect.com.api.firebase.FirebaseRequest;

@Singleton
public class Api implements ApiImpl {
    private DatabaseReference databaseReferencePlaylist;
    private final DatabaseReference databaseReference;
    private final FirebaseAuth auth;
    private String uid_user;
    private String deviceId;
    private String uid_grupo;

    @Inject
    public Api() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public FirebaseRequest referenciaPlaylist() {
        return FirebaseRequest.get(databaseReferencePlaylist);
    }

    @Override
    public FirebaseRequest usuarioAuth() {
        return FirebaseRequest.getAuth(auth);
    }

    @Override
    public void configuraApi(String deviceId, String uid_user, String uid_grupo) {
        this.deviceId = deviceId;
        this.uid_user = uid_user;
        this.uid_grupo = uid_grupo;
        constroiReferencias();
    }

    private void constroiReferencias() {
        databaseReferencePlaylist = constroiReferencia(BuildConfig.playlist);
    }

    @NonNull
    private DatabaseReference constroiReferencia(String reference) {
        reference = validarRota(reference);
        return databaseReference.child(reference);
    }

    @NonNull
    private String validarRota(String rota) {
        rota = rota.replaceAll(BuildConfig.uid_device,deviceId == null ? "" : deviceId);
        rota = rota.replaceAll(BuildConfig.uid_grupo,uid_grupo == null ? "padrao" : uid_grupo);
        rota = rota.replaceAll(BuildConfig.uid_user,uid_user == null ? "" : uid_user);
        return rota;
    }
}
