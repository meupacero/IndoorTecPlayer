package indoortec.com.home.viewstate;


import java.util.List;

import indoortec.com.home.model.HomeScreenItem;


public enum HomeScreenViewState {
    ViewStateError(),
    ViewStateLoading();

    private List<HomeScreenItem> homeScreenItemList;
    private String erro;

    public HomeScreenViewState setValue(List<HomeScreenItem> homeScreenItems) {
        this.homeScreenItemList = homeScreenItems;
        return this;
    }

    public void setValue(String erro) {
        this.erro = erro;
    }

    public List<HomeScreenItem> getHomeScreenItemList() {
        return homeScreenItemList;
    }

    public String getErro() {
        return erro;
    }
}
