import SwiftUI
import ComposeApp
import Appodeal

@main
struct iOSApp: App {
    init() {
        InitializeAppDarwinKt.initializeApp()
        
        // Hack for Appodeal, as init fails from Kotlin
        Appodeal.initialize(
            withApiKey: "3bd8b3a757d8fb52be113ab228f056579fc5874d65458486",
            types: [.interstitial, .banner, .rewardedVideo]
        )
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
