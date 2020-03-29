package ch.virustracker.app.controller;

import java.util.List;

import ch.virustracker.app.controller.p2pkit.ITrackerController;
import ch.virustracker.app.controller.p2pkit.P2PKitTrackerController;
import ch.virustracker.app.controller.restapi.RestApiController;
import ch.virustracker.app.model.database.VtDatabase;
import ch.virustracker.app.model.database.receiveevent.ReceiveEvent;
import ch.virustracker.app.model.ReportToken;
import ch.virustracker.app.model.proximityevent.IProximityEventResolver;

public class Controller {

    private static final long SEARCH_BACKTIME_MS = 1000 * 60 * 60 * 24 * 20; // search the last 20 days for infected tokens
    private final RestApiController restApiController;
    private IProximityEventResolver proximityEventProvider;
    private ITrackerController trackerController = new P2PKitTrackerController();

    public Controller() {
        this.restApiController = new RestApiController();
    }

    public void fetchNewInfections() {
        restApiController.fetchInfectedTokens(null);
    }

    public void onNewInfectedTokens(List<ReportToken> reportTokenList) {
        List<ReceiveEvent> seenTokens = VtDatabase.getInstance().receivedTokenDao().selectByTimeSpan(System.currentTimeMillis() - SEARCH_BACKTIME_MS, System.currentTimeMillis());
        //proximityEventProvider.getProximityEvents(seenTokens, infectedTokenList);
    }

    public void startTracking() {
        trackerController.startTracker();
    }

    public void stopTracking() {
        trackerController.stopTracker();
    }
}
