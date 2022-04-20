package indoortec.com.home.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import indoortec.com.controllercontract.PlaylistController;
import indoortec.com.entity.PlayList;
import indoortec.com.home.model.Midia;
import indoortec.com.synccontract.SyncPlaylist;
import maqplan.com.observer.Execute;
import maqplan.com.observer.Observer;

public class PlayerViewmodel extends ViewModel implements Observer<Execute> {
    private static final List<PlayList> _playlist = new ArrayList<>();
    private static final MutableLiveData<Midia> _midia = new MutableLiveData<>();
    private static final LiveData<Midia> midia = _midia;

    private final PlaylistController playlistController;
    private final SyncPlaylist sincronizador;

    private boolean tocando,tem_modificacoes, segura_display;

    private int position = 0;
    private Execute execute;

    @Inject
    public PlayerViewmodel(PlaylistController playlistController, SyncPlaylist sincronizador) {
        this.playlistController = playlistController;
        this.sincronizador = sincronizador;
        this.sincronizador.setObserver(this);
        init();
    }

    private void init() {
        _playlist.clear();
        _playlist.addAll(playlistController.fetchAll());
        play();
    }

    private void play() {
        if (!tocando && _playlist.size() > 0) {
            tocando = true;

            int position_playlist = _playlist.size() - 1;

            position ++;
            position = position >= (position_playlist) ? 0 : position;

            PlayList playListItem = _playlist.get(position);

            String path = getPath(playListItem.storage);
            Midia midia = new Midia(path,playListItem.tipo);
            _midia.setValue(midia);
        } else pararReproducao();
    }

    public void reproducaoConcluida() {
        pararReproducao();
        if (execute != null && !execute.isRun())
            execute.execute(() -> {
                play();
                sincronizador.validarPlayList();
            });
        else play();
    }

    private String getPath(String storage) {
        File file = new File("", storage);
        return file.getPath();
    }

    public LiveData<Midia> getData() {
        return midia;
    }

    private void pararReproducao() {
        tocando = false;
    }

    public boolean seguraDisplay() {
        return segura_display;
    }

    public void seguraDisplay(boolean segura_display) {
        this.segura_display = segura_display;
    }

    @Override
    public void observer(Execute execute) {
        this.execute = execute;
    }
}
