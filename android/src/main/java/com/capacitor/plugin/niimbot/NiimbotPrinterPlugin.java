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
import android.widget.Toast;

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

                    final String previewScript = "var toolbarButtons = document.querySelectorAll('.toolbar > button');" +
                        "console.log(toolbarButtons);" +
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

                    final String redirectScript = "var button = document.querySelector('.modal .btn-secondary');" +
                            "if (button) {" +
                            "    button.addEventListener('click', function() {" +
                            "        if(window.Capacitor.Plugins.SplashScreen) {" +
                            "           window.Capacitor.Plugins.SplashScreen.show({ autoHide: false });" +
                            "        }" +
                            "        window.WebAppInterface.onCloseButtonClick('" + finalToastMessage + "');" +
                            "        window.location.href = '" + redirectFullUri + "';" +
                            "    });" +
                            "}";
                    final String fullScript = (preview ? previewScript : "") + redirectScript;
                    // Inject JavaScript to set up the event listener after the page loads
                    webView.evaluateJavascript(fullScript, null);
                }
            });

            // Load the new URL
            webView.loadUrl(niimblueFullUri);
            // Add the JavaScript interface
            webView.addJavascriptInterface(new WebAppInterface(this), "WebAppInterface");

            // Add the FloatingActionButton
            addFloatingActionButton(webView, finalToastMessage, redirectFullUri);

            call.resolve();
        });

        // JSObject ret = new JSObject();
        // ret.put("value", implementation.echo(value));
        call.resolve();
    }

    private void addFloatingActionButton(WebView webView, String finalToastMessage, String redirectFullUri) {
        // Create a FloatingActionButton
        FloatingActionButton fab = new FloatingActionButton(this.getContext());
        fab.setImageResource(android.R.drawable.ic_menu_close_clear_cancel); // Set the close icon
        fab.setOnClickListener(view -> {
            // Handle FAB click
            //Toast.makeText(this.getContext(), "FAB Clicked", Toast.LENGTH_SHORT).show();
            // Call the onCloseButtonClick method from the JavaScript interface
            final String script = "window.WebAppInterface.onCloseButtonClick('" + finalToastMessage + "');" +
            "        window.location.href = '" + redirectFullUri + "';";
            webView.evaluateJavascript(script, null);
        });
    
        // Set layout parameters for the FAB
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.BOTTOM | Gravity.END;
        int margin = (int) (16 * getContext().getResources().getDisplayMetrics().density); // Convert 16dp to pixels
        params.setMargins(margin, margin, margin, margin);
        fab.setLayoutParams(params);
    
        // Ensure the parent layout is a FrameLayout
        ViewGroup parent = (ViewGroup) webView.getParent();
        if (parent instanceof FrameLayout) {
            // Add the FAB to the WebView's parent layout
            parent.addView(fab);
        } else {
            // Wrap the WebView in a FrameLayout
            FrameLayout frameLayout = new FrameLayout(this.getContext());
            parent.removeView(webView);
            frameLayout.addView(webView);
            frameLayout.addView(fab);
            parent.addView(frameLayout);
        }
    }
//    public void notifyDone() {
//        notifyListeners("done", null);
//    }
}