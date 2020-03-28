package ch.virustracker.app.controller;

import android.app.Application;
import android.content.Context;

import ch.virustracker.app.model.Model;

public class VtApp extends Application {

    private Model model;
    private static VtApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        this.model = new Model();
    }

    public static Context getContext() {
        return instance.getContext();
    }

    public static Model getModel() {
        return instance.model;
    }

    public static String string(int id) {
        return getContext().getString(id);
    }
}
