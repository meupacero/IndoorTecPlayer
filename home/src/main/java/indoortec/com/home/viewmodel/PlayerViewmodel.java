package indoortec.com.home.viewmodel;

import android.os.Handler;
import android.util.Log;

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
import indoortec.com.home.model.Midia;
import indoortec.com.synccontract.SyncPlaylist;
import indoortec.com.observer.Execute;
import indoortec.com.observer.Observer;

@Singleton
public class PlayerViewmodel extends ViewModel implements Observer<Object>, Runnable {
    private static final MutableLiveData<Midia> _midia = new MutableLiveData<>();
    private static final List<PlayList> _playlist = new ArrayList<>();
    private static final LiveData<Midia> midia = _midia;

    private final PlaylistController playlistController;
    private final SyncPlaylist sincronizador;
    private final Handler handler = new Handler();

    private boolean tocando,segura_display,executando;
    private int position = 0;
    private Execute execute;
    private final String TAG = getClass().getName();

    @Inject
    public PlayerViewmodel(PlaylistController playlistController, SyncPlaylist sincronizador) {
        this.playlistController = playlistController;
        this.sincronizador = sincronizador;
        this.sincronizador.setObserver(this);
        init();
        Log.d("xxx","PlayerViewmodel");
    }

    private void init() {
        _playlist.clear();
        _playlist.addAll(playlistController.fetchAll());
        play();
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

                    sincronizador.validarPlayList();
                });
            } else throw new RuntimeException("Já existe uma terefa de tratamento de dados em andamento");
            return;
        }

        if (!tocando) {
            tocando = true;

            if (_playlist.size() > 0) {
                int position_playlist = _playlist.size() - 1;

                position ++;
                position = position >= (position_playlist) ? 0 : position;

                PlayList playListItem = _playlist.get(position);

                String path = getPath(playListItem.storage);
                Midia midia = new Midia(path,playListItem.tipo);

                Log.d(TAG,"REPRODUZIND :" + midia.path);

                _midia.setValue(midia);
            } else {
                pararReproducao();
                handler.removeCallbacks(this);
                handler.postDelayed(this,3 * 1000L);
            }
        }
    }

    public void reproducaoConcluida() {
        pararReproducao();
        play();
    }

    private String getPath(String storage) {
        File file = new File("", storage);
        return file.getPath();
    }

    public LiveData<Midia> getData() {
        return midia;
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
        this.execute = (Execute) execute;
    }

    @Override
    public void run() {
        play();
    }
}
