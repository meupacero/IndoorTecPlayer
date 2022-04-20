package indoortec.com.home;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import indoortec.com.di.ViewModelKey;
import indoortec.com.home.viewmodel.HomeScreenViewmodel;

@Module
public interface HomeModule {
    @Binds
    @IntoMap
    @ViewModelKey(classKey = HomeScreenViewmodel.class)
    ViewModel bindViewModel(HomeScreenViewmodel homeViewModel);
}
