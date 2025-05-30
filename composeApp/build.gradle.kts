import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.TargetConfigDsl
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.*

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.gradle.versions)
    alias(libs.plugins.build.konfig)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.kotlin.cocoapods)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    } else {
        throw IllegalStateException("local.properties file not found")
    }
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    cocoapods {
        version = "1.0"

        summary = "Toritark app"
        homepage = "https://toritark.com"

        ios.deploymentTarget = iosLibs.versions.ios.deployment.target.get()

        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "ComposeApp"
            isStatic = false

            linkerOpts(
                "-framework", "FirebaseCore",
                "-framework", "FirebaseInstallations",
                "-framework", "FirebaseAnalytics",
                "-framework", "FirebaseAuth",
                "-framework", "FirebaseCrashlytics",
                "-framework", "GoogleAppMeasurement",
                "-framework", "GoogleUtilities",
                "-framework", "nanopb",
                "-framework", "Google-Mobile-Ads-SDK",
//                "-framework", "Appodeal",
//                "-framework", "StackModules",
//                "-framework", "StackConsentManager",
            )
        }

        specRepos {
            url("https://cdn.cocoapods.org")
//            url("https://github.com/bidon-io/CocoaPods_Specs.git")
//            url("https://github.com/appodeal/CocoaPods.git")
        }

        addFirebasePods()
        addGoogleAdsPods()
        addRevenueCatPods()
//        addAppodealPods()
    }

    @Suppress("OPT_IN_USAGE")
    wasmJs {
        outputModuleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        add(project.rootDir.path)
                        add(project.projectDir.path)
                    }
                }
            }
        }

        compilerOptions {
            freeCompilerArgs.addAll(
                "-Xwasm-use-traps-instead-of-exceptions",
                "-Xwasm-use-new-exception-proposal",
            )
        }

        binaries.executable()
    }

    sourceSets {
        all {
            languageSettings.enableLanguageFeature("ExpectActualClasses")
        }

        named { it.lowercase().startsWith("ios") }.configureEach {
            languageSettings {
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }

        val iosAndAndroidMain by creating {
            dependsOn(commonMain.get())
        }

        androidMain {
            dependsOn(iosAndAndroidMain)
        }

        iosMain {
            dependsOn(iosAndAndroidMain)
        }

        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.io.core)

            implementation(libs.kermit)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)

            implementation(libs.kotlinx.serialization.json)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.resources)

            implementation(libs.settings)
            implementation(libs.settings.coroutines)

            implementation(libs.kotlinx.datetime)

            implementation(libs.coil)
            implementation(libs.coil.network)

            implementation(libs.ksoup.html)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.compose.material.icons.core)
            implementation(libs.compose.material.icons.extended)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation)
        }

        iosAndAndroidMain.dependencies {
            implementation(libs.revenuecat.purchases.core)
            implementation(libs.revenuecat.purchases.ui)
            implementation(libs.revenuecat.purchases.datetime)
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.cio)
            implementation(libs.koin.android)

            implementation(project.dependencies.platform(libs.firebase.android.bom))
            implementation(libs.firebase.android.analytics)
            implementation(libs.firebase.android.crashlytics)
            implementation(libs.firebase.android.messaging)
            implementation(libs.firebase.android.messaging.ktx)
            implementation(libs.firebase.android.auth)
            implementation(libs.firebase.android.auth.ktx)

            implementation(libs.androidx.credentials.main)
            implementation(libs.androidx.credentials.play.services.auth)
            implementation(libs.google.identity.google.id)

            implementation(libs.appodeal.sdk.android)

            implementation(libs.play.services.ads.id)
            implementation(libs.play.services.app.set)
            implementation(libs.android.install.referrer)

            implementation(libs.amplitude.android)
            implementation(libs.mixpanel.android)
            implementation(libs.facebook.sdk.android.core)
            implementation(libs.facebook.sdk.android.marketing)
            implementation(libs.kochava.android.tracker)
            implementation(libs.kochava.android.events)
            implementation(libs.kochava.android.datapoints)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        wasmJsMain.dependencies {
            implementation(libs.koin.core.wasm.js)
            implementation(libs.ktor.client.js)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.junit)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(libs.ktor.client.mock)
        }

        androidUnitTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.test.junit)
            implementation(libs.junit)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(libs.ktor.client.mock)
            implementation(libs.mockk.agent)
            implementation(libs.mockk.android)
            implementation(libs.androidx.test.junit)
        }

        androidInstrumentedTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.test.junit)
            implementation(libs.junit)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(libs.ktor.client.mock)
            implementation(libs.mockk.agent)
            implementation(libs.mockk.android)
            implementation(libs.androidx.test.junit)
            implementation(libs.androidx.espresso.core)
        }
    }
}

