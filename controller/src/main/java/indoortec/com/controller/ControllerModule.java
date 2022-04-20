package indoortec.com.controller;

import dagger.Binds;
import dagger.Module;
import indoortec.com.controllercontract.PlaylistController;

@Module
public interface ControllerModule {

    @Binds
    PlaylistController playlistController(ControllerPlaylist controllerPlaylist);
}
