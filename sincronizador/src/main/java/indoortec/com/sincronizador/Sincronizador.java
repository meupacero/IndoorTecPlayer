package indoortec.com.sincronizador;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    public static final long INTERVALO_ENTRE_SINCRONIZACAO = 10;
    private final ApiIndoorTec api;
    private boolean sincronizando,revizao;
    private final PlayListProvider playListProvider;
    private final UsuarioProvider usuarioProvider;
    private final StorageReference storageReference;
    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sincronizar(observable -> playerViewModelObserver.observer(observable));
        }
    };
    private final String TAG = getClass().getName();
    private Observer<Object> playerViewModelObserver;

    @Inject
    public Sincronizador(ApiIndoorTec api, PlayListProvider playListProvider, UsuarioProvider usuarioProvider) {
        this.api = api;
        this.playListProvider = playListProvider;
        this.usuarioProvider = usuarioProvider;
        storageReference = FirebaseStorage.getInstance().getReference();
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
    public void setObserver(Observer<Object> observer) {
        this.playerViewModelObserver = observer;
    }

    @Override
    public void validarPlayList(Observer<Exception> exceptionObserver) {

        Log.d(TAG,"INICIANDO VALIDAÇÃO DA LISTA DE REPRODUÇÃO");

        api.sincronizar(observable -> {
            observable = validarPlayList(observable);

            List<PlayList> playListsLocal = playListProvider.fetchAll();

            Log.d(TAG,"COMPARANDO LISTA DE REPRODUÇÃO COM SERVIDOR");

            if (observable.size() != playListsLocal.size()) {
                revizao = true;
            } else if (observable.size() > 0) {
                for (int x = 0; x < observable.size(); x++){
                    PlayList local = playListsLocal.get(x);
                    ApiStorageItem nuvem = observable.get(x);
                    if (!local.storage.equals(nuvem.getStorage())){
                        revizao = true;
                        break;
                    }
                }
            }

            sincronizando = false;

            if (revizao) {
                Log.d(TAG,"POSSIVEIS DADOS NÃO COLETADOS. FAZENDO REVIZÃO");

                revizao = false;
                sincronizar(exceptionObserver);
            } else sincronizado(exceptionObserver);
        },exceptionObserver);
    }

    private void sincronizado(Observer<Exception> exceptionObserver) {
        Log.d(TAG,"------------VALIDAÇÃO CONCLUIDA--------------");

        api.removerMidiasCorrompidas(exceptionObserver);

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,1000L * INTERVALO_ENTRE_SINCRONIZACAO);
    }

    public void sincronizar(Observer<Exception> exceptionObserver) {
        if (sincronizando) {
            revizao = true;

            Log.d(TAG,"JA EXISTE UMA SINCRONIZAÇÃO EM ANDAMENTE, AO FINAL SERA FEITO UMA REVIZÃO");
            return;
        }

        validarAutenticacao();
        sincronizando = true;

        playerViewModelObserver.observer("Verificando se há atualizações");

        api.sincronizar(nuvemPlaylist -> {
            nuvemPlaylist = validarPlayList(nuvemPlaylist);

            if (nuvemPlaylist.size() == 0){
                playerViewModelObserver.observer("A playlist não está configurada");
                limpaPlaylist();
                return;
            }

            List<PlayList> localPlaylist = playListProvider.fetchAll();

            List<ApiStorageItem> midiasDownload = new ArrayList<>();

            for (int x = 0; x < nuvemPlaylist.size(); x ++){

                ApiStorageItem nuvemMidia = nuvemPlaylist.get(x);

                boolean baixar = true;

                for (int y = 0; y < localPlaylist.size(); y ++){

                    PlayList localMidia = localPlaylist.get(y);

                    if (x == y && localMidia.storage.equals(nuvemMidia.getStorage())) {
                        baixar = false;
                        break;
                    }
                }

                if (baixar)
                    midiasDownload.add(nuvemMidia);
            }

            List<ApiStorageItem> pendentePlaylist = new ArrayList<>();

            for (ApiStorageItem nuvemMidia : midiasDownload) {

                boolean download = true;

                for (PlayList localMidia : localPlaylist){

                    if (localMidia.storage.equals(nuvemMidia.getStorage())) {

                        download = false;

                        break;
                    }
                }

                if (download)
                    pendentePlaylist.add(nuvemMidia);
            }

            playerViewModelObserver.observer(pendentePlaylist.size() == 0 ? "A playlist está atualizada" : "Iniciando sincronização de midias");

            baixarMidia(0,nuvemPlaylist,localPlaylist,pendentePlaylist,exceptionObserver);
        },exceptionObserver);
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
        }
        return usuarioProvider.usuarioLogado();
    }

    @Override
    public void enviarDados(Conexao conexao, Observer<Boolean> voidObserver, Observer<Exception> exceptionObserver) {
        api.enviarDados(conexao,voidObserver,exceptionObserver);
    }

    private void baixarMidia(int position, List<ApiStorageItem> itemsServidor, List<PlayList> itemsLocal, List<ApiStorageItem> itemsPendentes, Observer<Exception> exceptionObserver) {
        if (revizao) {
            revizao = false;
            sincronizando = false;

            playerViewModelObserver.observer("Realizando revizção");

            sincronizar(exceptionObserver);
            return;
        }

        if (position < itemsPendentes.size()) {

            ApiStorageItem itemDownload = itemsPendentes.get(position);

            playerViewModelObserver.observer("Iniciando download");

            api.download(itemDownload, sucesso -> {
                playerViewModelObserver.observer("Midia baixada");
                atualizaPlaylist(itemsServidor,itemsLocal);
                sincronizando = false;
                sincronizar(exceptionObserver);
            }, exception -> {
                playerViewModelObserver.observer("Erro ao baixar midia");
                exception.printStackTrace();
                sincronizando = false;
                sincronizar(exceptionObserver);
            });

        } else atualizaPlaylist(itemsServidor,itemsLocal);
    }

    private void atualizaPlaylist(List<ApiStorageItem> nuvemPlaylist, List<PlayList> localPlaylist) {
        SincronizaDados sincronizaDados = new SincronizaDados(nuvemPlaylist,localPlaylist,playListProvider);
        if (playerViewModelObserver != null){
            playerViewModelObserver.observer(sincronizaDados);
        }
    }

    private void limpaPlaylist() {
        LimpaMidias limpaMidias = new LimpaMidias(playListProvider);
        if (playerViewModelObserver != null){
            playerViewModelObserver.observer(limpaMidias);
        }
    }

    private List<ApiStorageItem> validarPlayList(List<ApiStorageItem> playListList) {
        List<ApiStorageItem> pendentes = new ArrayList<>();

        for (ApiStorageItem playList : playListList){
            if (api.existe(playList.getStorage()))
                pendentes.add(playList);
        }
        return pendentes;
    }

}
