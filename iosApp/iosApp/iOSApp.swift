import SwiftUI
import ComposeApp
import Appodeal

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
