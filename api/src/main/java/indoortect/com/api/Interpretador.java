package indoortect.com.api;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import indoortec.com.apicontract.Action;
import indoortec.com.entity.ApiMidia;
import indoortec.com.entity.ApiStorageItem;
import indoortec.com.entity.PlayList;
import indoortec.com.entity.Usuario;
import indoortec.com.observer.Observer;

@Singleton
public class Interpretador implements InterpretadorImpl {
    private final ApiImpl api;

    @Inject
    public Interpretador(ApiImpl api) {
        this.api = api;
    }

    @Override
    public void sincronizar(Observer<List<ApiStorageItem>> observable, Observer<Exception> exceptionObserver){
        api.idPlaylistDb().findAllItems(new Action() {
            @Override
            protected void response(DataSnapshot dataSnapshot) {
                String playlistId = dataSnapshot.getValue(String.class);
                if (playlistId != null) {
                    listarItems(playlistId,observable,exceptionObserver);
                } else observable.observer(new ArrayList<>());
            }
        }, exceptionObserver);
    }

    private void listarItems(String playlistId, Observer<List<ApiStorageItem>> observable, Observer<Exception> exceptionObserver) {
        api.playlist(playlistId).findAllItems(new Action() {
            @Override
            protected void response(DataSnapshot dataSnapshot) {
                observable.observer(parseMidiasIds(dataSnapshot));
            }
        }, exceptionObserver);
    }

    @Override
    public void removerMidiasCorrompidas(List<String> playlistCorrompida,Observer<Exception> exceptionObserver) {
        api.idPlaylistDb().findAllItems(new Action() {
            @Override
            protected void response(DataSnapshot dataSnapshot) {
                String playlistId = dataSnapshot.getValue(String.class);
                if (playlistId != null) {
                    removeItems(playlistId, playlistCorrompida, exceptionObserver);
                }
            }
        }, exceptionObserver);
    }

    private void removeItems(String playlistId, List<String> items, Observer<Exception> exceptionObserver) {
        api.playlist(playlistId).findAllItems(new Action() {
            @Override
            protected void response(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String storage = String.valueOf(snapshot.child("storage").getValue());
                    for (String string : items){
                        if (storage.equals(string)) {
                            snapshot.getRef().removeValue();
                        }
                    }
                }
            }
        }, exceptionObserver);
    }

    @Override
    public void logar(Usuario usuario,Observer<Object> observer) {
        api.usuarioAuth().logar(usuario, observer);
    }

    @Override
    public void configuraApi(String deviceId, String uid_user) {
        api.configuraApi(deviceId,uid_user);
    }

    @Override
    public void pesquisaMidia(String midiaId,Observer<ApiMidia> observer,Observer<Exception> exceptionObserver) {
        api.midiaRef(midiaId).findAllItems(new Action() {
            @Override
            protected void response(DataSnapshot dataSnapshot) {
                ApiMidia apiMidia = dataSnapshot.getValue(ApiMidia.class);
                observer.observer(apiMidia);
            }
        }, exceptionObserver);
    }

    @Override
    public void download(ApiStorageItem itemDownload, Observer<Boolean> sucesso,Observer<Exception> exceptionObserver) {
        api.midiaStorage(itemDownload.getStorage()).download(itemDownload,sucesso,exceptionObserver);
    }

    private List<ApiStorageItem> parseMidiasIds(DataSnapshot dataSnapshot) {
        List<ApiStorageItem> apiStorageItem = new ArrayList<>();
        if (dataSnapshot.exists()) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                ApiStorageItem item = snapshot.getValue(ApiStorageItem.class);
                if (item != null){
                    apiStorageItem.add(item);
                }
            }
        }
        return apiStorageItem;
    }

    private boolean isaNull(String value) {
        return value == null || value.isEmpty() || value.equals("null");
    }

    private List<PlayList> parsePlayList(DataSnapshot dataSnapshot) {
        List<PlayList> playLists = new ArrayList<>();
        if (dataSnapshot.exists()) {
            int count = 0;

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                count++;
                PlayList item = snapshot.getValue(PlayList.class);

                if (item != null){
                    item.id = count;
                    playLists.add(item);
                }
            }
        }
        return playLists;
    }
}
