package com.capacitor.plugin.niimbot;

import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.getcapacitor.Plugin;

public class WebAppInterface {
    private Plugin plugin;

    public WebAppInterface(Plugin plugin) {
        this.plugin = plugin;
    }

    @JavascriptInterface
    public void onCloseButtonClick(final String toastMessage, final String redirectFullUri) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> {
            final String script =
                    "if(window.Capacitor.Plugins.SplashScreen) {" +
                        "console.log('Showing SplashScreen');" +
                        "window.Capacitor.Plugins.SplashScreen.show({ autoHide: false });" +
                    "}";
            plugin.getBridge().getWebView().evaluateJavascript(script, null);

            if (plugin instanceof NiimbotPrinterPlugin) {
                ((NiimbotPrinterPlugin) plugin).cleanUp();
            }

            if (!toastMessage.isEmpty()) {
                Toast.makeText(plugin.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
            }

            // Redirect to the specified URI
            //Sometimes the fab button isn't removed and navigation happens. We add a little delay here
            new Handler().postDelayed(() -> {
                plugin.getBridge().getWebView().loadUrl(redirectFullUri);
            }, 1000);
        });
    }


//    @JavascriptInterface
//    public void onDone() {
//        if (plugin instanceof NiimbotPrinterPlugin) {
//            ((NiimbotPrinterPlugin) plugin).notifyDone();
//        }
//    }
}