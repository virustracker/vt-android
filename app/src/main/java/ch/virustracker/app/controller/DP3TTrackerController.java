package ch.virustracker.app.controller;

import org.dpppt.android.sdk.DP3T;

import ch.virustracker.app.controller.bluetooth.ITrackerController;
import ch.virustracker.app.view.MainActivity;

public class DP3TTrackerController implements ITrackerController {

    public DP3TTrackerController() {
        DP3T.start(VtApp.getContext(), true, true);
    }

    @Override
    public void startTracker(MainActivity mainActivity) {
        DP3T.start(VtApp.getContext());
    }

    @Override
    public void stopTracker() {
        DP3T.stop(VtApp.getContext());
    }
}
