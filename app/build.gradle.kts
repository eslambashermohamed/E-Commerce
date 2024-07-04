plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-kapt")
}

android {
    namespace = "com.islam.ecommerce"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.islam.ecommerce"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"


    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        forEach{
            it.buildConfigField(
                "String","clientServerId","\"688491006186-gdfv5fvdke5brov1bh1salc061jgka9f.apps.googleusercontent.com\""
            )
            it.resValue(
                "string",
                "facebook_app_id",
                "\"1034286711747566\""
            )
            it.resValue(
                "string",
                "fb_login_protocol_scheme",
                "\"fb1034286711747566\""
            )
            it.resValue(
                "string",
                "facebook_client_token",
                "\"730a2a631b2c5d509283877277fe161e\""
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation("com.github.pwittchen:reactivenetwork-rx2:3.0.8")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-instantapps:18.1.0")
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //splash screen
    implementation("androidx.core:core-splashscreen:1.0.0")
    //datastore
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.datastore:datastore-core:1.1.1")
    //navigation
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    //viewmodel lifecycle
    implementation("androidx.fragment:fragment-ktx:1.3.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    //firebase
    implementation("androidx.datastore:datastore-core:1.1.1")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation ("com.facebook.android:facebook-login:16.0.0")
    implementation ("com.facebook.android:facebook-android-sdk:17.0.0")

    //coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
}