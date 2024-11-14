import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(NiimbotPrinterPlugin)
public class NiimbotPrinterPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "NiimbotPrinterPlugin"
    public let jsName = "NiimbotPrinter"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "echo", returnType: CAPPluginReturnPromise)
    ]
    private let implementation = NiimbotPrinter()

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.resolve([
            "value": implementation.echo(value)
        ])
    }
}
