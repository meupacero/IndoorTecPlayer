package indoortec.com.home;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import indoortec.com.di.ViewModelKey;
import indoortec.com.home.viewmodel.PlayerViewmodel;

@Module
public interface HomeModule {
    @Binds
    @IntoMap
    @ViewModelKey(classKey = PlayerViewmodel.class)
    ViewModel bindViewModel(PlayerViewmodel homeViewModel);
}
