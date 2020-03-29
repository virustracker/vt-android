package ch.virustracker.app.view;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.HttpAuthHandler;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import ch.virustracker.app.R;
import ch.virustracker.app.controller.VtApp;
import ch.virustracker.app.model.Model;

public class MainActivity extends AppCompatActivity {

    private ValueCallback<Uri[]> mFilePathCallback;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.model = VtApp.getModel();
        VtApp.getController().startTracking();
        initializeView();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        VtApp.getController().fetchNewInfections();;
    }

    private void initializeView() {
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setVisibility(View.GONE);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android"); // TODO: Should be webappinterface
        webView.loadUrl("https://polbyte.atthouse.pl/public/virus/");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.d("ChromeWebView", message);
                return false;
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                Log.d("ChromeWebView", "File choose");
                return true;
            }
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("ChromeWebView", consoleMessage.message());
                return false;
            }
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                Log.d("ChromeWebView", request.toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.getResources());
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (errorCode == 401) {
                    Log.d("WebView", "Error");
                }
            }
            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                handler.proceed("", "");
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView v, String url) {
                Log.d("WebView", url);
                return false;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
                view.setAlpha(0f);
                view.setVisibility(View.VISIBLE);
                view.animate()
                        .alpha(1.0f)
                        .setDuration(800)
                        .setListener(null);
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
    }
}