package indoortec.com.sessao;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import indoortec.com.di.ViewModelFactory;
import indoortec.com.sessao.databinding.FragmentSessaoBinding;
import indoortec.com.sessao.viewmodel.SessaoViewmodel;


public class SessaoFragment extends Fragment {

    @Inject
    ViewModelFactory factory;
    private SessaoViewmodel sessaoViewmodel;

    private FragmentSessaoBinding binding;

    public SessaoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DaggerSessaoComponent.factory().create(requireContext()).inject(this);
        sessaoViewmodel = new ViewModelProvider(this,factory).get(SessaoViewmodel.class);
    }

    @NonNull
    private FragmentSessaoBinding getBinding(){
        if (binding == null)
            binding = FragmentSessaoBinding.inflate(getLayoutInflater());
        return binding;
    }
}