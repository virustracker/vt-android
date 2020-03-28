package ch.virustracker.app.controller.restapi;

import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.ssl.SSLContexts;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLContext;

import ch.virustracker.app.R;
import ch.virustracker.app.controller.VtApp;
import ch.virustracker.app.model.database.infectedtoken.InfectedToken;
import kong.unirest.GenericType;
import kong.unirest.GetRequest;
import kong.unirest.HttpRequest;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.apache.ApacheClient;

public class RestApiController {

    public void fetchInfectedTokens(final IInfectedTokenListener infectedTokenlistener) {
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpRequest response = sendGetRequest(VtApp.string(R.string.api_base_url), VtApp.string(R.string.api_endpoint_infected_tokens), null);
                    Unirest.config().verifySsl(false);
                    List<InfectedToken> infectedTokenList = ( List<InfectedToken>) response.asObject(new GenericType<List<ApiInfectedToken>>(){}).getBody();
                    //String infectedTokenList = Unirest.get("http://httpbin.org").queryString("fruit", "apple").queryString("droid", "R2D2").asString().getBody();
                    Log.v("Hallo", infectedTokenList.toString());
                    if(infectedTokenlistener != null) infectedTokenlistener.newInfectedToekensAvailable();
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

    private HttpRequest sendGetRequest(String baseUrl, String endPoint, HashMap<String, String> parameters) throws UnirestException {
        GetRequest req = Unirest.get(baseUrl + endPoint);
        if (parameters != null) {
            for (String key : parameters.keySet()) req.queryString(key, parameters.get(key));
        }
        return req;
    }
}
