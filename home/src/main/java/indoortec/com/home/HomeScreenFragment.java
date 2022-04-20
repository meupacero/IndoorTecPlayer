package indoortec.com.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import indoortec.com.di.ViewModelFactory;
import indoortec.com.home.databinding.HomeScreenBinding;
import indoortec.com.home.viewmodel.HomeScreenViewmodel;

public class HomeScreenFragment extends Fragment {

    @Inject
    ViewModelFactory factory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HomeScreenBinding binding = HomeScreenBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DaggerHomeComponent.factory().create(requireContext()).inject(this);
        HomeScreenViewmodel homeScreenViewmodel = new ViewModelProvider(this,factory).get(HomeScreenViewmodel.class);
    }
}
