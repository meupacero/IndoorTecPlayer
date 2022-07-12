package indoortec.com.sessao.conexao;
import androidx.lifecycle.Observer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import indoortec.com.entity.BuildConfig;
import indoortec.com.entity.Usuario;

public class Server {

    private static final int PORT = 9090;

    public void start(Observer<Usuario> observer) {
        new Thread(){
            @Override
            public void run() {
                super.run();
//                if (BuildConfig.DEBUG){
//                    Usuario usuario = new Usuario();
//                    usuario.usuario = "teste@teste.com";
//                    usuario.senha = "123456";
//
//                    observer.onChanged(usuario);
//                    return;
//                }
                ServerSocket serverSocket = null;
                Socket socket = null;
                try {
                    serverSocket = new ServerSocket(PORT);
                    socket = serverSocket.accept();

                    InputStream input = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                    String jsonString = reader.readLine();

                    JSONObject jsonObject = new JSONObject(jsonString);

                    Usuario usuario = new Usuario();
                    usuario.usuario = jsonObject.getString("usuario");
                    usuario.senha = jsonObject.getString("senha");

                    observer.onChanged(usuario);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (serverSocket != null){
                            serverSocket.close();
                        }
                        if (socket != null){
                            socket.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
