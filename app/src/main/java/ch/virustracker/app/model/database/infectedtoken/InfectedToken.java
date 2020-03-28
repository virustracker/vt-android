package ch.virustracker.app.model.database.infectedtoken;

import com.google.gson.annotations.SerializedName;

import ch.virustracker.app.model.Token;

public class InfectedToken extends Token {

    public static final String SELF_REPORTED = "SELF_REPORTED";
    public static final String VERIFIED = "VERIFIED";

    @SerializedName("value")
    private String tokenValue;

    @SerializedName("type")
    private String reportType;

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    @Override
    public String getTokenValue() {
        return tokenValue;
    }
}
