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
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        moduleName = "composeApp"
//        browser {
//            val rootDirPath = project.rootDir.path
//            val projectDirPath = project.projectDir.path
//            commonWebpackConfig {
//                outputFileName = "composeApp.js"
//                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
//                    static = (static ?: mutableListOf()).apply {
//                        // Serve sources to debug inside browser
//                        add(rootDirPath)
//                        add(projectDirPath)
//                    }
//                }
//            }
//        }
//        binaries.executable()
//    }

    sourceSets {
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
            "",
        )

        buildConfigField(
            FieldSpec.Type.INT,
            "API_PORT",
            "0",
        )

        buildConfigField(
            FieldSpec.Type.BOOLEAN,
            "API_IS_HTTPS",
            "false",
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

        // API
        buildConfigField(
            FieldSpec.Type.STRING,
            "API_HOST",
            requireNotNull(localProperties.getProperty("api.local.host")) {
                "Set api.local.host in local.properties file"
            },
        )

        buildConfigField(
            FieldSpec.Type.INT,
            "API_PORT",
            requireNotNull(localProperties.getProperty("api.local.port")) {
                "Set api.local.port in local.properties file"
            },
        )

        buildConfigField(
            FieldSpec.Type.BOOLEAN,
            "API_IS_HTTPS",
            requireNotNull(localProperties.getProperty("api.local.isHttps")) {
                "Set api.local.isHttps in local.properties file"
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

        // API
        buildConfigField(
            FieldSpec.Type.STRING,
            "API_HOST",
            requireNotNull(localProperties.getProperty("api.production.host")) {
                "Set api.production.host in local.properties file"
            },
        )

        buildConfigField(
            FieldSpec.Type.INT,
            "API_PORT",
            requireNotNull(localProperties.getProperty("api.production.port")) {
                "Set api.production.port in local.properties file"
            },
        )

        buildConfigField(
            FieldSpec.Type.BOOLEAN,
            "API_IS_HTTPS",
            requireNotNull(localProperties.getProperty("api.production.isHttps")) {
                "Set api.production.isHttps in local.properties file"
            },
        )
    }

    targetConfigs {
        create("android") {

        }

        create("ios") {

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
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false

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