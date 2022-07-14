package indoortect.com.api.requests;

import android.nfc.NfcAdapter;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import indoortec.com.apicontract.Action;
import indoortec.com.observer.Observer;

public class RealtimeRequest {
    private DatabaseReference databaseReference;
    private static final Handler handler = new Handler();

    public RealtimeRequest(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public void findAllItems(Action action, Observer<Exception> exceptionObserver) {
        if (databaseReference != null){
            setValueEventListenner(action,exceptionObserver);
        } else exceptionObserver.observer(new NullPointerException("Referencia da playlist não encontrada"));
    }

    public void buildHash(Action action, Observer<Exception> exceptionObserver) {
        if (databaseReference != null) {
            databaseReference = databaseReference.push();
            findAllItems(action,exceptionObserver);
        }
    }

    public void setValue(Object object, Observer<Boolean> observer, Observer<Exception> exceptionObserver) {
        final boolean[] sucesso = {false};
        final boolean[] timeout = {false};

        databaseReference.setValue(object).addOnSuccessListener(unused -> {
            if (timeout[0])
                return;

            sucesso[0] = true;
            observer.observer(true);
        });

        handler.postDelayed(()-> {
            if (!sucesso[0]) {
                timeout[0] = true;
                exceptionObserver.observer(new Exception("Não foi possível se comunicar com o servidor"));
            }
        },10 * 1000L);
    }

    public void remove(Observer<Boolean> sucesso,Observer<Exception> erro) {
        databaseReference.removeValue().addOnSuccessListener(unused -> sucesso.observer(true)).addOnFailureListener(erro::observer);
    }

    private void setValueEventListenner(Action action, Observer<Exception> exceptionObserver){
        ValueEventListener valueEventListener = getValueEventListennter(action);
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
        handler.postDelayed(()-> {
            if (!action.sucesso) {
                action.timout = true;
                databaseReference.removeEventListener(valueEventListener);
                exceptionObserver.observer(new Exception("Não foi possível se comunicar com o servidor"));
            }
        },10 * 1000L);
    }

    @NonNull
    private ValueEventListener getValueEventListennter(Action action) {
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
