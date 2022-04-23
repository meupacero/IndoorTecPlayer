package indoortec.com.repositorio.provider;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import indoortec.com.entity.PlayList;
import indoortec.com.providercontract.PlayListProvider;
import indoortec.com.repositorio.dao.RoomPlayListDao;

@Singleton
public class ProviderPlayList implements PlayListProvider {
    private final RoomPlayListDao roomPlayListDao;

    @Inject
    public ProviderPlayList(RoomPlayListDao roomPlayListDao) {
        this.roomPlayListDao = roomPlayListDao;
    }

    @Override
    public List<PlayList> fetchAll() {
        return roomPlayListDao.fetchAll();
    }

    @Override
    public void removeAll() {
        roomPlayListDao.removeAll();
    }

    @Override
    public void insert(List<PlayList> playLists) {
        roomPlayListDao.insert(playLists);
    }
}
