package indoortec.com.home.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class HomeScreenViewmodel extends ViewModel {
    private static final MutableLiveData<String> _data = new MutableLiveData<>();
    private static final LiveData<String> data = _data;

    @Inject
    public HomeScreenViewmodel() {

    }
}
