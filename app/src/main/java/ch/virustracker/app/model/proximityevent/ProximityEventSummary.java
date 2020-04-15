package ch.virustracker.app.model.proximityevent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

public class ProximityEventSummary {

    private int numProximityEvents;
    private int numHighRiskEvents;
    private int numLowRiskEvents;
    private long timeAtBeginningOfDay;

    public ProximityEventSummary(int numProximityEvents, int numHighRiskEvents, int numLowRiskEvents, long timeAtBeginningOfDay) {
        this.numProximityEvents = numProximityEvents;
        this.numHighRiskEvents = numHighRiskEvents;
        this.numLowRiskEvents = numLowRiskEvents;
        this.timeAtBeginningOfDay = timeAtBeginningOfDay;
    }

    public static List<ProximityEventSummary> getProximityEventsByDay(List<ProximityEvent> proximityEvents) {
        List<ProximityEventSummary> summaryList = new LinkedList<>();
        long timeAtBeginningOfDay = beginningOfDay(System.currentTimeMillis());
        summaryList.add(new ProximityEventSummary(0,0,0,timeAtBeginningOfDay));
        for (ProximityEvent proximityEvent : proximityEvents) {
            timeAtBeginningOfDay = beginningOfDay(proximityEvent.getTimestampMs());
            ProximityEventSummary existingEvent = null;
            for (ProximityEventSummary sum : summaryList) {
                if (sum.getTimeAtBeginningOfDay() == timeAtBeginningOfDay) existingEvent = sum;
            }
            if (existingEvent == null) {
                existingEvent = new ProximityEventSummary(1, proximityEvent.getRisk() == ProximityEvent.RISK_HIGH ? 1 : 0, proximityEvent.getRisk() == ProximityEvent.RISK_LOW ? 1 : 0, timeAtBeginningOfDay);
                summaryList.add(existingEvent);
            } else {
                if (proximityEvent.getRisk() == ProximityEvent.RISK_HIGH) existingEvent.setNumHighRiskEvents(existingEvent.getNumHighRiskEvents()+1);
                if (proximityEvent.getRisk() == ProximityEvent.RISK_LOW) existingEvent.setNumLowRiskEvents(existingEvent.getNumLowRiskEvents()+1);
                existingEvent.setNumProximityEvents(existingEvent.getNumProximityEvents()+1);
            }
        }
        summaryList.sort((o1, o2) -> Long.compare(o2.getTimeAtBeginningOfDay(), o1.getTimeAtBeginningOfDay()));
        return summaryList;
    }

    public static long beginningOfDay(long timestampMs) {
        Instant instant = Instant.ofEpochMilli(timestampMs);
        ZonedDateTime zdt = ZonedDateTime.ofInstant( instant , ZoneId.systemDefault() );
        LocalDateTime start = zdt.toLocalDate().atStartOfDay();
        return start.toEpochSecond(ZoneId.systemDefault().getRules().getOffset(start))*1000;
    }

    public int getNumProximityEvents() {
        return numProximityEvents;
    }

    public void setNumProximityEvents(int numProximityEvents) {
        this.numProximityEvents = numProximityEvents;
    }

    public int getNumHighRiskEvents() {
        return numHighRiskEvents;
    }

    public void setNumHighRiskEvents(int numHighRiskEvents) {
        this.numHighRiskEvents = numHighRiskEvents;
    }

    public int getNumLowRiskEvents() {
        return numLowRiskEvents;
    }

    public void setNumLowRiskEvents(int numLowRiskEvents) {
        this.numLowRiskEvents = numLowRiskEvents;
    }

    public long getTimeAtBeginningOfDay() {
        return timeAtBeginningOfDay;
    }

    public void setTimeAtBeginningOfDay(long timeAtBeginningOfDay) {
        this.timeAtBeginningOfDay = timeAtBeginningOfDay;
    }
}
