package ch.virustracker.app.view;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import ch.virustracker.app.controller.VtApp;

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
        String listJson = "{\"tokens\":[{\"timestamp\": 1585094400000, \"count\": 645}, {\"timestamp\": 1585180800000, \"count\": 1215}, {\"timestamp\": 1585267200000, \"count\": 2617}, {\"timestamp\": 1585353600000, \"count\": 312}, {\"timestamp\": 1585440000000, \"count\": 3716}], \"events\":[{\"infection_state\":0,\"timestamp\":1585440000000,\"is_confidential\":false,\"distance_type\":1,\"duration\":1200000},{\"infection_state\":1,\"timestamp\":1585267200000,\"is_confidential\":true,\"distance_type\":3,\"duration\":200000}]}";
        new Thread(() -> {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:nw.callbackProximityEventList('"+listJson+"')");
                }
            });
        }).start();
    }
    @JavascriptInterface
    public void sendInfectionRequest(String days) {

        VtApp.getController().selfReportPositiveAndSubmitTokensWithLocation(Integer.valueOf(days));

        // use that data to store / make http request and invoke callback to inform was it successful
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:nw.callbackInfectionRequest(1)");
            }
        });
    }
}
