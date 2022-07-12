package indoortect.com.api;

import indoortect.com.api.firebase.FirebaseRequest;
import indoortect.com.api.requests.RealtimeRequest;
import indoortect.com.api.requests.StorageRequest;

public interface ApiImpl {

    FirebaseRequest usuarioAuth();

    RealtimeRequest idPlaylistDb();

    void configuraApi(String uid_device, String uid_user);

    RealtimeRequest playlist(String playlistId);

    RealtimeRequest midiaRef(String midiaId);

    StorageRequest midiaStorage(String storage);

    RealtimeRequest conexaoRef();

    RealtimeRequest removeRef();

    RealtimeRequest logRef(String key);

    RealtimeRequest funcionalidades();

    String getDeviceId();
}
