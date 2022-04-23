package indoortec.com.sessao;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import indoortec.com.sessao.databinding.FragmentSessaoBinding;


public class SessaoFragment extends Fragment {
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

    @NonNull
    private FragmentSessaoBinding getBinding(){
        if (binding == null)
            binding = FragmentSessaoBinding.inflate(getLayoutInflater());
        return binding;
    }
}