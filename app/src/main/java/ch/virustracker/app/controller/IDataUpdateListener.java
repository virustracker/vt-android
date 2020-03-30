package ch.virustracker.app.controller;

interface IDataUpdateListener {

    void newProximityEventsAvailable();
    void newTestReportStateAvailable();

}
