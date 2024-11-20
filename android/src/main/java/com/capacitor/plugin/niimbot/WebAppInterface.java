package com.capacitor.plugin.niimbot;

import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.getcapacitor.Plugin;

public class WebAppInterface {
    private Plugin plugin;

    public WebAppInterface(Plugin plugin) {
        this.plugin = plugin;
    }

    @JavascriptInterface
    public void onCloseButtonClick(final String toastMessage) {
        if(!toastMessage.isEmpty()) {
            Toast.makeText(plugin.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
        }
    }

//    @JavascriptInterface
//    public void onDone() {
//        if (plugin instanceof NiimbotPrinterPlugin) {
//            ((NiimbotPrinterPlugin) plugin).notifyDone();
//        }
//    }
}