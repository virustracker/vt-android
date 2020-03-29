package ch.virustracker.app.model;

import com.google.gson.annotations.SerializedName;

import ch.virustracker.app.model.Token;

// Represents a token obtained from the server that corresponds to a test result that was
// uploaded to the server.
public class ReportToken {

    public static final String SELF_REPORTED = "SELF_REPORTED";
    public static final String VERIFIED = "VERIFIED";

    public static final String POSITIVE = "POSITIVE";
    public static final String NEGATIVE = "NEGATIVE";
    public static final String UNKNOWN = "UNKNOWN";

    public ReportToken(String tokenValue, String testResult, String reportType) {
        this.token = new Token(tokenValue);
        this.testResult = testResult;
        this.reportType = reportType;
    }

    private Token token;

    private String reportType;

    private String testResult;

    public Token getToken() {
        return token;
    }

    public String getReportType() {
        return reportType;
    }

    public String getTestResult() {
        return testResult;
    }
}
