package indoortec.com.sessao.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.format.Formatter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import indoortec.com.entity.Usuario;
import indoortec.com.observer.Observer;
import indoortec.com.sessao.conexao.Server;
import indoortec.com.synccontract.SyncPlaylist;
@SuppressLint("HardwareIds")
@Singleton
public class SessaoViewmodel extends ViewModel implements Observer<Object> {
    private final MutableLiveData<Bitmap> _qrCode = new MutableLiveData<>();
    public final LiveData<Bitmap> qrCode = _qrCode;

    private final MutableLiveData<Usuario> _usuario = new MutableLiveData<>();
    public final LiveData<Usuario> usuario = _usuario;

    private final MutableLiveData<Exception> _exception = new MutableLiveData<>();
    public final LiveData<Exception> exception = _exception;

    private final SyncPlaylist sincronizador;
    private String deviceId;

    @Inject
    public SessaoViewmodel(SyncPlaylist sincronizador) {
        this.sincronizador = sincronizador;
    }

    public void gerarQrCode(Context context) throws Exception {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String addressIp = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        QRCodeWriter writer = new QRCodeWriter();
        ByteMatrix byteMatrix = writer.encode(addressIp, BarcodeFormat.QR_CODE,512,512);

        int largura = 512;
        int altura = 512;

        Bitmap bitmap = Bitmap.createBitmap(largura,altura,Bitmap.Config.RGB_565);

        for (int x = 0; x < largura; x++){
            for (int y = 0; y < altura; y++) {
                if (byteMatrix.get(y,x) == 0) {
                    bitmap.setPixel(x,y, Color.BLACK);
                }else {
                    bitmap.setPixel(x,y,Color.WHITE);
                }
            }
        }
        _qrCode.setValue(bitmap);
    }

    public void iniciarConexao() {
        new Server().start(_usuario::postValue);
    }

    public static String getMacRede() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equals("eth0")||nif.getName().equals("wlan0")){
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "02:00:00:00:00:00";
                    }
                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes)
                        res1.append(String.format("%02X:",b));

                    if (res1.length() > 0)
                        res1.deleteCharAt(res1.length() - 1);
                    return res1.toString();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    public void logar(Usuario usuario) {
        usuario.deviceId = deviceId;
        sincronizador.logar(usuario, this);
    }

    @Override
    public void observer(Object observable) {
        if (observable instanceof Usuario){
            this._usuario.setValue((Usuario) observable);
        } else this._exception.setValue((Exception) observable);
    }

    public Usuario usuarioLogado() {

        return null;
    }
}
