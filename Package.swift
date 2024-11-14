// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorNiimbotPrinter",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "CapacitorNiimbotPrinter",
            targets: ["NiimbotPrinterPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main")
    ],
    targets: [
        .target(
            name: "NiimbotPrinterPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/NiimbotPrinterPlugin"),
        .testTarget(
            name: "NiimbotPrinterPluginTests",
            dependencies: ["NiimbotPrinterPlugin"],
            path: "ios/Tests/NiimbotPrinterPluginTests")
    ]
)