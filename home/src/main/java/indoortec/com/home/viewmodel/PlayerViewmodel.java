package indoortec.com.home.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import indoortec.com.controllercontract.PlaylistController;
import indoortec.com.entity.PlayList;
import indoortec.com.entity.Usuario;
import indoortec.com.home.model.Midia;
import indoortec.com.synccontract.SyncPlaylist;
import indoortec.com.observer.Execute;
import indoortec.com.observer.Observer;

@Singleton
public class PlayerViewmodel extends ViewModel implements Observer<Object>, Runnable {
    private final MutableLiveData<Midia> _midia = new MutableLiveData<>();
    public final LiveData<Midia> midia = _midia;

    private final MutableLiveData<Boolean> _usuario = new MutableLiveData<>();
    public final LiveData<Boolean> usuario = _usuario;

    private final List<PlayList> _playlist = new ArrayList<>();

    private final PlaylistController playlistController;
    private final SyncPlaylist sincronizador;
    private final Handler handler = new Handler();
    private final String deviceId;

    private boolean tocando,segura_display,executando;
    private int position = 0;
    private Execute execute;
    private final String TAG = getClass().getName();

    @SuppressLint("HardwareIds")
    @Inject
    public PlayerViewmodel(Context context,PlaylistController playlistController, SyncPlaylist sincronizador) {
        this.playlistController = playlistController;
        this.sincronizador = sincronizador;
        this.sincronizador.setObserver(this);
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        init();
    }

    private void init() {
        Usuario usuario = sincronizador.usuarioLogado(deviceId);
        _usuario.setValue(usuario != null);

        if (usuario != null){
            _playlist.clear();
            _playlist.addAll(playlistController.fetchAll());
            sincronizador.sincronizar(this::observer);
            play();
        }
    }

    private void play() {
        if (execute != null) {
            Log.d(TAG,"EXISTE TAREFAS A SEREM REALIZADAS");

            if (!executando) {
                executando = true;

                execute.execute((playLists) -> {
                    _playlist.clear();
                    _playlist.addAll(playLists);

                    executando = false;
                    execute = null;

                    Log.d(TAG,"TAREFA CONCLUIDA");

                    play();

                    sincronizador.validarPlayList(this::observer);
                });
            } else throw new RuntimeException("Já existe uma terefa de tratamento de dados em andamento");
            return;
        }

        if (!tocando) {
            tocando = true;

            if (_playlist.size() > 0) {
                int position_playlist = _playlist.size() - 1;

                position = position > position_playlist ? 0 : position;

                PlayList playListItem = _playlist.get(position);
                Midia midia = parseMidia(playListItem);

                Log.d(TAG,"REPRODUZIND :" + midia.file.getPath());

                _midia.setValue(midia);
                position ++;
            } else {
                pararReproducao();
                handler.removeCallbacks(this);
                handler.postDelayed(this,3 * 1000L);
            }
        }
    }

    @NonNull
    private Midia parseMidia(PlayList playListItem) {
        String diretorio = getPath(playListItem.storage);
        File file = new File(diretorio);
        return new Midia(file, playListItem.tipo);
    }

    public void reproducaoConcluida() {
        pararReproducao();
        play();
    }

    private String getPath(String storage) {
        File root = new File(Environment.getExternalStorageDirectory(),"indoortec");
        root = new File(root,"playlist");
        root = new File(root,storage);
        return root.getPath();
    }

    private void pararReproducao() {
        Log.d(TAG,"REPRODUÇÂO PARADA");
        tocando = false;
    }

    public boolean seguraDisplay() {
        return segura_display;
    }

    public void seguraDisplay(boolean segura_display) {
        this.segura_display = segura_display;
    }

    @Override
    public void observer(Object execute) {
        if (execute instanceof Execute){
            this.execute = (Execute) execute;
        }else if (execute instanceof Exception){

        }

    }

    @Override
    public void run() {
        play();
    }
}
