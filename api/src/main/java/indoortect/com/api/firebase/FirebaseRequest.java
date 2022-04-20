package indoortect.com.api.firebase;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import indoortec.com.apicontract.Action;

public class FirebaseRequest {
    private static final Handler handler = new Handler();
    private final DatabaseReference reference;

    private FirebaseRequest(DatabaseReference reference) {
        this.reference = reference;
    }

    public static FirebaseRequest get(DatabaseReference reference) {
        return new FirebaseRequest(reference);
    }

    public void execute(Action action) {
        ValueEventListener valueEventListener = getListener(action);
        reference.addListenerForSingleValueEvent(valueEventListener);
        handler.postDelayed(()-> {
            if (!action.sucesso) {
                action.timout = true;
                reference.removeEventListener(valueEventListener);
            }
        },60 * 1000L);
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
