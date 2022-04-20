package indoortec.com.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Provider;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> classToViewModel;

    @Inject
    public ViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> classToViewModel) {
        this.classToViewModel = classToViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Provider<? extends ViewModel> modelProvider = classToViewModel.get(modelClass);

        if (modelProvider == null)
            throw getNullPointerException(modelClass);

        ViewModel viewModel = modelProvider.get();

        if (viewModel == null)
            throw getNullPointerException(modelClass);

        return Objects.requireNonNull(modelClass.cast(viewModel));
    }

    @NonNull
    private <T extends ViewModel> NullPointerException getNullPointerException(@NonNull Class<T> modelClass) {
        return new NullPointerException("Nenhum mapeamento de ViewModel para classe : {" + modelClass.getName() + "}");
    }
}
