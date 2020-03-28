package ch.virustracker.app.model.proximityevent;

import ch.virustracker.app.model.InfectionState;

public class ProximityEvent {

    public enum Distance { LOW_DIST, MEDIUM_DIST, HIGH_DIST}

    private Distance distance;
    private long timestampMs;
    private long durationMs;
    private InfectionState infectionState;
    private boolean confidentialEncounter;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    public void setTimestampMs(long timestampMs) {
        this.timestampMs = timestampMs;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public InfectionState getInfectionState() {
        return infectionState;
    }

    public void setInfectionState(InfectionState infectionState) {
        this.infectionState = infectionState;
    }

    public boolean isConfidentialEncounter() {
        return confidentialEncounter;
    }

    public void setConfidentialEncounter(boolean confidentialEncounter) {
        this.confidentialEncounter = confidentialEncounter;
    }
}
