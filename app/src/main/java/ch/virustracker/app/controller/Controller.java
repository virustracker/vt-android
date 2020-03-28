package ch.virustracker.app.controller;

import java.util.List;

import ch.virustracker.app.controller.restapi.InfectedTokenResponse;
import ch.virustracker.app.controller.restapi.RestApiController;
import ch.virustracker.app.model.database.VtDatabase;
import ch.virustracker.app.model.database.infectedtoken.InfectedToken;
import ch.virustracker.app.model.database.receivedtoken.ReceivedToken;
import ch.virustracker.app.model.proximityevent.IProximityEventProvider;
import ch.virustracker.app.model.proximityevent.ProximityEvent;

public class Controller {

    private static final long SEARCH_BACKTIME_MS = 1000 * 60 * 60 * 24 * 20; // search the last 20 days for infected tokens
    private final RestApiController restApiController;
    private IProximityEventProvider proximityEventProvider;

    public Controller() {
        this.restApiController = new RestApiController();
    }

    public void fetchNewInfections() {
        restApiController.fetchInfectedTokens(null);
    }

    public void onNewInfectedTokens(List<InfectedToken> infectedTokenList) {
        List<ReceivedToken> seenTokens = VtDatabase.getInstance().receivedTokenDao().selectByTimeSpan(System.currentTimeMillis() - SEARCH_BACKTIME_MS, System.currentTimeMillis());
        //proximityEventProvider.getProximityEvents(seenTokens, infectedTokenList);
    }
}
