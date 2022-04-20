package indoortec.com.providercontract;

import java.util.List;

import indoortec.com.entity.PlayList;
public interface PlayListProvider {
    List<PlayList> fetchAll();
    void removeAll();
    void insert(List<PlayList> playLists);
}
