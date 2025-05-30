import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        InitializeAppDarwinKt.initializeApp()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
