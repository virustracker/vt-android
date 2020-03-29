package ch.virustracker.app.model.proximityevent;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import ch.virustracker.app.model.ReportToken;
import ch.virustracker.app.model.database.receiveevent.ReceiveEvent;

public class DefaultProximityEventResolverTest {
    LinkedList<ReceiveEvent> receiveEvents = new LinkedList<>();
    LinkedList<ReportToken> reportTokens = new LinkedList<>();
    int tokenId = 0;

    @Before
    public void setUp() {
        receiveEvents.clear();
        reportTokens.clear();
    }

    public void addContactEvent(long timestampSeconds, String testResult,
                                String reportType) {
        String tokenValue = "AAAAAAAAAAAAAAAA" + tokenId++;
        ReceiveEvent event = new ReceiveEvent(tokenValue, timestampSeconds * 1000);
        receiveEvents.add(event);
        ReportToken reportToken = new ReportToken(event.getTokenValue(), testResult, reportType);
        reportTokens.add(reportToken);
    }

    public void addSubsequentContactEvent(long secondsBetweenEvents, String testResult,
                                          String reportType) {
        long lastEventTimestampMs = (receiveEvents.isEmpty() ? 0 :
                receiveEvents.getLast().getTimestampMs());
        long newTimestampSeconds = lastEventTimestampMs / 1000 + secondsBetweenEvents;
        addContactEvent(newTimestampSeconds, testResult, reportType);
    }

    List<ProximityEvent> resolveProximityEvents() {
        return new DefaultProximityEventResolver().resolveProximityEvents(receiveEvents,
                reportTokens);
    }

    @Test
    public void testGroupCloseContactEvents() {
        addSubsequentContactEvent(0, ReportToken.POSITIVE, ReportToken.SELF_REPORTED);
        // Create a new event, two minutes after the previous one.
        addSubsequentContactEvent(120, ReportToken.POSITIVE, ReportToken.SELF_REPORTED);
        List<ProximityEvent> proximityEvents = resolveProximityEvents();
        assert (proximityEvents.size() == 1);
    }

    @Test
    public void testDoNotGroupFarApartContactEvents() {
        addSubsequentContactEvent(0, ReportToken.POSITIVE, ReportToken.SELF_REPORTED);
        // Create a new event, one hour after the previous one
        addSubsequentContactEvent(60 * 60, ReportToken.POSITIVE, ReportToken.SELF_REPORTED);
        List<ProximityEvent> proximityEvents = resolveProximityEvents();
        assert (proximityEvents.size() == 2);
    }

    @Test
    public void testDoNotGroupContactEventsWithDifferentType() {
        addSubsequentContactEvent(0, ReportToken.POSITIVE, ReportToken.SELF_REPORTED);
        addSubsequentContactEvent(120, ReportToken.POSITIVE, ReportToken.VERIFIED);
        List<ProximityEvent> proximityEvents = resolveProximityEvents();
        assert (proximityEvents.size() == 2);
    }

    @Test
    public void testGroupUnsortedContactEvents() {
        addContactEvent(120, ReportToken.POSITIVE, ReportToken.SELF_REPORTED);
        addContactEvent(60, ReportToken.POSITIVE, ReportToken.SELF_REPORTED);
        addContactEvent(180, ReportToken.POSITIVE, ReportToken.SELF_REPORTED);

        List<ProximityEvent> proximityEvents = resolveProximityEvents();
        assert (proximityEvents.size() == 1);
    }

    @Test
    public void testGroupFilterNonPositiveResults() {
        addContactEvent(60, ReportToken.POSITIVE, ReportToken.SELF_REPORTED);
        addContactEvent(90, ReportToken.NEGATIVE, ReportToken.SELF_REPORTED);
        addContactEvent(120, ReportToken.POSITIVE, ReportToken.SELF_REPORTED);
        addContactEvent(150, ReportToken.UNKNOWN, ReportToken.SELF_REPORTED);
        addContactEvent(180, ReportToken.POSITIVE, ReportToken.SELF_REPORTED);

        List<ProximityEvent> proximityEvents = resolveProximityEvents();
        assert (proximityEvents.size() == 1);
    }
}

