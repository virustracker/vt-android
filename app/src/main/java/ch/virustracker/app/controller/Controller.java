package ch.virustracker.app.controller;

import org.dpppt.android.sdk.internal.database.Database;
import org.dpppt.android.sdk.internal.database.models.Handshake;

import java.util.HashSet;
import java.util.List;

import ch.virustracker.app.controller.bluetooth.ITrackerController;
import ch.virustracker.app.controller.restapi.IDataUpdateListener;
import ch.virustracker.app.controller.restapi.RestApiController;
import ch.virustracker.app.controller.restapi.submitreport.SubmitReportController;
import ch.virustracker.app.controller.restapi.submitreport.SubmitReportTokensData;
import ch.virustracker.app.model.ReportToken;
import ch.virustracker.app.model.database.VtDatabase;
import ch.virustracker.app.model.database.receiveevent.ReceiveEvent;
import ch.virustracker.app.model.proximityevent.DefaultProximityEventResolver;
import ch.virustracker.app.model.proximityevent.IProximityEventResolver;
import ch.virustracker.app.model.proximityevent.ProximityEvent;
import ch.virustracker.app.view.MainActivity;

public class Controller {

    private static final long SEARCH_BACKTIME_MS = 1000 * 60 * 60 * 24 * 20; // search the last 20 days for infected tokens

    private final RestApiController restApiController;
    private IProximityEventResolver proximityEventResolver;
    private ITrackerController trackerController = new DP3TTrackerController();
    private SubmitReportController testReportController = new SubmitReportController();
    private HashSet<IDataUpdateListener> dataUpdateListeners = new HashSet<>();

    public Controller() {
        this.restApiController = new RestApiController();
        this.proximityEventResolver = new DefaultProximityEventResolver();
    }

    public void fetchNewInfections() {
        restApiController.fetchReportTokens();
    }

    public void onNewReportTokens(List<ReportToken> reportTokenList) {
        Database database = new Database(VtApp.getContext());
        List<Handshake> handshakes = database.getHandshakes();
        List<ReceiveEvent> seenTokens = ReceiveEvent.getReceiveEventsFromHandshakes(handshakes);
        //List<ReceiveEvent> seenTokens = VtDatabase.getInstance().receivedTokenDao().selectByTimeSpan(System.currentTimeMillis() - SEARCH_BACKTIME_MS, System.currentTimeMillis());
        List<ProximityEvent> proximityEvents = proximityEventResolver.resolveProximityEvents(seenTokens, reportTokenList);
        for (ProximityEvent proximityEvent : proximityEvents) VtDatabase.getInstance().proximityEventDao().insertAll(proximityEvent);
        for (IDataUpdateListener listener : dataUpdateListeners) {
            if (listener != null) listener.newProximityEventsAvailable();
        }
    }

    public void addDataUpdateListener(IDataUpdateListener listener) {
        dataUpdateListeners.add(listener);
    }

    public void removeDataUpdateListener(IDataUpdateListener listener) {
        dataUpdateListeners.remove(listener);
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
        // perform DB call in non-ui thread
        new Thread(() -> {
            SubmitReportTokensData testReport = testReportController.generateTestReport(nDays);
            restApiController.submitReportTokens(testReport);
        }).start();
    }

    /**
     * {@link ch.virustracker.app.controller.restapi.RestApiService} callback, after a report has been submitted.
     *
     * @param code can be 200 (OK), 400 (Bad Request) or 500 (Internal Server Error)
     */
    public void onSubmittedReportTokens(int code) {
        // TODO: provide user feedback, if needed
        for (IDataUpdateListener listener : dataUpdateListeners) {
            if (listener != null) listener.newTestReportStateAvailable();
        }
    }

}
