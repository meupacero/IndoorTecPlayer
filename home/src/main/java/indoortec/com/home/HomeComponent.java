package indoortec.com.home;

import android.content.Context;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {HomeModule.class})
public interface HomeComponent {
    void inject(HomeScreenFragment homeScreenFragment);

    @Component.Factory
    interface Factory {
        HomeComponent create(@BindsInstance Context context);
    }
}