android {
    namespace = "com.toritark.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.toritark.app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = getVersionCode()
        versionName = getVersionName()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        create("release") {
            keyAlias = localProperties["signing.keyAlias"] as String
            keyPassword = localProperties["signing.keyPassword"] as String
            storeFile = file(localProperties["signing.storeFile"] as String)
            storePassword = localProperties["signing.storePassword"] as String
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")

            buildConfigField("boolean", "DEBUG", "false")
        }

        getByName("debug") {
            buildConfigField("boolean", "DEBUG", "true")
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    testOptions {
        packaging {
            resources.excludes.add("META-INF/*")
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

tasks.withType<Test> {
    if (name == "mergeDebugAndroidTestAssets") {
        enabled = false
    }
}

tasks.withType<Test> {
    if (name == "copyRoomSchemasToAndroidTestAssetsDebugAndroidTest") {
        enabled = false
    }
}

tasks.whenTaskAdded {
    if (name.contains("copyRoomSchemasToAndroidTestAssetsDebugAndroidTest")) {
        enabled = false
    }
}

fun TargetConfigDsl.emptyStringField(fieldName: String) {
    buildConfigField(FieldSpec.Type.STRING, fieldName, "")
}

fun TargetConfigDsl.propertyField(
    type: FieldSpec.Type,
    fieldName: String,
    propertyName: String,
    defaultValue: String = "",
) {
    buildConfigField(
        type,
        fieldName,
        project.findProperty(propertyName) as String? ?: defaultValue,
    )
}

fun TargetConfigDsl.localPropertiesStringField(fieldName: String, propertyName: String) {
    buildConfigField(
        FieldSpec.Type.STRING,
        fieldName,
        requireNotNull(localProperties.getProperty(propertyName)) {
            "Set $propertyName in local.properties file"
        },
    )
}

buildkonfig {
    packageName = "com.toritark.app"

    defaultConfigs {
        emptyStringField("ENVIRONMENT")
        emptyStringField("GOOGLE_SIGN_IN_SERVER_CLIENT_ID")

        // API
        propertyField(
            type = FieldSpec.Type.STRING,
            fieldName = "API_HOST",
            propertyName = "apiHost",
            defaultValue = "192.168.1.3",
        )

        propertyField(
            type = FieldSpec.Type.INT,
            fieldName = "API_PORT",
            propertyName = "apiPort",
            defaultValue = "8000",
        )

        propertyField(
            type = FieldSpec.Type.BOOLEAN,
            fieldName = "API_IS_HTTPS",
            propertyName = "apiIsHttps",
            defaultValue = "false",
        )

        // Appodeal
        emptyStringField("APPODEAL_KEY")
        // RevenueCat
        emptyStringField("REVENUE_CAT_API_KEY")
        // Analytics
        localPropertiesStringField("AMPLITUDE_API_KEY", "amplitude.apiKey")
        localPropertiesStringField("MIXPANEL_API_KEY", "mixpanel.apiKey")
        localPropertiesStringField("KOCHAVA_APP_GUID", "mixpanel.apiKey")
        emptyStringField("KOCHAVA_APP_GUID")

        // Subscriptions management URL
        emptyStringField("SUBSCRIPTIONS_MANAGEMENT_URL")
    }

    defaultConfigs("local") {
        buildConfigField(FieldSpec.Type.STRING, "ENVIRONMENT", "local")
        localPropertiesStringField(
            "GOOGLE_SIGN_IN_SERVER_CLIENT_ID",
            "signIn.local.google.serverClientId",
        )
    }

    defaultConfigs("production") {
        buildConfigField(FieldSpec.Type.STRING, "ENVIRONMENT", "production")
        localPropertiesStringField(
            "GOOGLE_SIGN_IN_SERVER_CLIENT_ID",
            "signIn.production.google.serverClientId",
        )
    }

    targetConfigs {
        create("android") {
            // RevenueCat
            localPropertiesStringField("REVENUE_CAT_API_KEY", "revenuecat.android.key")
            // Appodeal
            localPropertiesStringField("APPODEAL_KEY", "appodeal.android.key")

            // Subscriptions management URL
            buildConfigField(
                FieldSpec.Type.STRING,
                "SUBSCRIPTIONS_MANAGEMENT_URL",
                "https://play.google.com/store/account/subscriptions",
            )

            // Analytics
            localPropertiesStringField("KOCHAVA_APP_GUID", "kochava.android.appGuid")
        }

        create("iosSimulatorArm64") {
            // RevenueCat
            localPropertiesStringField("REVENUE_CAT_API_KEY", "revenuecat.ios.key")
            // Appodeal
            localPropertiesStringField("APPODEAL_KEY", "appodeal.ios.key")

            // Subscriptions management URL
            buildConfigField(
                FieldSpec.Type.STRING,
                "SUBSCRIPTIONS_MANAGEMENT_URL",
                "https://apps.apple.com/account/subscriptions",
            )
        }

        create("iosArm64") {
            // RevenueCat
            localPropertiesStringField("REVENUE_CAT_API_KEY", "revenuecat.ios.key")
            // Appodeal
            localPropertiesStringField("APPODEAL_KEY", "appodeal.ios.key")

            // Subscriptions management URL
            buildConfigField(
                FieldSpec.Type.STRING,
                "SUBSCRIPTIONS_MANAGEMENT_URL",
                "https://apps.apple.com/account/subscriptions",
            )
        }
    }
}

fun getVersionCode(): Int {
    val yearStr = libs.versions.app.version.year.get()
    val monthStr = libs.versions.app.version.month.get()
    val dateStr = libs.versions.app.version.date.get()
    val buildStr = libs.versions.app.version.build.get()

    val currentYearYY = yearStr.toIntOrNull()
        ?: throw IllegalArgumentException("Invalid app-version-year: '$yearStr'. Must be an integer.")
    if (yearStr.length != 2 || currentYearYY < 0 || currentYearYY > 99) {
        throw IllegalArgumentException(
            "Invalid app-version-year: '$yearStr'. Must be a two-digit number (00-99)."
        )
    }

    val month = monthStr.toIntOrNull()
        ?: throw IllegalArgumentException("Invalid app-version-month: '$monthStr'. Must be an integer.")
    if (monthStr.length != 2 || month !in 1..12) {
        throw IllegalArgumentException("Invalid app-version-month: '$monthStr'. Must be a two-digit number between 01 and 12.")
    }

    val day = dateStr.toIntOrNull() ?: throw IllegalArgumentException(
        "Invalid app-version-date: '$dateStr'. Must be an integer."
    )
    if (dateStr.length != 2 || day !in 1..31) {
        throw IllegalArgumentException("Invalid app-version-date: '$dateStr'. Must be a two-digit number between 01 and 31.")
    }
    val build = buildStr.toIntOrNull() ?: throw IllegalArgumentException(
        "Invalid app-version-build: '$buildStr'. Must be an integer."
    )
    if (buildStr.isEmpty() || buildStr.length > 3 || build < 0 || build > 999) {
        throw IllegalArgumentException(
            "Invalid app-version-build: '$buildStr'. Must be a 1 to 3 digit number between 0 and 999."
        )
    }
    val baseYearYY = 24

    val processedYear = currentYearYY - baseYearYY
    if (processedYear < 0) {
        throw IllegalStateException(
            "app-version-year ('$yearStr') is earlier than the configured baseYearYY ('$baseYearYY'). " +
                    "Ensure baseYearYY is appropriate or versions are always increasing."
        )
    }

    val versionCode = (processedYear * 10_000_000) +
            (month * 100_000) +
            (day * 1_000) +
            (build)

    return versionCode
}


fun getVersionName(): String {
    return buildString {
        libs.versions.app.version.year.get().let(::append)
        append(".")
        libs.versions.app.version.month.get().let(::append)
        append(".")
        libs.versions.app.version.date.get().let(::append)
        append(".")
        libs.versions.app.version.build.get().let(::append)
    }
}

/**
 * Cocoapods
 */
fun CocoapodsExtension.addPod(
    name: String,
    version: Provider<String>,
    linkOnly: Boolean = false,
    configure: CocoapodsExtension.CocoapodsDependency.() -> Unit = {},
) {
    pod(name) {
        this.version = version.get()
        extraOpts += listOf("-compiler-option", "-fmodules")
        this.linkOnly = linkOnly;

        configure()
    }
}

fun CocoapodsExtension.addFirebasePods() {
    addPod(name = "FirebaseCore", version = iosLibs.versions.firebase)
    addPod(name = "FirebaseAnalytics", version = iosLibs.versions.firebase)
    addPod(name = "FirebaseAuth", version = iosLibs.versions.firebase)
    addPod(name = "FirebaseMessaging", version = iosLibs.versions.firebase)
    addPod(name = "FirebaseCrashlytics", version = iosLibs.versions.firebase)
    addPod(name = "FirebaseInstallations", version = iosLibs.versions.firebase)
}

fun CocoapodsExtension.addGoogleAdsPods() {
    addPod(name = "Google-Mobile-Ads-SDK", version = iosLibs.versions.google.mobile.ads.ios)
}

fun CocoapodsExtension.addRevenueCatPods() {
    addPod(name = "PurchasesHybridCommon", version = iosLibs.versions.revenuecat, linkOnly = true)
    addPod(name = "PurchasesHybridCommonUI", version = iosLibs.versions.revenuecat, linkOnly = true)
}

fun CocoapodsExtension.addAppodealPods() {
    addPod(name = "Appodeal", version = iosLibs.versions.appodeal.sdk)
    addPod(name = "APDAmazonAdapter", version = iosLibs.versions.appodeal.amazon.adapter, linkOnly = true)
    addPod(name = "APDAppLovinAdapter", version = iosLibs.versions.appodeal.applovin.adapter, linkOnly = true)
    addPod(name = "APDAppLovinMAXAdapter", version = iosLibs.versions.appodeal.applovin.max.adapter, linkOnly = true)
    addPod(name = "APDBidMachineAdapter", version = iosLibs.versions.appodeal.bid.machine.adapter, linkOnly = true)
    addPod(name = "APDBidonAdapter", version = iosLibs.versions.appodeal.bidon.adapter.main, linkOnly = true)
    addPod(name = "APDBigoAdsAdapter", version = iosLibs.versions.appodeal.bigo.ads.adapter, linkOnly = true)
    addPod(name = "APDDTExchangeAdapter", version = iosLibs.versions.appodeal.dt.exchange.adapter, linkOnly = true)
    addPod(name = "APDGoogleAdMobAdapter", version = iosLibs.versions.appodeal.admob.adapter, linkOnly = true)
    addPod(name = "APDIABAdapter", version = iosLibs.versions.appodeal.iab.adapter, linkOnly = true)
    addPod(name = "APDInMobiAdapter", version = iosLibs.versions.appodeal.inmobi.adapter, linkOnly = true)
    addPod(name = "APDIronSourceAdapter", version = iosLibs.versions.appodeal.ironsource.adapter, linkOnly = true)
    addPod(name = "APDLevelPlayAdapter", version = iosLibs.versions.appodeal.levelplay.adapter, linkOnly = true)
    addPod(
        name = "APDMetaAudienceNetworkAdapter",
        version = iosLibs.versions.appodeal.meta.audience.network.adapter,
        linkOnly = true,
    )
    addPod(name = "APDMintegralAdapter", version = iosLibs.versions.appodeal.mintegral.adapter, linkOnly = true)
    addPod(name = "APDMyTargetAdapter", version = iosLibs.versions.appodeal.mytarget.adapter, linkOnly = true)
    addPod(name = "APDPangleAdapter", version = iosLibs.versions.appodeal.pangle.adapter, linkOnly = true)
    addPod(name = "APDSentryAdapter", version = iosLibs.versions.appodeal.sentry.adapter, linkOnly = true)
    addPod(name = "APDSmaatoAdapter", version = iosLibs.versions.appodeal.smaato.adapter, linkOnly = true)
    addPod(name = "APDUnityAdapter", version = iosLibs.versions.appodeal.unity.adapter, linkOnly = true)
    addPod(name = "APDVungleAdapter", version = iosLibs.versions.appodeal.vungle.adapter, linkOnly = true)
    addPod(name = "APDYandexAdapter", version = iosLibs.versions.appodeal.yandex.adapter, linkOnly = true)
    addPod(
        name = "AppLovinMediationAmazonAdMarketplaceAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.amazon.ad.marketplace.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationBidMachineAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.bid.machine.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationBigoAdsAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.bigo.ads.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationByteDanceAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.bytedance.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationChartboostAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.chartboost.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationFacebookAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.facebook.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationFyberAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.fyber.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationGoogleAdManagerAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.google.ad.manager.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationGoogleAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.google.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationInMobiAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.inmobi.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationIronSourceAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.ironsource.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationMintegralAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.mintegral.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationMobileFuseAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.mobile.fuse.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationMolocoAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.moloco.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationMyTargetAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.mytarget.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationOguryPresageAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.ogury.presage.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationSmaatoAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.smaato.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationUnityAdsAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.unity.ads.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationVerveAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.verve.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationVungleAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.vungle.adapter,
        linkOnly = true,
    )
    addPod(
        name = "AppLovinMediationYandexAdapter",
        version = iosLibs.versions.appodeal.applovin.mediation.yandex.adapter,
        linkOnly = true,
    )
    addPod(
        name = "BidMachineAmazonAdapter",
        version = iosLibs.versions.appodeal.bid.machine.amazon.adapter,
        linkOnly = true,
    )
    addPod(
        name = "BidMachineMetaAudienceAdapter",
        version = iosLibs.versions.appodeal.bid.machine.meta.audience.adapter,
        linkOnly = true,
    )
    addPod(
        name = "BidMachineMintegralAdapter",
        version = iosLibs.versions.appodeal.bid.machine.mintegral.adapter,
        linkOnly = true,
    )
    addPod(
        name = "BidMachineMyTargetAdapter",
        version = iosLibs.versions.appodeal.bid.machine.mytarget.adapter,
        linkOnly = true,
    )
    addPod(
        name = "BidMachinePangleAdapter",
        version = iosLibs.versions.appodeal.bid.machine.pangle.adapter,
        linkOnly = true,
    )
    addPod(
        name = "BidMachineVungleAdapter",
        version = iosLibs.versions.appodeal.bid.machine.vungle.adapter,
        linkOnly = true,
    )
    addPod(
        name = "BidonAdapterAmazon",
        version = iosLibs.versions.appodeal.bidon.adapter.amazon,
        linkOnly = true,
    )
    addPod(
        name = "BidonAdapterAppLovin",
        version = iosLibs.versions.appodeal.bidon.adapter.applovin,
        linkOnly = true,
    )
    addPod(
        name = "BidonAdapterBidMachine",
        version = iosLibs.versions.appodeal.bidon.adapter.bid.machine,
        linkOnly = true,
    )
    addPod(
        name = "BidonAdapterBigoAds",
        version = iosLibs.versions.appodeal.bidon.adapter.bigo.ads,
        linkOnly = true,
    )
    addPod(
        name = "BidonAdapterChartboost",
        version = iosLibs.versions.appodeal.bidon.adapter.chartboost,
        linkOnly = true,
    )
    addPod(
        name = "BidonAdapterDTExchange",
        version = iosLibs.versions.appodeal.bidon.adapter.dt.exchange,
        linkOnly = true,
    )
    addPod(
        name = "BidonAdapterInMobi",
        version = iosLibs.versions.appodeal.bidon.adapter.inmobi,
        linkOnly = true,
    )
    addPod(
        name = "BidonAdapterIronSource",
        version = iosLibs.versions.appodeal.bidon.adapter.ironsource,
        linkOnly = true,
    )
    addPod(
        name = "BidonAdapterMetaAudienceNetwork",
        version = iosLibs.versions.appodeal.bidon.adapter.meta.audience.network,
        linkOnly = true,
    )
    addPod(
        name = "BidonAdapterMintegral",
        version = iosLibs.versions.appodeal.bidon.adapter.mintegral,
        linkOnly = true,
    )
    addPod(
        name = "BidonAdapterMobileFuse",
        version = iosLibs.versions.appodeal.bidon.adapter.mobile.fuse,
        linkOnly = true,
    )
    addPod(
        name = "BidonAdapterMyTarget",
        version = iosLibs.versions.appodeal.bidon.adapter.ironsource,
        linkOnly = true,
    )
    addPod(
        name = "BidonAdapterUnityAds",
        version = iosLibs.versions.appodeal.bidon.adapter.unity.ads,
        linkOnly = true,
    )
    addPod(
        name = "BidonAdapterVungle",
        version = iosLibs.versions.appodeal.bidon.adapter.vungle,
        linkOnly = true,
    )
    addPod(
        name = "BidonAdapterYandex",
        version = iosLibs.versions.appodeal.bidon.adapter.yandex,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceAPSAdapter",
        version = iosLibs.versions.appodeal.ironsource.aps.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceAdMobAdapter",
        version = iosLibs.versions.appodeal.ironsource.admob.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceAppLovinAdapter",
        version = iosLibs.versions.appodeal.ironsource.applovin.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceBidMachineAdapter",
        version = iosLibs.versions.appodeal.ironsource.bid.machine.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceBigoAdapter",
        version = iosLibs.versions.appodeal.ironsource.bigo.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceFacebookAdapter",
        version = iosLibs.versions.appodeal.ironsource.facebook.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceFyberAdapter",
        version = iosLibs.versions.appodeal.ironsource.fyber.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceInMobiAdapter",
        version = iosLibs.versions.appodeal.ironsource.inmobi.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceMintegralAdapter",
        version = iosLibs.versions.appodeal.ironsource.mintegral.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceMobileFuseAdapter",
        version = iosLibs.versions.appodeal.ironsource.mobile.fuse.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceMolocoAdapter",
        version = iosLibs.versions.appodeal.ironsource.moloco.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceMyTargetAdapter",
        version = iosLibs.versions.appodeal.ironsource.mytarget.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceOguryAdapter",
        version = iosLibs.versions.appodeal.ironsource.ogury.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourcePangleAdapter",
        version = iosLibs.versions.appodeal.ironsource.pangle.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceSmaatoAdapter",
        version = iosLibs.versions.appodeal.ironsource.smaato.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceUnityAdsAdapter",
        version = iosLibs.versions.appodeal.ironsource.unity.ads.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceVerveAdapter",
        version = iosLibs.versions.appodeal.ironsource.verve.adapter,
        linkOnly = true,
    )
    addPod(
        name = "IronSourceVungleAdapter",
        version = iosLibs.versions.appodeal.ironsource.vungle.adapter,
        linkOnly = true,
    )
}
