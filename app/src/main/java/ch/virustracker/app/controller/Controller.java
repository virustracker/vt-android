package ch.virustracker.app.controller;

import android.util.Base64;
import android.util.Log;

import java.util.List;
import java.util.stream.Collectors;

import ch.virustracker.app.controller.altbeacon.AltBeaconTrackerController;
import ch.virustracker.app.controller.p2pkit.ITrackerController;
import ch.virustracker.app.controller.p2pkit.P2PKitTrackerController;
import ch.virustracker.app.controller.restapi.RestApiController;
import ch.virustracker.app.controller.restapi.dao.Report;
import ch.virustracker.app.controller.restapi.dao.SubmitReportTokensData;
import ch.virustracker.app.controller.restapi.dao.Token;
import ch.virustracker.app.model.database.VtDatabase;
import ch.virustracker.app.model.database.advertiseevent.AdvertiseEvent;
import ch.virustracker.app.model.database.location.Location;
import ch.virustracker.app.model.database.receiveevent.ReceiveEvent;
import ch.virustracker.app.model.ReportToken;
import ch.virustracker.app.model.proximityevent.IProximityEventResolver;
import ch.virustracker.app.view.MainActivity;

public class Controller {

    private static final long SEARCH_BACKTIME_MS = 1000 * 60 * 60 * 24 * 20; // search the last 20 days for infected tokens
    private final RestApiController restApiController;
    private IProximityEventResolver proximityEventProvider;
    private ITrackerController trackerController = new AltBeaconTrackerController();

    public Controller() {
        this.restApiController = new RestApiController();
    }

    public void fetchNewInfections() {
        restApiController.fetchReportTokens(null);
    }

    public void onNewReportTokens(List<ReportToken> reportTokenList) {
        List<ReceiveEvent> seenTokens = VtDatabase.getInstance().receivedTokenDao().selectByTimeSpan(System.currentTimeMillis() - SEARCH_BACKTIME_MS, System.currentTimeMillis());
        //proximityEventProvider.getProximityEvents(seenTokens, infectedTokenList);
    }

    public void startTracking(MainActivity mainActivity) {
        trackerController.startTracker(mainActivity);
    }

    public void stopTracking() {
        trackerController.stopTracker();
    }

    /**
     * Self-report positive and send the token for the last nDays
     *
     * @param nDays number of days since the user had symptoms
     */
    public void selfReportPositiveAndSubmitTokensWithLocation(final int nDays) {
        new Thread(() -> {
            // perform DB call in non-ui thread
            long searchBackTime = 1000 * 60 * 60 * 24 * nDays; // search the last nDays
            List<AdvertiseEvent> sentTokens = VtDatabase.getInstance().advertiseEventDao()
                    .selectByTimeSpan(System.currentTimeMillis() - searchBackTime, System.currentTimeMillis());
            Report report = new Report();
            report.setType("SELF_REPORT");
            report.setResult("POSITIVE");
            for (AdvertiseEvent ae : sentTokens) {
                Token token = new Token();
                token.setPreimage(Base64.encodeToString(ae.getPreImage(), Base64.NO_WRAP));
                Location loc = ae.getLocation();
                if (loc != null) {
                    token.setLat(loc.getLatitude());
                    token.setLong(loc.getLongitude());
                }
                report.getTokens().add(token);
            }
            SubmitReportTokensData data = new SubmitReportTokensData();
            data.setReport(report);
            restApiController.submitReportTokens(data);
//            Log.i("API", "Reported Positive and submit #" + sentTokens.size() + " tokens");
        }).start();
    }

    /**
     * {@link ch.virustracker.app.controller.restapi.RestApiService} callback, after a report has been submitted.
     *
     * @param code can be 200 (OK), 400 (Bad Request) or 500 (Internal Server Error)
     */
    public void onSubmittedReportTokens(int code) {
        // TODO: provide user feedback, if needed
//        Log.i("API", "Reported Submitted, response: " + code + ".");
    }

}
