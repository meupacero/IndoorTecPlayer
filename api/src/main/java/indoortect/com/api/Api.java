package indoortect.com.api;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Inject;
import javax.inject.Singleton;

import indoortect.com.api.firebase.FirebaseRequest;
import indoortect.com.api.requests.RealtimeRequest;
import indoortect.com.api.requests.StorageRequest;

@Singleton
public class Api implements ApiImpl {
    private final FirebaseAuth auth;
    private String uid_device;
    private String uid_user;
    private DatabaseReference playlistIdRef,conexaoRef,removeRef,logRef;
    private final DatabaseReference databaseReference;
    private final StorageReference storageReference;
    private StorageReference midia;

    @Inject
    public Api() {
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public FirebaseRequest usuarioAuth() {
        return FirebaseRequest.getAuth(auth);
    }

    @Override
    public RealtimeRequest idPlaylistDb() {
        return new RealtimeRequest(playlistIdRef);
    }

    @Override
    public RealtimeRequest playlist(String uid_playlist) {
        String playlistRota = BuildConfig.playlist;
        playlistRota = playlistRota.replaceAll(BuildConfig.uid_playlist,uid_playlist == null ? "" : uid_playlist);
        playlistRota = playlistRota.replaceAll(BuildConfig.uid_user,uid_user == null ? "" : uid_user);
        return new RealtimeRequest(databaseReference.child(playlistRota));
    }

    @Override
    public RealtimeRequest midiaRef(String midiaId) {
        String midiaRef = BuildConfig.midia;
        midiaRef = midiaRef.replaceAll(BuildConfig.uid_user,uid_user).replaceAll(BuildConfig.uid_midia, midiaId);
        return new RealtimeRequest(databaseReference.child(midiaRef));
    }

    @Override
    public StorageRequest midiaStorage(String storage) {
        StorageReference reference = storage == null ? midia : midia.child(storage);
        return new StorageRequest(reference);
    }

    @Override
    public RealtimeRequest conexaoRef() {
        return new RealtimeRequest(conexaoRef);
    }

    @Override
    public RealtimeRequest removeRef() {
        return new RealtimeRequest(removeRef);
    }

    @Override
    public RealtimeRequest logRef(String key) {
        DatabaseReference reference = key == null ? logRef : logRef.child(key);
        return new RealtimeRequest(reference);
    }

    @Override
    public RealtimeRequest funcionalidades() {
        return new RealtimeRequest(databaseReference.child("funcionalidades"));
    }

    @Override
    public String getDeviceId() {
        return uid_device;
    }

    @Override
    public void configuraApi(String uid_device, String uid_user) {
        this.uid_device = uid_device;
        this.uid_user = uid_user;
        playlistIdRef = getDatabaseReferebce();
        conexaoRef = databaseReference.child(getConexao());
        removeRef = databaseReference.child(getRemove());
        logRef = databaseReference.child(getLogRef());
        midia = storageReference.child("usuarios").child(uid_user).child("midia");
    }

    private String getRemove() {
        String conexao = BuildConfig.remove;
        conexao = conexao.replaceAll(BuildConfig.uid_user,uid_user == null ? "" : uid_user);
        conexao = conexao.replaceAll(BuildConfig.uid_device,uid_device == null ? "" : uid_device);
        return conexao;
    }

    private String getConexao() {
        String conexao = BuildConfig.conexao;
        conexao = conexao.replaceAll(BuildConfig.uid_user,uid_user == null ? "" : uid_user);
        conexao = conexao.replaceAll(BuildConfig.uid_device,uid_device == null ? "" : uid_device);
        return conexao;
    }

    private String getLogRef() {
        String conexao = BuildConfig.log;
        conexao = conexao.replaceAll(BuildConfig.uid_user,uid_user == null ? "" : uid_user);
        conexao = conexao.replaceAll(BuildConfig.uid_device,uid_device == null ? "" : uid_device);
        return conexao;
    }

    @NonNull
    private DatabaseReference getDatabaseReferebce() {
        return databaseReference.child(getEndPoint());
    }

    @NonNull
    private String getEndPoint() {
        String rota = BuildConfig.playlistId;
        rota = rota.replaceAll(BuildConfig.uid_user,uid_user == null ? "" : uid_user);
        rota = rota.replaceAll(BuildConfig.uid_device,uid_device == null ? "" : uid_device);
        return rota;
    }
}
