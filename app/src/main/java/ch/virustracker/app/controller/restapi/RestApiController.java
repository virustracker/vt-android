package ch.virustracker.app.controller.restapi;

import android.util.Log;

import com.google.gson.Gson;

import ch.virustracker.app.R;
import ch.virustracker.app.controller.VtApp;
import ch.virustracker.app.controller.restapi.dao.SubmitReportTokensData;
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

    /**
     * Submits token as part of a report. Tokens in the report must have a preimage.
     * @param submitReportTokenData
     */
    public void submitReportTokens(final SubmitReportTokensData submitReportTokenData) {

        new Thread(() -> {
            try {
//                Log.i("API", new Gson().toJson(submitReportTokenData));
                int code = service.submitReportTokens(submitReportTokenData).execute().code();
                VtApp.getController().onSubmittedReportTokens(code);
            } catch (Exception e){
//                Log.e("API", "ERROR: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }).start();

    }

}
