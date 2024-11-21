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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.FrameLayout;
import android.view.Gravity;
import android.view.ViewGroup;
import android.graphics.Color;
import android.content.res.ColorStateList;

@CapacitorPlugin(name = "NiimbotPrinter")
public class NiimbotPrinterPlugin extends Plugin {

    private FloatingActionButton _fab;

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
            final WebView webView = this.bridge.getWebView();

            final String currentUrl = webView.getUrl();

            final String baseUrl = currentUrl.substring(0, currentUrl.lastIndexOf("/")) + "/";
            final String redirectFullUri = (redirectUri == null || redirectUri.isEmpty()) ? currentUrl : baseUrl + redirectUri;
            String niimblueFullUri = baseUrl + niimblueUri;

            // Set a WebViewClient to listen for the page load completion
            webView.setWebViewClient(new WebViewClient() {
                private boolean isPageLoaded = false;

                @Override
                public void onPageFinished(WebView view, String url) {
                    if(isPageLoaded) {
                        // Remove the WebViewClient to prevent multiple calls after redirect to main app
                        webView.setWebViewClient(null);
                        return;
                    }

                    isPageLoaded = true;
                    super.onPageFinished(view, url);

                    final String previewScript = "window.Capacitor.Plugins.SplashScreen?.hide();" +
                            "var toolbarButtons = document.querySelectorAll('.toolbar > button');" +
                            "console.log('toolbarButtons', toolbarButtons);" +
                            "var previewButton = null;" +
                            "toolbarButtons.forEach(function(button) {" +
                            "    var icon = button.querySelector('span.mdi');" +
                            "    if (icon && icon.textContent === '') {" +
                            "        previewButton = button;" +
                            "    }" +
                            "});" +
                            "console.log('previewButton', previewButton);" +
                            "if (previewButton) {" +
                            "    previewButton.click();" +
                            "}";

                    final String redirectScript =
                            "var checkButtonInterval = setInterval(function() {" +
                                    "    var button = document.querySelector('.modal .btn-secondary');" +
                                    "    console.log('Checking for button:', button);" +
                                    "    if (button) {" +
                                    "        clearInterval(checkButtonInterval);" +
                                    "        console.log('Button found:', button);" +
                                    "        button.addEventListener('click', function() {" +
                                    "            console.log('Calling onCloseButtonClick');" +
                                    "            window.WebAppInterface.onCloseButtonClick('" + finalToastMessage + "', '" + redirectFullUri + "');" +
                                    "        });" +
                                    "    }" +
                                    "}, 100);"; // Check every 100 milliseconds
                    final String fullScript = (preview ? previewScript : "") + redirectScript;
                    // Inject JavaScript to set up the event listener after the page loads
                    webView.evaluateJavascript(fullScript, null);

                    // Add the FloatingActionButton
                    addFloatingActionButton(webView, finalToastMessage, redirectFullUri);
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


    public void removeFloatingActionButton() {
        if (_fab != null) {
            ViewGroup parent = (ViewGroup) _fab.getParent();
            if (parent != null) {
                parent.removeView(_fab);
            }
            _fab = null;
        }
    }

//    public void notifyDone() {
//        notifyListeners("done", null);
//    }
}