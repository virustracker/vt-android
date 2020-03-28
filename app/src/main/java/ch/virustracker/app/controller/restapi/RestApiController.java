package ch.virustracker.app.controller.restapi;

import android.util.Log;

import java.io.IOException;

import ch.virustracker.app.R;
import ch.virustracker.app.controller.VtApp;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class RestApiController {

    private final RestApiService service;
    Retrofit retrofit;

    public RestApiController() {
        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(VtApp.string(R.string.api_base_url)).build();
        service = retrofit.create(RestApiService.class);
    }

    public void fetchInfectedTokens(final IInfectedTokenListener infectedTokenlistener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InfectedTokenResponse infectedTokenList = service.getInfectedTokens().execute().body();
                    VtApp.getController().onNewInfectedTokens(infectedTokenList.tokens);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
