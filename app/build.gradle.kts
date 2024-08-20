plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.protobuf") version "0.9.4" apply true
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


        implementation("androidx.core:core-ktx:1.12.0")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.11.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.core:core-splashscreen:1.0.1")

        // navigation components
        val nav_version = "2.7.7"
        implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
        implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
        implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")

        val lifecycle_version = "2.7.0"
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
        implementation("androidx.fragment:fragment-ktx:1.6.2")
        implementation("androidx.activity:activity-ktx:1.8.2")
        implementation("androidx.datastore:datastore-preferences:1.0.0")
        implementation("com.google.protobuf:protobuf-kotlin-lite:4.26.0")

        // firebase dependencies
        implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
        implementation("com.google.firebase:firebase-analytics")
        implementation("com.google.firebase:firebase-crashlytics")
        implementation("com.google.firebase:firebase-firestore-ktx")
        implementation("com.google.firebase:firebase-auth-ktx")
        implementation("com.google.android.gms:play-services-auth:21.0.0")
        implementation ("com.facebook.android:facebook-login:16.0.0")

        // third party libraries
        implementation("com.github.pwittchen:reactivenetwork-rx2:3.0.8")

        // Local Unit Tests
        implementation("androidx.test:core-ktx:1.5.0")
        testImplementation("junit:junit:4.13.2")
        testImplementation("org.hamcrest:hamcrest-all:1.3")
        testImplementation("androidx.arch.core:core-testing:2.2.0")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
        testImplementation("com.google.truth:truth:1.1.3")
        testImplementation("org.mockito:mockito-core:5.11.0")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
        testImplementation("com.google.dagger:hilt-android-testing:2.44.2")
        kaptTest("com.google.dagger:hilt-android-compiler:2.44")

        // Instrumented Unit Tests
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("junit:junit:4.13.2")
        androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
        androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
        androidTestImplementation("com.google.truth:truth:1.1.3")
        androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        androidTestImplementation("org.mockito:mockito-core:5.11.0")
        androidTestImplementation("org.mockito:mockito-android:5.11.0")
        androidTestImplementation("com.google.dagger:hilt-android-testing:2.44.2")
        kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.44")
        debugImplementation("androidx.fragment:fragment-testing:1.6.2")
        androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    }
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.26.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

