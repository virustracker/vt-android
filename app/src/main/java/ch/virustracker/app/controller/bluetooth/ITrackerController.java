package ch.virustracker.app.controller.bluetooth;

import ch.virustracker.app.view.MainActivity;

public interface ITrackerController {

    void startTracker(MainActivity mainActivity);
    void stopTracker();
}
