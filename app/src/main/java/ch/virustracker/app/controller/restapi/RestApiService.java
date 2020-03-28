package ch.virustracker.app.controller.restapi;


import retrofit.Call;
import retrofit.http.GET;

public interface RestApiService {

    @GET("vt-server-token")
    Call<InfectedTokenResponse> getInfectedTokens();
}
