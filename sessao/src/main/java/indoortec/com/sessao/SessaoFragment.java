package indoortec.com.sessao;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import indoortec.com.di.ViewModelFactory;
import indoortec.com.entity.Usuario;
import indoortec.com.sessao.databinding.FragmentSessaoBinding;
import indoortec.com.sessao.viewmodel.SessaoViewmodel;

public class SessaoFragment extends Fragment implements Observer<Object> {

    @Inject
    ViewModelFactory factory;
    private SessaoViewmodel sessaoViewmodel;
    private FragmentSessaoBinding binding;

    public SessaoFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DaggerSessaoComponent.factory().create(requireContext()).inject(this);
        sessaoViewmodel = new ViewModelProvider(this, factory).get(SessaoViewmodel.class);
        sessaoViewmodel.exception.observe(getViewLifecycleOwner(),this);
        sessaoViewmodel.qrCode.observe(getViewLifecycleOwner(),this);
        sessaoViewmodel.usuario.observe(getViewLifecycleOwner(),this);
    }

    @NonNull
    private FragmentSessaoBinding getBinding(){
        if (binding == null)
            binding = FragmentSessaoBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void onChanged(Object object) {
        if (object instanceof Bitmap) {
            configuraBitmap((Bitmap) object);
        } else if (object instanceof Usuario) {
            checkUsuario((Usuario) object);
        } else if (object instanceof Exception){
            onErro((Exception) object);
        }
    }

    private void configuraBitmap(Bitmap object) {
        getBinding().qrCode.setImageBitmap(object);
        mostraQrCode();
        sessaoViewmodel.iniciarConexao();
    }

    private void logar(Usuario usuario) {
        escondeQrCode();
        sessaoViewmodel.logar(usuario);
    }

    private void onErro(Exception exception) {
        verificaUsuarioLogado();
        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void escondeQrCode() {
        showView(View.GONE, View.VISIBLE);
    }

    private void mostraQrCode() {
        showView(View.VISIBLE, View.GONE);
    }

    private void showView(int qrCodeVisible, int loadingVisible) {
        getBinding().qrCode.setVisibility(qrCodeVisible);
        getBinding().loagind.setVisibility(loadingVisible);
    }

    private void checkUsuario(Usuario usuario) {
        if (usuario.account_uid == null || usuario.account_uid.isEmpty())
            logar(usuario);
        else verificaUsuarioLogado();
    }

    private void verificaUsuarioLogado() {
        boolean usuarioLogado = sessaoViewmodel.usuarioLogado();
        if (usuarioLogado) {
            Observer<Boolean> observer = (Observer<Boolean>) requireActivity();
            observer.onChanged(true);
        } else init();
    }

    private void init() {
        try {
            sessaoViewmodel.gerarQrCode(requireContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        verificaUsuarioLogado();
    }
}