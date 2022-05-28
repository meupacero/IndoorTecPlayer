package indoortect.com.api.firebase;

import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import indoortec.com.apicontract.Action;
import indoortec.com.entity.Usuario;
import indoortec.com.observer.Observer;

public class FirebaseRequest {
    private static final Handler handler = new Handler();
    private final DatabaseReference reference;
    private final FirebaseAuth auth;

    private FirebaseRequest(DatabaseReference reference) {
        this.reference = reference;
        auth = null;
    }

    public FirebaseRequest(FirebaseAuth auth) {
        this.auth = auth;
        reference = null;
    }

    public static FirebaseRequest get(DatabaseReference reference) {
        return new FirebaseRequest(reference);
    }

    public static FirebaseRequest getAuth(FirebaseAuth auth) {
        return new FirebaseRequest(auth);
    }

    public void logar(Usuario usuario,Observer<Object> observer) {
        if (auth == null) {
            onErro(observer);
            return;
        }

        auth.signInWithEmailAndPassword(usuario.usuario,usuario.senha).addOnSuccessListener(authResult -> {
            if (authResult == null || authResult.getUser() == null){
                onErro(observer);
                return;
            }

            usuario.account_uid = authResult.getUser().getUid();

            observer.observer(usuario);

        }).addOnFailureListener(observer::observer);
    }

    private void onErro(Observer<Object> observer) {
        observer.observer(new Exception("Ocorreu um erro. Porfavor, tente escanear o código novamente"));
    }

    public void execute(Action action) {
        if (reference != null){
            ValueEventListener valueEventListener = getListener(action);
            reference.addListenerForSingleValueEvent(valueEventListener);
            handler.postDelayed(()-> {
                if (!action.sucesso) {
                    action.timout = true;
                    reference.removeEventListener(valueEventListener);
                }
            },60 * 1000L);
        } else throw new NullPointerException("Referencia da playlist não encontrada");
    }

    @NonNull
    private ValueEventListener getListener(Action action) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!action.timout) {
                    action.sucesso = true;
                    action.dataSnapshot = dataSnapshot;
                    handler.post(action);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String stringBuilder = "Code : " + databaseError.getCode() + "\n" +
                        "Details : " + databaseError.getDetails() + "\n" +
                        "Message : " + databaseError.getMessage() + "\n";
                new Exception(stringBuilder).printStackTrace();
            }
        };
    }
}
