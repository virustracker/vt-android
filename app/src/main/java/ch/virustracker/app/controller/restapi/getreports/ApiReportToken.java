package ch.virustracker.app.controller.restapi.getreports;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ch.virustracker.app.model.ReportToken;

public class ApiReportToken {

    @SerializedName("type")
    public String type;

    @SerializedName("value")
    public String value;

    public ReportToken getReportToken() {
        return new ReportToken(value, ReportToken.POSITIVE, type);
    }
}
