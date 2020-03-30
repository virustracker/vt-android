package ch.virustracker.app.controller.restapi;

public interface IDataUpdateListener {

    void newProximityEventsAvailable();
    void newTestReportStateAvailable();

}
