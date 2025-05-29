import SwiftUI
import ComposeApp
import Appodeal

@main
struct iOSApp: App {
    init() {
        InitializeAppDarwinKt.initializeApp()
        
        // Appodeal.initialize(
        //             withApiKey: "3bd8b3a757d8fb52be113ab228f056579fc5874d65458486",
        //             types: .interstitial
        //         )

    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
