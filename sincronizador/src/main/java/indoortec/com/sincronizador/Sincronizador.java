package indoortec.com.sincronizador;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import indoortec.com.apicontract.ApiIndoorTec;
import indoortec.com.entity.ApiStorageItem;
import indoortec.com.entity.Conexao;
import indoortec.com.entity.PlayList;
import indoortec.com.entity.Usuario;
import indoortec.com.observer.Observer;
import indoortec.com.providercontract.PlayListProvider;
import indoortec.com.providercontract.UsuarioProvider;
import indoortec.com.synccontract.SyncPlaylist;

@SuppressLint("StaticFieldLeak")
@Singleton
public class Sincronizador implements SyncPlaylist {
    public static int DELAY_SYNC = 10;
    public static int DELAY_FUNCION = 10;
    public static boolean ENVIA_LOGS = false;
    private ApiIndoorTec api;
    private final PlayListProvider provider;
    private final UsuarioProvider usuarioProvider;
    private final Handler handler = new Handler();

    private Observer<Object> playerViewModelObserver;
    private final List<String> logs = new ArrayList<>();
    private int DELAY_CONEXAO = 5;
    private final List<ApiStorageItem> pendencias = new ArrayList<>();

    @Inject
    public Sincronizador(ApiIndoorTec api, PlayListProvider provider, UsuarioProvider usuarioProvider) {
        this.api = api;
        this.provider = provider;
        this.usuarioProvider = usuarioProvider;
    }

    @Override
    public void logar(Usuario usuario,Observer<Object> viewModelObserver) {
        api.logar(usuario, observer -> {
            if (observer instanceof Exception){
                viewModelObserver.observer(observer);
                return;
            }
            Usuario user = (Usuario) observer;
            user.logado = true;
            usuarioProvider.gravar(user);
            viewModelObserver.observer(user);
        });
    }

    @Override
    public boolean usuarioLogado() {
        logs.add("usuarioLogado()");
        return usuarioProvider.usuarioLogado() != null;
    }

    @Override
    public void setObserver(Observer<Object> observer) {
        this.playerViewModelObserver = observer;
    }

    private final Runnable runnable = this::sincronizar;

