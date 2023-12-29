import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
}

val myAttribute = Attribute.of("foo", String::class.java)

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }
    
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "19"
            }
        }
    }

    jvm("desktop") {
        attributes.attribute(myAttribute, "desktop")
    }
    jvm("jvm") {
        attributes.attribute(myAttribute, "jvm")
    }

    sourceSets {
        val jvmMain by getting {
            //dependsOn(commonMain.get())
        }

        val androidMain by getting {

        }

        val desktopMain by getting {

        }

        val desktopTest by getting {
            //dependsOn(commonTest.get())
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }


        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(libs.kotlinx.datetime.v050)
        }

        jvmMain.dependencies{
            implementation(libs.exposed.core)
            implementation(libs.exposed.crypt)
            implementation(libs.exposed.dao)
            implementation(libs.exposed.jdbc)
            implementation(libs.exposed.kotlin.datetime)
            implementation(libs.exposed.money)
            implementation(libs.sqlite.jdbc)
            implementation(libs.koin.core)
        }

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android.v353)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

    }
}

android {
    namespace = "com.asaad27"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.asaad27"
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}
dependencies {
    implementation(libs.androidx.constraintlayout)
    testImplementation(project(":composeApp"))
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.asaad27"
            packageVersion = "1.0.0"
        }
    }
}

compose.experimental {
    web.application {}
}