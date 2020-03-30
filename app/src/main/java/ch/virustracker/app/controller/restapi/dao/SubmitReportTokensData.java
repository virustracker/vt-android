package ch.virustracker.app.controller.restapi.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Input for POST /vt-server-token
 */
public class SubmitReportTokensData {

    @SerializedName("report")
    @Expose
    private Report report;

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

}
