package indoortec.com.sessao;

import androidx.lifecycle.ViewModel;

import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import indoortec.com.di.ViewModelFactory;
import indoortec.com.di.ViewModelKey;
import indoortec.com.sessao.viewmodel.SessaoViewmodel;

@Module
public class SessaoModule {
    @Singleton
    @Provides
    @IntoMap
    @ViewModelKey(classKey = SessaoViewmodel.class)
    ViewModel bindViewModel(SessaoViewmodel homeViewModel){
        return homeViewModel;
    }

    @Singleton
    @Provides
    ViewModelFactory factory(Map<Class<? extends ViewModel>, Provider<ViewModel>> classToViewModel){
        return new ViewModelFactory(classToViewModel);
    }
}
