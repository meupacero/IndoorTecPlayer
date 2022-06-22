package indoortect.com.api;

import android.os.Handler;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import indoortec.com.apicontract.Action;
import indoortec.com.entity.ApiMidia;
import indoortec.com.entity.ApiStorageItem;
import indoortec.com.entity.Conexao;
import indoortec.com.entity.PlayList;
import indoortec.com.entity.Usuario;
import indoortec.com.observer.Observer;

@Singleton
public class Interpretador implements InterpretadorImpl {
    private final ApiImpl api;
    private final List<String> strings = new ArrayList<>();

    @Inject
    public Interpretador(ApiImpl api) {
        this.api = api;
    }

    private final Handler handler = new Handler();

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            api.logRef(null).buildHash(new Action() {
                @Override
                protected void response(DataSnapshot dataSnapshot) {
                    api.logRef(dataSnapshot.getKey()).setValue(strings, observable1 -> {}, Throwable::printStackTrace);
                }
            }, Throwable::printStackTrace);
        }
    };

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
    public void deslogar() {
        api.usuarioAuth().deslogar();
    }

    @Override
    public void enviarLog(ArrayList<String> strings) {
        this.strings.clear();
        this.strings.addAll(strings);
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,500L);
    }

    @Override
    public void funcionalidades(Observer<Map<String, Object>> observer) {
        api.funcionalidades().findAllItems(new Action() {
            @Override
            protected void response(DataSnapshot dataSnapshot) {
                Map<String, Object> map = new HashMap<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Object o = dataSnapshot1.getValue();
                    if (o != null) {
                        map.put(dataSnapshot1.getKey(), o);
                    }
                }
                observer.observer(map);
            }
        }, observable -> {
            observer.observer(new HashMap<>());
            observable.printStackTrace();
        });
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

    @Override
    public void enviarDados(Conexao conexao, Observer<Boolean> voidObserver, Observer<Exception> exceptionObserver) {
        api.conexaoRef().setValue(conexao,voidObserver,exceptionObserver);
    }

    @Override
    public void isRemove(Observer<Exception> exceptionObserver, Observer<Boolean> voidObserver) {
        api.removeRef().findAllItems(new Action() {
            @Override
            protected void response(DataSnapshot dataSnapshot) {
                Boolean remove = null;
                if (dataSnapshot.exists()){
                    Object obj = dataSnapshot.getValue();
                    try {
                        remove = Boolean.parseBoolean(String.valueOf(obj));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                voidObserver.observer(remove != null && remove);
            }
        }, exceptionObserver);
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
}
