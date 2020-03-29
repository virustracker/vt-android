package ch.virustracker.app.model.database.servertoken;

import com.google.gson.annotations.SerializedName;

import ch.virustracker.app.model.Token;

// Represents a token obtained from the server that corresponds to a test result that was
// uploaded to the server.
public class ReportToken extends Token {

    public static final String SELF_REPORTED = "SELF_REPORTED";
    public static final String VERIFIED = "VERIFIED";

    public ReportToken(String tokenValue, String reportType, String testResult) {
        super(tokenValue);
        this.reportType = reportType;
        this.testResult = testResult;
    }

    @SerializedName("value")
    private String tokenValue;

    @SerializedName("type")
    private String reportType;

    @SerializedName("testResult")
    private String testResult;

    public String getReportType() {
        return reportType;
    }

    public String getTestResult() {
        return testResult;
    }
}
