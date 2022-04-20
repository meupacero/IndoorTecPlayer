package indoortec.com.controller;

import androidx.paging.DataSource;

import java.util.List;

import javax.inject.Inject;

import indoortec.com.controllercontract.PlaylistController;
import indoortec.com.entity.PlayList;
import indoortec.com.providercontract.PlayListProvider;

public class ControllerPlaylist implements PlaylistController {
    private final PlayListProvider playListProvider;

    @Inject
    public ControllerPlaylist(PlayListProvider playListProvider) {
        this.playListProvider = playListProvider;
    }

    @Override
    public List<PlayList> fetchAll() {
        return playListProvider.fetchAll();
    }
}
