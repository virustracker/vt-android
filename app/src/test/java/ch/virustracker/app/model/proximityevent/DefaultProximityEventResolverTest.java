package ch.virustracker.app.model.proximityevent;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import ch.virustracker.app.model.ReportToken;
import ch.virustracker.app.model.Token;
import ch.virustracker.app.model.database.advertiseevent.AdvertiseEvent;
import ch.virustracker.app.model.database.receiveevent.ReceiveEvent;


public class DefaultProximityEventResolverTest {
    @Test
    public void testResolve() {
        ReceiveEvent event1 = new ReceiveEvent();
        event1.setTokenValue("AAAAAAAAAAAAAAAA");
        ReportToken reportToken1 = new ReportToken(event1.getTokenValue(), ReportToken.POSITIVE,
                ReportToken.SELF_REPORTED);

        ReceiveEvent event2 = new ReceiveEvent();
        event2.setTokenValue("BBBBBBBBBBBBBBBB");
        ReportToken reportToken2 = new ReportToken(event2.getTokenValue(), ReportToken.POSITIVE,
                ReportToken.SELF_REPORTED);

        List<ReceiveEvent> receiveEvents = Arrays.asList(event1, event2);
        List<ReportToken> reportTokens = Arrays.asList(reportToken1, reportToken2);

        List<ProximityEvent> proximityEvents =
                new DefaultProximityEventResolver().resolveProximityEvents(receiveEvents,
                        reportTokens);
        assert(proximityEvents.size() == 2);
    }

}

