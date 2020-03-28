package ch.virustracker.app.controller.restapi;

public class ApiInfectedToken {

    public enum ReportType {SELF_REPORTED, VERIFIED}

    public ReportType type;
    public String value;

}
