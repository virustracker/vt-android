package ch.virustracker.app.controller;

import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.HashMap;

import ch.virustracker.app.R;

public class RestApiController {

    public void fetchInfectedTokens(long fromTimestampMs, long toTimestampMs, final IInfectedTokenListener infectedTokenlistener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> parameters = new HashMap<>();
                    String response = sendGetRequest(VtApp.string(R.string.api_base_url), VtApp.string(R.string.api_base_url), parameters);
                    if(infectedTokenlistener != null) infectedTokenlistener.newInfectedToekensAvailable();
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String sendGetRequest(String string, String string1, HashMap<String, String> parameters) throws UnirestException {
        return "";
    }
}
