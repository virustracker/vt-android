package ch.virustracker.app.controller.restapi.submitreport;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Input for POST /vt-server-token
 */
public class SubmitReportTokensData {

    @SerializedName("report")
    @Expose
    private SubmitReport report;

    public SubmitReport getReport() {
        return report;
    }

    public void setReport(SubmitReport report) {
        this.report = report;
    }

}
