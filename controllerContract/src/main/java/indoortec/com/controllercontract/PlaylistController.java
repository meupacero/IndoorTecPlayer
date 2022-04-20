package indoortec.com.controllercontract;

import java.util.List;

import indoortec.com.entity.PlayList;

public interface PlaylistController {
    List<PlayList> fetchAll();
}
