package ch.virustracker.app.model.proximityevent;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

    @NonNull
    @Override
    public String toString() {
        return "ContactEvent {\n  event: " + event + "\n  report: " + report + "\n}";
    }
}

// Represents contacts between the user and another user that are close enough in time to each
// other to be considered a single unit.
class Encounter {
    LinkedList<ContactEvent> events = new LinkedList<>();

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Encounter { events: [\n");
        for (ContactEvent event : events) {
            builder.append("  ").append(event).append(",\n");
        }
        builder.delete(builder.length() - 2, builder.length());
        builder.append("\n] }");
        return builder.toString();
    }
}

public class DefaultProximityEventResolver implements IProximityEventResolver {

    private static int CLOSE_CONTACT_DISTANCE_METER = 6;

    private int getDistanceInMeter(ReceiveEvent event) {
        return (int) event.getDistanceMeter();
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
        proximityEvent.setDurationMs(durationMs);
        proximityEvent.setReportType(firstEvent.report.getReportType());
        proximityEvent.setTestResult(firstEvent.report.getTestResult());
        return proximityEvent;
    }

    // Returns whether `newContactEvent` likely belongs to the physical encounter represented by
    // `encounter`.
    private boolean isSameEncounter(Encounter encounter, ContactEvent newContactEvent) {
        ContactEvent lastContactEvent = encounter.events.getLast();
        ReceiveEvent lastEncounterEvent = lastContactEvent.event;
        ReceiveEvent newEvent = newContactEvent.event;
        if (lastContactEvent.report.getTestResult() != newContactEvent.report.getTestResult() ||
                lastContactEvent.report.getReportType() != newContactEvent.report.getReportType()) {
            // Either the test result or the type of the new report is different from the
            // ContactEvents in the encounter. Hence the ContactEvent likely identifies a new
            // physical encounter, likely with a different user.
            return false;
        }
        long timeDifference =
                Math.abs(lastEncounterEvent.getTimestampMs() - newEvent.getTimestampMs());
        return timeDifference < Model.ROTATE_INTERVAL_MS * 1.1;
    }

    // Groups ContactEvents that are timewise close and therefore likely belonging to the same
    // physical encounter.
    private List<Encounter> groupContactEventsByTimestamps(List<ContactEvent> contactEvents) {
        List<ContactEvent> sortedContactEvents = new ArrayList<>(contactEvents.size());
        sortedContactEvents.addAll(contactEvents);
        // Sort events by ascending timestamps.
        Collections.sort(sortedContactEvents,
                (e1, e2) -> Long.compare(e1.event.getTimestampMs(), e2.event.getTimestampMs()));

        LinkedList<Encounter> encounters = new LinkedList<>();
        Encounter currentEncounter = null;
        for (ContactEvent contactEvent : contactEvents) {
            if (currentEncounter == null || !isSameEncounter(currentEncounter, contactEvent)) {
                currentEncounter = new Encounter();
                encounters.add(currentEncounter);
            }
            currentEncounter.events.add(contactEvent);
        }
        return encounters;
    }

    // Tries to find ContactEvents belonging to the same physical encounter and groups them
    // accordingly.
    private List<Encounter> groupContactEventsIntoEncounters(List<ContactEvent> contactEvents) {
        List<Encounter> encounters = new LinkedList<>();
        encounters.addAll(groupContactEventsByTimestamps(contactEvents));
        // TODO Potentially add other ways to group ContactEvents into encounters.
        return encounters;
    }

    @Override
    public List<ProximityEvent> resolveProximityEvents(List<ReceiveEvent> receiveEvents,
                                                       List<ReportToken> reportTokens) {
        // Group all ReportTokens by the token so we can easily look them up.
        Map<Token, ReportToken> positiveTokenMap = new HashMap<>();
        for (ReportToken reportToken : reportTokens) {
            if (reportToken.getTestResult().equals(ReportToken.POSITIVE)) {
                positiveTokenMap.put(reportToken.getToken(), reportToken);
            }
        }

        // Keep only ReceiveEvents with a matching ReportToken and group them together.
        List<ContactEvent> contactEvents = new LinkedList<>();
        for (ReceiveEvent event : receiveEvents) {
            ReportToken reportToken = positiveTokenMap.get(event.getToken());
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
