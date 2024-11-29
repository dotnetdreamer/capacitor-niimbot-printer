package com.capacitor.plugin.niimbot;

import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.FrameLayout;
import android.view.Gravity;
import android.view.ViewGroup;
import android.graphics.Color;
import android.content.res.ColorStateList;

import java.net.MalformedURLException;
import java.net.URL;

@CapacitorPlugin(name = "NiimbotPrinter")
public class NiimbotPrinterPlugin extends Plugin {

    private FloatingActionButton _fab;

    @PluginMethod
    public void print(PluginCall call) {
        final Boolean debug = call.getBoolean("debug", true);
        final String niimblueUri = "assets/niimblue/index.html";
        final JSObject toast = call.getObject("toast", new JSObject());
        final String toastMessage = toast.getString("message", "Redirecting...");
        final Boolean toastEnabled = toast.getBoolean("enabled", true);
        final String finalToastMessage = toastEnabled ? toastMessage : "";
        final String redirectUri = call.getString("redirectUri");
        final Boolean preview = call.getBoolean("preview", false);
        final boolean displayFab = call.getBoolean("displayFab", true);

        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> {
            final WebView webView = this.bridge.getWebView();

            final String currentUrl = webView.getUrl();

            String baseUrl;
            try {
              URL url = new URL(currentUrl);
              baseUrl = url.getProtocol() + "://" + url.getHost() + (url.getPort() != -1 ? ":" + url.getPort() : "") + "/";
            } catch (MalformedURLException e) {
              call.reject("Invalid URL: " + currentUrl);
              return;
            }
            final String redirectFullUri = (redirectUri == null || redirectUri.isEmpty()) ? currentUrl : baseUrl + redirectUri;
            String niimblueFullUri = baseUrl + niimblueUri;

            final String loadingScript = "window.location.href = '" + niimblueFullUri + "';";
            webView.evaluateJavascript(loadingScript, value -> {
                //Check if main container is loaded in the page
                final String checkPageLoadedScript = "var containerInterval = setInterval(function() {" +
                        "    var container = document.querySelector('.container');" +
                        (debug ? "console.log('Checking for container: .container', container);" : "") +
                        "    if (container) {" +
                        "        clearInterval(containerInterval);" +
                        (debug ? "console.log('Container found:', container);" : "") +
                        "    }" +
                        "}, 100);"; // Check every 100 milliseconds
                new Handler().postDelayed(() -> {
//                    webView.evaluateJavascript(checkPageLoadedScript, null);

                 final String backButtonScript =
                 (debug ? "console.log('Adding scripts');" : "") +
                   "  window.Capacitor.Plugins.App.addListener('backButton', (ev) => {" +
                   (debug ? "console.log('backButton was called!');" : "") +
                   "      window.WebAppInterface.onCloseButtonClick('" + finalToastMessage + "', '" + redirectFullUri + "');" +
                   "  });";

                 final String previewScript = "window.Capacitor.Plugins.SplashScreen?.hide();" +
                         "var toolbarButtons = document.querySelectorAll('.toolbar > button');" +
                         (debug ? "console.log('toolbarButtons', toolbarButtons);" : "") +
                         "var previewButton = null;" +
                         "toolbarButtons.forEach(function(button) {" +
                         "    var icon = button.querySelector('span.mdi');" +
                         "    if (icon && icon.textContent === '') {" +
                         "        previewButton = button;" +
                         "    }" +
                         "});" +
                         (debug ? "console.log('previewButton', previewButton);" : "") +
                         "if (previewButton) {" +
                         "    previewButton.click();" +
                         "}";

                 final String redirectScript =
                         "var checkButtonInterval = setInterval(function() {" +
                                 "    var button = document.querySelector('.modal .btn-secondary');" +
                                 (debug ? "console.log('Checking for close button: .modal .btn-secondary', button);" : "") +
                                 "    if (button) {" +
                                 "        clearInterval(checkButtonInterval);" +
                                 (debug ? "console.log('Close Button found:', button);" : "") +
                                 "        button.addEventListener('click', function() {" +
                                 (debug ? "console.log('Calling onCloseButtonClick');" : "") +
                                 "            window.WebAppInterface.onCloseButtonClick('" + finalToastMessage + "', '" + redirectFullUri + "');" +
                                 "        });" +
                                 "    }" +
                                 "}, 100);"; // Check every 100 milliseconds
                     final String fullPreviewScript = preview ? previewScript + redirectScript : "";
                     final String fullScript = backButtonScript + fullPreviewScript;
                     // Inject JavaScript to set up the event listener after the page loads
                     webView.evaluateJavascript(fullScript, null);

                     //Add the FloatingActionButton
                    addFloatingActionButton(webView, finalToastMessage, redirectFullUri);
                }, 1000);
            });

            // Add the JavaScript interface
            webView.addJavascriptInterface(new WebAppInterface(this), "WebAppInterface");

            call.resolve();
        });

        // JSObject ret = new JSObject();
        // ret.put("value", implementation.echo(value));
        call.resolve();
    }

    private void addFloatingActionButton(WebView webView, String finalToastMessage, String redirectFullUri) {
        // Create a FloatingActionButton
        _fab = new FloatingActionButton(this.getContext());
        _fab.setImageResource(android.R.drawable.ic_menu_close_clear_cancel); // Set the close icon

        // Set the background color of the FAB using a hex value
        _fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000"))); // Change the hex value as needed

        _fab.setOnClickListener(view -> {
            // Handle FAB click
            final String script =
                    "window.WebAppInterface.onCloseButtonClick('" + finalToastMessage + "', '" + redirectFullUri + "');";
            this.bridge.getWebView().evaluateJavascript(script, null);
        });

        // Set layout parameters for the FAB
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.BOTTOM | Gravity.END;
        int margin = (int) (16 * getContext().getResources().getDisplayMetrics().density); // Convert 16dp to pixels
        params.setMargins(margin, margin, margin, margin);
        _fab.setLayoutParams(params);

        // Ensure the parent layout is a FrameLayout
        ViewGroup parent = (ViewGroup) webView.getParent();
        if (parent instanceof FrameLayout) {
            // Add the FAB to the WebView's parent layout
            parent.addView(_fab);
        } else {
            // Wrap the WebView in a FrameLayout
            FrameLayout frameLayout = new FrameLayout(this.getContext());
            parent.removeView(webView);
            frameLayout.addView(webView);
            frameLayout.addView(_fab);
            parent.addView(frameLayout);
        }
    }

    public void cleanUp() {
        _removeFloatingActionButton();

        final String script =
                "window.localStorage.removeItem('automation')" +
                        "";
        getBridge().getWebView().evaluateJavascript(script, null);

    }

    private void _removeFloatingActionButton() {
        if (_fab != null) {
            ViewGroup parent = (ViewGroup) _fab.getParent();
            if (parent != null) {
                parent.removeView(_fab);
            }
            _fab = null;
        }
    }

    private void _notifyDone() {
        notifyListeners("done", null);
    }
}