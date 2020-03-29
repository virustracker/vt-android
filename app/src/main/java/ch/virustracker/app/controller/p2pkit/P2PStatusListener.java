package ch.virustracker.app.controller.p2pkit;

import android.util.Log;

import ch.uepaa.p2pkit.P2PKitStatusListener;
import ch.uepaa.p2pkit.StatusResult;

public class P2PStatusListener implements P2PKitStatusListener {
    @Override
    public void onEnabled() {
        //Log.v("StatusListener", )
    }

    @Override
    public void onDisabled() {

    }

    @Override
    public void onError(StatusResult result) {

    }

    @Override
    public void onException(Throwable throwable) {

    }
}
