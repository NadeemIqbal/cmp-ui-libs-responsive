// swift-tools-version:5.9
//
// SwiftPM manifest for io.github.nadeemiqbal:responsive-ui as a precompiled
// XCFramework for Swift / Objective-C consumers.
//
// The URL and checksum below are kept in sync with the latest tagged release
// by .github/workflows/publish.yml — see the "Update Package.swift" step.
//
// Swift consumer usage:
//
//     dependencies: [
//         .package(url: "https://github.com/NadeemIqbal/cmp-ui-libs-responsive", exact: "1.0.0")
//     ]
//
// Then import in Swift:
//
//     import ResponsiveUI
//
// Kotlin Multiplatform consumers should use the Maven Central coordinate
// instead — `io.github.nadeemiqbal:responsive-ui:1.0.0`.

import PackageDescription

let package = Package(
    name: "ResponsiveUI",
    platforms: [.iOS(.v13)],
    products: [
        .library(name: "ResponsiveUI", targets: ["ResponsiveUI"]),
    ],
    targets: [
        .binaryTarget(
            name: "ResponsiveUI",
            // PLACEHOLDER — auto-updated by .github/workflows/publish.yml on each tagged release.
            url: "https://github.com/NadeemIqbal/cmp-ui-libs-responsive/releases/download/v1.0.0/ResponsiveUI.xcframework.zip",
            checksum: "93f4852f57cd4a2314d9933f315ba57b55a5ce13c7307bc29cead33c8d233dfd"
        ),
    ]
)
