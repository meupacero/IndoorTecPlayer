package indoortec.com.sessao;

import android.content.Context;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {SessaoModule.class})
public interface SessaoComponent {
    void inject(SessaoFragment playerFragment);

    @Component.Factory
    interface Factory {
        SessaoComponent create(@BindsInstance Context context);
    }
}
