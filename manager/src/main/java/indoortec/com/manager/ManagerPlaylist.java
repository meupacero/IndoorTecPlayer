package indoortec.com.manager;

import javax.inject.Inject;

import indoortec.com.managercontract.PlaylistManager;
import indoortec.com.repositorio.provider.ProviderPlayList;
import indoortec.com.synccontract.SyncPlaylist;

public class ManagerPlaylist implements PlaylistManager {
    private final ProviderPlayList providerPlayList;
    private final SyncPlaylist syncPlaylist;

    @Inject
    public ManagerPlaylist(ProviderPlayList providerPlayList, SyncPlaylist syncPlaylist) {
        this.providerPlayList = providerPlayList;
        this.syncPlaylist = syncPlaylist;
    }
}
