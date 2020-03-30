package ch.virustracker.app.controller.restapi.getreports;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ch.virustracker.app.controller.restapi.getreports.ApiReportToken;
import ch.virustracker.app.model.ReportToken;

public class ReportTokenResponse {
    @SerializedName("tokens")
    public List<ApiReportToken> tokens;

    public List<ReportToken> getReportTokens() {
        List<ReportToken> rl = new ArrayList<>(tokens.size());
        for (ApiReportToken token : tokens) {
            rl.add(token.getReportToken());
        }
        return  rl;
    }
}
