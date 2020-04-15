package ch.virustracker.app.controller;

import android.app.Application;
import android.content.Context;

import org.dpppt.android.sdk.DP3T;
import org.dpppt.android.sdk.internal.backend.models.ApplicationInfo;

import ch.virustracker.app.R;
import ch.virustracker.app.model.Model;
import ch.virustracker.app.model.database.VtDatabase;

public class VtApp extends Application {

    private Model model;
    private static VtApp instance;
    private Controller controller;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationInfo applicationInfo = new ApplicationInfo("ch.virustracker.app", getString(R.string.api_base_url));
        DP3T.init(this, applicationInfo);
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
