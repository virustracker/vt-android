package ch.virustracker.app.controller.p2pkit;

import android.util.Log;

import ch.uepaa.p2pkit.P2PKitStatusListener;
import ch.uepaa.p2pkit.StatusResult;

public class P2PStatusListener implements P2PKitStatusListener {
    @Override
    public void onEnabled() {
        Log.v("StatusListener", "Enabled");
    }

    @Override
    public void onDisabled() {
        Log.v("StatusListener", "Enabled");
    }

    @Override
    public void onError(StatusResult result) {
        Log.v("StatusListener", "Error: " + result);
    }

    @Override
    public void onException(Throwable throwable) {
        Log.v("StatusListener", "Exception: " + throwable.toString());
    }
}
