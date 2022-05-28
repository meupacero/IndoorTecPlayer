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
    private final DatabaseReference databaseReferencePlaylist,databaseReference;
    private final FirebaseAuth auth;

    @Inject
    public Api() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReferencePlaylist = constroiReferencia(BuildConfig.playlist);
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

    @NonNull
    private DatabaseReference constroiReferencia(String reference) {
        reference = validarRota(reference);
        return databaseReference.child(reference);
    }

    @NonNull
    private String validarRota(String rota) {
        rota = rota.replaceAll(BuildConfig.uid_device,"");
        rota = rota.replaceAll(BuildConfig.uid_grupo,"");
        rota = rota.replaceAll(BuildConfig.uid_user,"");
        return rota;
    }
}
