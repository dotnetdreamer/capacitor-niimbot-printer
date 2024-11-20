package com.capacitor.plugin.niimbot;

import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "NiimbotPrinter")
public class NiimbotPrinterPlugin extends Plugin {

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        /*JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));*/
        call.resolve();
    }

    @PluginMethod
    public void print(PluginCall call) {
        final String niimblueUri = call.getString("niimblueUri");
        if (niimblueUri == null || niimblueUri.isEmpty()) {
            call.reject("niimblueUri is null or empty");
            return;
        }

        final JSObject toast = call.getObject("toast", new JSObject());
        final String toastMessage = toast.getString("message", "Redirecting...");
        final Boolean toastEnabled = toast.getBoolean("enabled", true);
        final String finalToastMessage = toastEnabled ? toastMessage : "";
        final String redirectUri = call.getString("redirectUri");
        final Boolean preview = call.getBoolean("preview", false);

        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> {
            WebView webView = this.bridge.getWebView();
            final String currentUrl = webView.getUrl();

            final String baseUrl = currentUrl.substring(0, currentUrl.lastIndexOf("/")) + "/";
            final String redirectFullUri = (redirectUri == null || redirectUri.isEmpty()) ? currentUrl : baseUrl + redirectUri;
            String niimblueFullUri = baseUrl + niimblueUri;

            // Set a WebViewClient to listen for the page load completion
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    // Inject JavaScript to set up the event listener after the page loads
                    webView.evaluateJavascript(
                    "var toolbarButtons = document.querySelectorAll('.toolbar > button');" +
                    "console.log(toolbarButtons);" +
                    "var previewButton = null;" +
                    "toolbarButtons.forEach(function(button) {" +
                    "    var icon = button.querySelector('span.mdi');" +
                    "    if (icon && icon.textContent === '') {" +
                    "        previewButton = button;" +
                    "    }" +
                    "});" +
                    "console.log('previewButton', previewButton);" +
                    "if (previewButton && " + preview + ") {" +
                    "    previewButton.click();" +
                    "}" +
                    "var button = document.querySelector('.modal .btn-secondary');" +
                    "if (button) {" +
                    "    button.addEventListener('click', function() {" +
                    "        if(window.Capacitor.Plugins.SplashScreen) {" +
                    "           window.Capacitor.Plugins.SplashScreen.show({ autoHide: false });" +
                    "        }" +
                    "        window.WebAppInterface.onCloseButtonClick('" + finalToastMessage + "');" +
                    "        window.location.href = '" + redirectFullUri + "';" +
                    "    });" +
                    "}",
                            null
                    );
                }
            });

            // Load the new URL
            webView.loadUrl(niimblueFullUri);
            // Add the JavaScript interface
            webView.addJavascriptInterface(new WebAppInterface(this), "WebAppInterface");

            call.resolve();
        });

        // JSObject ret = new JSObject();
        // ret.put("value", implementation.echo(value));
        call.resolve();
    }

//    public void notifyDone() {
//        notifyListeners("done", null);
//    }
}