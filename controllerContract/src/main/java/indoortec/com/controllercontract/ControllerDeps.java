package indoortec.com.controllercontract;

import indoortec.com.synccontract.SyncPlaylist;

public interface ControllerDeps {
    SyncPlaylist syncPlaylist();
    PlaylistController playlistController();
}
