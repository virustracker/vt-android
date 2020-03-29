package ch.virustracker.app.model.proximityevent;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.virustracker.app.model.Model;
import ch.virustracker.app.model.ReportToken;
import ch.virustracker.app.model.Token;
import ch.virustracker.app.model.database.proximityevent.Distance;
import ch.virustracker.app.model.database.receiveevent.ReceiveEvent;

// Represents a single instance of a contact between the user and another user who has reported
// their test data.
class ContactEvent {
    public ContactEvent(ReceiveEvent event, ReportToken report) {
        this.event = event;
        this.report = report;
    }

    public ReceiveEvent event;
    public ReportToken report;
}

// Represents contacts between the user and another user that are close enough in time to each
// other to be considered a single unit.
class Encounter {
    List<ContactEvent> events;
}

public class DefaultProximityEventResolver implements IProximityEventResolver {

    private static int CLOSE_CONTACT_DISTANCE_METER = 5;
    private static long LONG_CONTACT_MS = 15 * 60 * 1000;  // 15min

    private int getDistanceInMeter(ReceiveEvent event) {
        // TODO implement this reasonably.
        return 10;
    }

    private ProximityEvent computeProximityEvent(Encounter encounter) {
        ProximityEvent proximityEvent = new ProximityEvent();
        int minimalDistance = 100;
        for (ContactEvent contactEvent : encounter.events) {
            minimalDistance = Math.min(minimalDistance, getDistanceInMeter(contactEvent.event));
        }
        boolean closeDistance = minimalDistance <= CLOSE_CONTACT_DISTANCE_METER;
        if (closeDistance) {
            proximityEvent.setDistance(Distance.LOW_DIST);
        } else {
            proximityEvent.setDistance(Distance.MEDIUM_DIST);
        }

        ContactEvent firstEvent = encounter.events.get(0);
        ContactEvent lastEvent = encounter.events.get(encounter.events.size() - 1);
        long durationMs = lastEvent.event.getTimestampMs() - firstEvent.event.getTimestampMs();
        boolean longContact = durationMs < CLOSE_CONTACT_DISTANCE_METER;
        proximityEvent.setDurationMs(durationMs);
        proximityEvent.setEventType(firstEvent.report.getReportType());
        proximityEvent.setTestResult(firstEvent.report.getTestResult());
        return proximityEvent;
    }

    // Groups all ContactEvents with the same underlying token together in an Encounter.
    List<Encounter> groupContactEventsByToken(List<ContactEvent> contactEvents) {
        Map<Token, Encounter> encounterByToken = new HashMap<>();
        for (ContactEvent event : contactEvents) {
            Token token = event.event.getToken();
            Encounter encounter = encounterByToken.get(token);
            if (encounter == null) {
                encounter = new Encounter();
                encounterByToken.put(token, encounter);
            }
            encounter.events.add(event);
        }
        // Only return encounters with at least to events.
        List<Encounter> encounters = new LinkedList<>();
        for (Encounter encounter : encounterByToken.values()) {
            if (encounter.events.size() > 1) {
                encounters.add(encounter);
            }
        }
        return encounters;
    }

    // Creates a separate Encounter for each ContactEvent.
    List<Encounter> createEncounterPerEvent(List<ContactEvent> contactEvents) {
        List<Encounter> encounters = new LinkedList<>();
        for (ContactEvent contactEvent : contactEvents) {
            Encounter encounter = new Encounter();
            encounter.events.add(contactEvent);
            encounters.add(encounter);
        }
        return encounters;
    }

    // Tries to find ContactEvents belonging to the same physical encounter and groups them
    // accordingly.
    List<Encounter> groupContactEventsIntoEncounters(List<ContactEvent> contactEvents) {
        List<Encounter> encounters = new LinkedList<>();
        encounters.addAll(createEncounterPerEvent(contactEvents));
        encounters.addAll(groupContactEventsByToken(contactEvents));
        // TODO Add grouping by a) location, b) same signal strength in close time window?

        return encounters;
    }

    @Override
    public List<ProximityEvent> resolveProximityEvents(List<ReceiveEvent> receiveEvents,
                                                       List<ReportToken> reportTokens) {
        // Group all ReportTokens by the token so we can easily look them up.
        Map<Token, ReportToken> reportTokenMap = new HashMap<>();
        for (ReportToken reportToken : reportTokens) {
            reportTokenMap.put(reportToken.getToken(), reportToken);
        }

        // Keep only ReceiveEvents with a matching ReportToken and group them together.
        List<ContactEvent> contactEvents = new LinkedList<>();
        for (ReceiveEvent event : receiveEvents) {
            ReportToken reportToken = reportTokenMap.get(event.getToken());
            if (reportToken == null) {
                continue;
            }
            contactEvents.add(new ContactEvent(event, reportToken));
        }

        List<Encounter> encounters = groupContactEventsIntoEncounters(contactEvents);
        List<ProximityEvent> proximityEvents = new ArrayList<>();
        for (Encounter encounter : encounters) {
            proximityEvents.add(computeProximityEvent(encounter));
        }

        return proximityEvents;
    }
}
