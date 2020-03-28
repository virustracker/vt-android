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

    public Context getContext() {
        return instance.getContext();
    }

    public Model getModel() {
        return instance.model;
    }
}
