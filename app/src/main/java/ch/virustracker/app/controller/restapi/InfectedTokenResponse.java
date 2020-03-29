package ch.virustracker.app.controller.restapi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ch.virustracker.app.model.database.servertoken.ReportToken;

public class InfectedTokenResponse {

    @SerializedName("tokens")
    public List<ReportToken> tokens;

}