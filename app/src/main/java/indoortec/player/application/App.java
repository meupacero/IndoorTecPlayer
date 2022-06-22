package indoortec.player.application;

import androidx.fragment.app.FragmentActivity;

public class App {
    private FragmentActivity context;
    private boolean autoStartInit = false;
    private boolean playlistServiceInit = false;
    private boolean visible;
    private static App instance;

    public static App getInstance() {
        if (instance == null)
            instance = new App();
        return instance;
    }

    public boolean isPlaylistServiceInit() {
        return playlistServiceInit;
    }

    public void setPlaylistServiceInit(boolean playlistServiceInit) {
        this.playlistServiceInit = playlistServiceInit;
    }

    public boolean autoStartNaoIniciado() {
        return !autoStartInit;
    }

    public void iniciarAutoStart() {
        this.autoStartInit = true;
    }

    public static void setInstance(App instance) {
        App.instance = instance;
    }

    public FragmentActivity getContext() {
        return context;
    }

    public void setContext(FragmentActivity context) {
        this.context = context;
    }

    public void activityVisivel(){
        this.visible = true;
    }

    public void activityPausada(){
        this.visible = false;
    }

    public boolean isVisible() {
        return visible;
    }
}
