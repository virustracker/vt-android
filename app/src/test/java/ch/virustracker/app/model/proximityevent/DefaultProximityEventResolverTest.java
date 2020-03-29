package ch.virustracker.app.model.proximityevent;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import ch.virustracker.app.model.ReportToken;
import ch.virustracker.app.model.Token;
import ch.virustracker.app.model.database.advertiseevent.AdvertiseEvent;
import ch.virustracker.app.model.database.receiveevent.ReceiveEvent;

class Fixture {
    LinkedList<ReceiveEvent> receiveEvents = new LinkedList<>();
    LinkedList<ReportToken> reportTokens = new LinkedList<>();

    public Fixture addContactEvent(long secondsBetweenEvents, String testResult,
                                   String reportType) {
        long lastEventTimestampMs = (receiveEvents.isEmpty() ? 0 :
                receiveEvents.getLast().getTimestampMs());
        ReceiveEvent event = new ReceiveEvent("AAAAAAAAAAAAAAAA",
                lastEventTimestampMs + secondsBetweenEvents * 1000);
        receiveEvents.add(event);
        ReportToken reportToken = new ReportToken(event.getTokenValue(), testResult, reportType);
        reportTokens.add(reportToken);
        return this;
    }
}

public class DefaultProximityEventResolverTest {
    @Test
    public void testGroupCloseContactEvents() {
        Fixture fixture = new Fixture();
        fixture.addContactEvent(0, ReportToken.POSITIVE, ReportToken.SELF_REPORTED);
        // Create a new event, two minutes after the previous one.
        fixture.addContactEvent(120, ReportToken.POSITIVE, ReportToken.SELF_REPORTED);

        List<ProximityEvent> proximityEvents =
                new DefaultProximityEventResolver().resolveProximityEvents(fixture.receiveEvents,
                        fixture.reportTokens);
        assert (proximityEvents.size() == 1);
    }

    @Test
    public void testDoNotGroupFarApartContactEvents() {
        Fixture fixture = new Fixture();
        fixture.addContactEvent(0, ReportToken.POSITIVE, ReportToken.SELF_REPORTED);
        // Create a new event, one hour after the previous one
        fixture.addContactEvent(60*60, ReportToken.POSITIVE, ReportToken.SELF_REPORTED);

        List<ProximityEvent> proximityEvents =
                new DefaultProximityEventResolver().resolveProximityEvents(fixture.receiveEvents,
                        fixture.reportTokens);
        assert (proximityEvents.size() == 2);
    }

}

