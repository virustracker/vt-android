package ch.virustracker.app.controller;

import ch.ubique.android.starsdk.STARTracing;
import ch.virustracker.app.controller.bluetooth.ITrackerController;
import ch.virustracker.app.view.MainActivity;

public class StarTrackerController implements ITrackerController {

    public StarTrackerController() {
        STARTracing.init(VtApp.getContext(), "ch.virustracker.app");
    }

    @Override
    public void startTracker(MainActivity mainActivity) {
        STARTracing.start(VtApp.getContext());
    }

    @Override
    public void stopTracker() {
        STARTracing.stop(VtApp.getContext());
    }
}
