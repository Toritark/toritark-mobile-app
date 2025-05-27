import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.*

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.androidx.room)
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

        ios.deploymentTarget = libs.versions.ios.deployment.target.get()

        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "composeApp"
            isStatic = false

            linkerOpts(
                "-framework", "FirebaseCore",
                "-framework", "FirebaseInstallations",
                "-framework", "FirebaseAnalytics",
                "-framework", "FirebaseAuth",
                "-framework", "FirebaseCrashlytics",
                "-framework", "FirebaseCrashlytics",
                "-framework", "GoogleAppMeasurement",
                "-framework", "GoogleUtilities",
                "-framework", "nanopb"
            )
        }

        pod("PurchasesHybridCommon") {
            version = libs.versions.revenuecat.ios.get()
            linkOnly = true
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
        pod("PurchasesHybridCommonUI") {
            version = libs.versions.revenuecat.ios.get()
            linkOnly = true
            extraOpts += listOf("-compiler-option", "-fmodules")
        }

        pod("FirebaseCore") {
            version = libs.versions.firebase.ios.get()
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
        pod("FirebaseAnalytics") {
            version = libs.versions.firebase.ios.get()
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
        pod("FirebaseAuth") {
            version = libs.versions.firebase.ios.get()
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
        pod("FirebaseMessaging") {
            version = libs.versions.firebase.ios.get()
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
        pod("FirebaseCrashlytics") {
            version = libs.versions.firebase.ios.get()
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
        pod("FirebaseInstallations") {
            version = libs.versions.firebase.ios.get()
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
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

        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.io.core)

            implementation(libs.kermit)

            implementation(libs.koin.core)
            implementation(libs.koin.core.coroutines)
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

            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.androidx.room.runtime)
            implementation(libs.kotlinx.datetime)

            implementation(libs.coil)
            implementation(libs.coil.network)

            implementation(libs.ksoup.html)

            implementation(libs.revenuecat.purchases.core)
            implementation(libs.revenuecat.purchases.ui)
            implementation(libs.revenuecat.purchases.datetime)

            implementation(libs.rebugger)

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

            implementation(libs.appodeal.sdk)

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

    // Room
    add("kspAndroid", libs.androidx.room.compiler)
//    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
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

buildkonfig {
    packageName = "com.toritark.app"

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "ENVIRONMENT",
            "",
        )

        buildConfigField(
            FieldSpec.Type.STRING,
            "GOOGLE_SIGN_IN_SERVER_CLIENT_ID",
            "",
        )

        // API
        buildConfigField(
            FieldSpec.Type.STRING,
            "API_HOST",
            project.findProperty("apiHost") as String? ?: "192.168.1.3",
        )

        buildConfigField(
            FieldSpec.Type.INT,
            "API_PORT",
            project.findProperty("apiPort") as String? ?: "8000",
        )

        buildConfigField(
            FieldSpec.Type.BOOLEAN,
            "API_IS_HTTPS",
            project.findProperty("apiIsHttps") as String? ?: "false",
        )

        // Analytics
        buildConfigField(
            FieldSpec.Type.STRING,
            "AMPLITUDE_API_KEY",
            requireNotNull(localProperties.getProperty("amplitude.apiKey")) {
                "Set amplitude.apiKey in local.properties file"
            },
        )

        buildConfigField(
            FieldSpec.Type.STRING,
            "MIXPANEL_API_KEY",
            requireNotNull(localProperties.getProperty("mixpanel.apiKey")) {
                "Set mixpanel.apiKey in local.properties file"
            },
        )

        buildConfigField(
            FieldSpec.Type.STRING,
            "KOCHAVA_APP_GUID",
            requireNotNull(localProperties.getProperty("kochava.android.appGuid")) {
                "Set kochava.android.appGuid in local.properties file"
            },
        )

        // Subscriptions management URL
        buildConfigField(
            FieldSpec.Type.STRING,
            "SUBSCRIPTIONS_MANAGEMENT_URL",
            "",
        )
    }

    defaultConfigs("local") {
        buildConfigField(
            FieldSpec.Type.STRING,
            "ENVIRONMENT",
            "local",
        )

        buildConfigField(
            FieldSpec.Type.STRING,
            "GOOGLE_SIGN_IN_SERVER_CLIENT_ID",
            requireNotNull(localProperties.getProperty("signIn.local.google.serverClientId")) {
                "Set signIn.local.google.serverClientId in local.properties file"
            },
        )
    }

    defaultConfigs("production") {
        buildConfigField(
            FieldSpec.Type.STRING,
            "ENVIRONMENT",
            "production",
        )

        buildConfigField(
            FieldSpec.Type.STRING,
            "GOOGLE_SIGN_IN_SERVER_CLIENT_ID",
            requireNotNull(localProperties.getProperty("signIn.production.google.serverClientId")) {
                "Set signIn.production.google.serverClientId in local.properties file"
            },
        )
    }

    targetConfigs {
        create("android") {
            // Subscriptions management URL
            buildConfigField(
                FieldSpec.Type.STRING,
                "SUBSCRIPTIONS_MANAGEMENT_URL",
                "https://play.google.com/store/account/subscriptions",
            )
        }

        create("ios") {
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