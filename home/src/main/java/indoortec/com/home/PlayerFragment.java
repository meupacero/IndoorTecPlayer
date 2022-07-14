package indoortec.com.home;

import static android.content.Context.POWER_SERVICE;
import static indoortec.com.entity.PlayList.VIDEO;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import java.io.File;

import javax.inject.Inject;

import indoortec.com.di.ViewModelFactory;
import indoortec.com.home.databinding.HomeScreenBinding;
import indoortec.com.home.model.Midia;
import indoortec.com.home.viewmodel.PlayerViewmodel;

public class PlayerFragment extends Fragment implements Observer<Midia>, MediaPlayer.OnCompletionListener {

    @Inject
    ViewModelFactory factory;
    private HomeScreenBinding binding;
    private Bitmap bitmap;
    private Runnable run1,run2;
    private final Handler handler = new Handler();
    private PlayerViewmodel playerViewmodel;
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        run1 = this::mostraPlayerVideo;
        run2 = this::resetX;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getBinding().getRoot();
    }

    @NonNull
    private HomeScreenBinding getBinding() {
        if (binding == null) {
            binding = HomeScreenBinding.inflate(getLayoutInflater());
            binding.videoPlayer.setOnCompletionListener(this);
        }
        return binding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DaggerHomeComponent.factory().create(requireContext()).inject(this);
        playerViewmodel = new ViewModelProvider(this,factory).get(PlayerViewmodel.class);
        playerViewmodel.midia.observe(getViewLifecycleOwner(), this);
        playerViewmodel.usuario.observe(getViewLifecycleOwner(), aBoolean -> {
            if (!aBoolean) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext().getApplicationContext());
                preferences.edit().putBoolean("logado", false).apply();
                Observer<Boolean> observer = (Observer<Boolean>) requireActivity();
                observer.onChanged(false);
            }
        });
        playerViewmodel._reset.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean){
                playerViewmodel._reset.removeObservers(getViewLifecycleOwner());
                playerViewmodel._reset.setValue(false);

                Intent intent = new Intent(requireActivity(), requireActivity().getClass());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                requireActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void onChanged(Midia midia) {
        if (midia.tipo.equals(VIDEO)){
            playVideo(midia);
        } else playImagem(midia);
    }

    private void playVideo(Midia midia) {
        getBinding().videoPlayer.setVideoPath(midia.file.getPath());
        getBinding().videoPlayer.setVisibility(View.VISIBLE);
        getBinding().videoPlayer.start();
        soltarTela();
    }

    private void playImagem(Midia midia) {
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }

        File imgFile = midia.file;

        if(imgFile.exists()){
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }

        segurarTelaLigada();

        getBinding().imagem.setImageDrawable(new BitmapDrawable(getResources(),bitmap));
        getBinding().contentImagem.animate().alpha(1).setDuration(1000L);

        handler.removeCallbacks(run1);
        handler.postDelayed(run1, 15000L);
    }

    private void mostraPlayerVideo() {
        getBinding().contentImagem.animate().alpha(0).setDuration(1000L).start();
        getBinding().videoPlayer.animate().alpha(1).setDuration(1000L).start();

        handler.removeCallbacks(run2);
        handler.postDelayed(run2, 1000);
    }

    private void resetX() {
        getBinding().imagem.setX(0);
        playerViewmodel.reproducaoConcluida();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.reset();
        getBinding().videoPlayer.setVisibility(View.GONE);
        getBinding().videoPlayer.seekTo(0);
        playerViewmodel.reproducaoConcluida();
    }

    private void soltarTela() {
        if (wakeLock != null && playerViewmodel.seguraDisplay()) {
            wakeLock.release();
            playerViewmodel.seguraDisplay(false);
        }
    }

    @SuppressLint({"InvalidWakeLockTag", "WakelockTimeout"})
    private void segurarTelaLigada() {
        if (!playerViewmodel.seguraDisplay()) {
            playerViewmodel.seguraDisplay(true);
            PowerManager powerManager = (PowerManager) requireContext().getSystemService(POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "state");
            wakeLock.acquire();
        }
    }
}
