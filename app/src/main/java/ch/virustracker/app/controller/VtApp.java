package ch.virustracker.app.controller;

import android.app.Application;
import android.content.Context;

import ch.virustracker.app.model.Model;
import ch.virustracker.app.model.database.VtDatabase;

public class VtApp extends Application {

    private Model model;
    private static VtApp instance;
    private Controller controller;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        this.model = new Model();
        this.controller = new Controller();
    }

    public static Context getContext() {
        return instance;
    }

    public static Model getModel() {
        return instance.model;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        VtDatabase.closeDb();
    }

    public static String string(int id) {
        return getContext().getString(id);
    }

    public static Controller getController() {
        return instance.controller;
    }
}
