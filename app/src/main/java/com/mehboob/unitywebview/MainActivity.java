package com.mehboob.unitywebview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccess(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView.getSettings().setSupportMultipleWindows(false);
        webView.getSettings().setSupportZoom(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("WebViewConsole", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return true;
            }
        });

        startWebView("https://2bet888.bet/home/");

        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        injectLoginTrigger(webView);

    }

    private void injectJavaScript(WebView view) {

//        view.evaluateJavascript(
//                "javascript:document.getElementById('ant-btn.ant-btn-primary').addEventListener('click', function() {" +
//                        "   window.Android.showToast('login');" +
//                        "});", null);
    }


    private void injectLoginTrigger(WebView webView){

//        webView.evaluateJavascript(
//                "javascript:(function() { " +
//                        "   console.log('JavaScript executed');" +
//                        "   var loginButton = document.querySelector('ant-btn ant-btn-primary');" +
//                        "   loginButton.addEventListener('click', function() {" +
//                        "       console.log('Button clicked');" +
//                        "       window.Android.showToast('login');" +
//                        "   });" +
//                        "})()", null);
//
//
//        webView.loadUrl(
//                "javascript:(function() { " +
//                        "var element = document.getElementByClass('ant-btn ant-btn-primary');"
//                        +"element.parentNode.removeChild(element);" +
//                        "})()");


        webView.loadUrl(
                "javascript:(function() { " +
                        "   var element = document.querySelectorAll('.ant-btn.ant-btn-primary');" +
                        "   if (element) {" +  // Check if the element exists
                        "      console.log('Button found JUUUUU'); " +  // Change the background color to green
                        "   } else {" +
                        "       console.log('Button not found');" +
                        "   }" +
                        "})()");
    }
    private void startWebView(String myURL) {


        webView.setWebViewClient(new Client());
        webView.loadUrl(myURL);

    }

    public class Client extends WebViewClient {
        ProgressDialog progressDialog;
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // ignore ssl error
            if (handler != null){
                handler.proceed();
            } else {
                super.onReceivedSslError(view, null, error);

                Toast.makeText(MainActivity.this, ""+error.getUrl(), Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        }


        //If you will not use this method url links are open in new brower not in webview
   @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!url.contains("https://2bet888.bet/home/")) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            } else {
                view.loadUrl(url);
                return true;
            }
        }


        //Show loader on url load
        @Override

        public void onLoadResource(WebView view, String url) {
            // Then show progress  Dialog
            if (progressDialog == null && url.contains("https://2bet888.bet/home/")) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("loading ...");
                progressDialog.show();


            }
        }

        // Called when all page resources loaded
        @Override

        public void onPageFinished(WebView view, String url) {
            try {
                // Close progressDialog
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}

