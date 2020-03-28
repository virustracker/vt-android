package ch.virustracker.app.view;

import android.webkit.JavascriptInterface;

public class WebAppInterface {

    public WebAppInterface(MainActivity mainActivity) {
    }

    @JavascriptInterface
    public String toString() { return "injectedObject"; }
}
