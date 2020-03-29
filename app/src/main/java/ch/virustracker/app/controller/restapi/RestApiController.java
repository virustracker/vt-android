package ch.virustracker.app.controller.restapi;

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

    public void fetchReportTokens(final IReportTokenListener reportTokenlistener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ReportTokenResponse reportTokenResponse = service.getReportTokens().execute().body();
                    VtApp.getController().onNewReportTokens(reportTokenResponse.tokens);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