    private void sync() {
        logs.add("sync()");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,DELAY_SYNC * 1000L);
    }

    public void sincronizar() {
        logs.add("sincronizar()");
        sync();
        List<String> log = new ArrayList<>(logs);
        logs.clear();
        enviarLogs(new ArrayList<>(log));
        validarAutenticacao();
        logs.add("Verificando se há atualizações");

        api.sincronizar(nuvemPlaylist -> {
            logs.add("nuvemPlaylist ->");
            nuvemPlaylist = validarPlayList(nuvemPlaylist);

            List<PlayList> localPlaylist = provider.fetchAll();

            logs.add("localPlaylist");

            List<ApiStorageItem> midiasDownload = new ArrayList<>();

            for (int x = 0; x < nuvemPlaylist.size(); x++) {
                ApiStorageItem nuvemMidia = nuvemPlaylist.get(x);
                logs.add("nuvemPlaylist "+x+" : "+nuvemMidia.getStorage());

                boolean baixar = true;

                for (int y = 0; y < localPlaylist.size(); y++) {

                    PlayList localMidia = localPlaylist.get(y);

                    if (x == y && localMidia.storage.equals(nuvemMidia.getStorage())) {
                        baixar = false;
                        break;
                    }
                }

                if (baixar)
                    midiasDownload.add(nuvemMidia);
            }

            logs.add("--------------------------------------------");

            for (PlayList playList : localPlaylist)
                logs.add("localPlaylist :" + playList.getStorage());

            logs.add("--------------------------------------------");

            List<ApiStorageItem> pendentePlaylist = new ArrayList<>();

            for (ApiStorageItem nuvemMidia : midiasDownload) {

                boolean download = true;

                for (PlayList localMidia : localPlaylist) {

                    if (localMidia.storage.equals(nuvemMidia.getStorage())) {

                        download = false;

                        break;
                    }
                }

                if (download)
                    pendentePlaylist.add(nuvemMidia);
            }

            logs.add(pendentePlaylist.size() == 0 ? "Nenhum item para baixar" : "Iniciando download de midias");

            pendencias.clear();
            pendencias.addAll(pendentePlaylist);

            baixarMidia(0, nuvemPlaylist, localPlaylist);
        }, observable -> playerViewModelObserver.observer(observable));
    }

    private void enviarLogs(ArrayList<String> strings) {
        if (ENVIA_LOGS) {
            api.enviarLog(strings);
        }
    }

    private void validarAutenticacao() {
        api.isRemove(Throwable::printStackTrace, this::deslogar);
    }

    private void deslogar(Boolean remove) {
        if (remove) {
            api.deslogar();
            usuarioProvider.remove();
        }
        playerViewModelObserver.observer(remove);
    }

    @Override
    public Usuario usuarioLogado(String deviceId) {
        Usuario usuario = usuarioProvider.usuarioLogado();
        if (usuario != null){
            String uid_user = usuario.account_uid;
            api.configuraApi(deviceId,uid_user);
            baixarFuncionalidades();
        }
        return usuarioProvider.usuarioLogado();
    }

    private final Runnable runnableFuncionalidades = () -> {
        api.funcionalidades(observable -> {
            try {
                Object obj = observable.get("enviarlogs");
                ENVIA_LOGS = Boolean.parseBoolean(String.valueOf(obj));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                Object obj = observable.get("delay_sincronizacao_funcoes");
                DELAY_FUNCION = Integer.parseInt(String.valueOf(obj));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                Object obj = observable.get("delay_sincronizacao");
                DELAY_SYNC = Integer.parseInt(String.valueOf(obj));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                Object obj = observable.get("delay_conexao");
                DELAY_CONEXAO = Integer.parseInt(String.valueOf(obj));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            baixarFuncionalidades();
        });
    };
    private void baixarFuncionalidades() {
        handler.removeCallbacks(runnableFuncionalidades);
        handler.postDelayed(runnableFuncionalidades, DELAY_FUNCION * 1000L);
    }

    @Override
    public void enviarDados(Conexao conexao, Observer<Boolean> voidObserver, Observer<Exception> exceptionObserver) {
        new Handler().postDelayed(() -> api.enviarDados(conexao,voidObserver,exceptionObserver),DELAY_CONEXAO * 1000L);
    }

    private void baixarMidia(int position, List<ApiStorageItem> itemsServidor, List<PlayList> itemsLocal) {
        if (position < pendencias.size()) {
            logs.add("Preparando para baixar item. Restam : "+pendencias.size());

            ApiStorageItem itemDownload = pendencias.get(position);

            String storage = itemDownload.getStorage();
            String extensao = storage.substring(storage.lastIndexOf("."));

            itemDownload.nomeTemporario = System.currentTimeMillis() + extensao;

            logs.add("nomeTemporario:"+itemDownload.nomeTemporario);

            playerViewModelObserver.observer(pendencias.size() + "Itens para baixar");

            playerViewModelObserver.observer("Iniciando download. "+pendencias.size() + "Itens restante");

            api.download(itemDownload, sucesso -> {
                logs.add("Midia baixada:"+itemDownload.getStorage());

                playerViewModelObserver.observer("Download de midia concluido");

                if (pendencias.contains(itemDownload)) {
                    moverParaPastaPlaylist(itemDownload);
                    atualizaPlaylist(itemsServidor,itemsLocal);
                } else renomear(itemDownload);
            }, exception -> {
                logs.add("Erro ao baixar midia:"+itemDownload.getStorage()+"\n"+exception.getMessage());
                playerViewModelObserver.observer("logs.add(\"Midia baixada:\"+itemDownload.getStorage());");
                exception.printStackTrace();
                if (pendencias.contains(itemDownload)) {
                    baixarMidia(position + 1,itemsServidor,itemsLocal);
                }
            });

        } else atualizaPlaylist(itemsServidor,itemsLocal);
    }

    private void renomear(ApiStorageItem itemDownload) {
        logs.add("renomear()");
        File pendencias = getPendenciasFile();
        File pendente = new File(pendencias, itemDownload.nomeTemporario);
        File baixado = new File(pendencias, itemDownload.getStorage());
        pendente.renameTo(baixado);
        logs.add("Sucesso:"+pendente.exists());
        logs.add("---------------------------------");
    }

    @NonNull
    private File getPendenciasFile() {
        File root = new File(Environment.getExternalStorageDirectory(),"indoortec");

        if (!root.exists()){
            root.mkdir();
            root.mkdirs();
        }

        File pendencias = new File(root,"pendencias");

        if (!pendencias.exists()){
            pendencias.mkdir();
            pendencias.mkdirs();
        }
        return pendencias;
    }

    private File getPlaylistFile() {
        File root = new File(Environment.getExternalStorageDirectory(),"indoortec");
        File playlist = new File(root,"playlist");

        if (!playlist.exists()){
            playlist.mkdir();
            playlist.mkdirs();
        }
        return playlist;
    }

    private void moverParaPastaPlaylist(ApiStorageItem itemDownload) {
        logs.add("moverParaPastaPlaylist()");
        File pendencias = getPendenciasFile();
        File playlist = getPlaylistFile();

        logs.add("Movendo midias : " + itemDownload.getStorage() + " para pasta de playlist");

        File pendente = new File(pendencias, itemDownload.nomeTemporario);

        if (!pendente.exists()){
            logs.add("A midia não foi salva como nome temporario");
            pendente = new File(pendencias, itemDownload.getStorage());
        } else {
            logs.add("A midia foi salva como nome temporario:"+itemDownload.nomeTemporario);
            String fileSize = String.valueOf(pendente.length());

            if (fileSize.equals(itemDownload.getSize())) {
                File reproduzir = new File(playlist, itemDownload.getStorage());

                pendente.renameTo(reproduzir);
                logs.add("Sucesso:"+reproduzir.exists());
                logs.add("------------------------------");
            }
            return;
        }

        if (!pendente.exists()){
            logs.add("A midia não foi salva com sucesso");
            logs.add("Sucesso:"+false);
            logs.add("------------------------------");
        } else {
            String fileSize = String.valueOf(pendente.length());

            if (fileSize.equals(itemDownload.getSize())) {
                File reproduzir = new File(playlist, itemDownload.getStorage());

                pendente.renameTo(reproduzir);
                logs.add("Sucesso:"+reproduzir.exists());
                logs.add("------------------------------");
            }
        }
    }

    private void atualizaPlaylist(List<ApiStorageItem> nuvemPlaylist, List<PlayList> localPlaylist) {
        logs.add("atualizaPlaylist() nuvemPlaylist-> "+nuvemPlaylist.size()+"|localPlaylist->"+localPlaylist.size());
        SincronizaDados sincronizaDados = new SincronizaDados(nuvemPlaylist,localPlaylist, provider,logs);
        if (playerViewModelObserver != null){
            playerViewModelObserver.observer(sincronizaDados);
        }
    }

    private List<ApiStorageItem> validarPlayList(List<ApiStorageItem> playListList) {
        List<ApiStorageItem> pendentes = new ArrayList<>();

        for (ApiStorageItem playList : playListList){
            if (api.existe(playList.getStorage()))
                pendentes.add(playList);
        }
        logs.add("validarPlayList -> pendentes");
        return pendentes;
    }
}
