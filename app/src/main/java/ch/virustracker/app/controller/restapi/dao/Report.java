package ch.virustracker.app.controller.restapi.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Report sent in the POST /vt-server-token
 */
public class Report {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("tokens")
    @Expose
    private List<Token> tokens = new ArrayList<>();

    @SerializedName("attestation")
    @Expose
    private String attestation;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public String getAttestation() {
        return attestation;
    }

    public void setAttestation(String attestation) {
        this.attestation = attestation;
    }
}
