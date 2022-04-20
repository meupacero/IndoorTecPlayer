package indoortec.com.manager;

import dagger.Binds;
import dagger.Module;
import indoortec.com.managercontract.PlaylistManager;

@Module
public interface ManagerModule {

    @Binds
    PlaylistManager playlistManager(ManagerPlaylist managerPlaylist);
}
