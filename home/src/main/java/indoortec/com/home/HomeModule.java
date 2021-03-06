package indoortec.com.home;

import androidx.lifecycle.ViewModel;

import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import indoortec.com.di.ViewModelFactory;
import indoortec.com.di.ViewModelKey;
import indoortec.com.home.viewmodel.PlayerViewmodel;

@Module
public class HomeModule {
    @Singleton
    @Provides
    @IntoMap
    @ViewModelKey(classKey = PlayerViewmodel.class)
    ViewModel bindViewModel(PlayerViewmodel homeViewModel){
        return homeViewModel;
    }

    @Provides
    ViewModelFactory factory(Map<Class<? extends ViewModel>, Provider<ViewModel>> classToViewModel){
        return new ViewModelFactory(classToViewModel);
    }
}
