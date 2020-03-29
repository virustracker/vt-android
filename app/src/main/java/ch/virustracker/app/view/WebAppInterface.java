package ch.virustracker.app.view;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class WebAppInterface {
    private final WebView webView;
    Context mContext;
    // Instantiate the interface and set the context
    WebAppInterface(Context c, WebView webView) {
        mContext = c;
        this.webView = webView;
    }
    @JavascriptInterface
    public void log(String message) {
        Log.v("WebAppInterface", message);
    }
    @JavascriptInterface
    public void setSettings(String settingMapJson) {
        // store in db, no callback needed
    }
    @JavascriptInterface
    public void getSettings() {
        String settingListJson = "{\"should_share_data\": 1}";
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:nw.callbackUserSettings('" + settingListJson + "')");
            }
        });
    }
    @JavascriptInterface
    public void getProximityEventList(long from, long to) {
        String listJson = "[{\"infection_state\":0,\"timestamp\":0,\"is_confidential\":false,\"distance_type\":0,\"duration\":0},{\"infection_state\":1,\"timestamp\":10,\"is_confidential\":true,\"distance_type\":1,\"duration\":3},{\"infection_state\":0,\"timestamp\":20,\"is_confidential\":false,\"distance_type\":0,\"duration\":6},{\"infection_state\":1,\"timestamp\":30,\"is_confidential\":true,\"distance_type\":1,\"duration\":9},{\"infection_state\":0,\"timestamp\":40,\"is_confidential\":false,\"distance_type\":0,\"duration\":12}]";
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:nw.callbackProximityEventList('"+listJson+"')");
            }
        });
    }
    @JavascriptInterface
    public void sendInfectionRequest(String days) {
        // use that data to store / make http request and invoke callback to inform was it successful
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:nw.callbackInfectionRequest(1)");
            }
        });
    }
}
