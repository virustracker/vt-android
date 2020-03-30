package ch.virustracker.app.controller.restapi;


import ch.virustracker.app.controller.restapi.dao.SubmitReportTokensData;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface RestApiService {

    @GET("vt-server-token")
    Call<ReportTokenResponse> getReportTokens();

    @POST("vt-server-token")
    Call<Void> submitReportTokens(@Body SubmitReportTokensData submitReportTokenData);
}
