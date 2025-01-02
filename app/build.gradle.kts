import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // 파이어베이스 & 구글 서비스 플러그인
    id("com.google.gms.google-services")
}

// 프로퍼티를 선언하고, 저장한 키값을 불러온다.
val properties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { properties.load(it) }
}

android {
    namespace = "com.BCU.testingapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.BCU.testingapplication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 로컬 프로퍼티에 비교적 안전하게 저장된 값을 불러온다.
        buildConfigField("String", "GPT_API_KEY", "\"${properties["GPT_API_KEY"]}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    // 파이어베이스 BOM
    implementation("com.google.firebase:firebase-bom:33.7.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")

    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // 안드로이드X 카드뷰
    implementation("androidx.cardview:cardview:1.0.0")

    // WebRTC , HTTP , JSON 처리용
    implementation("io.getstream:stream-webrtc-android:1.3.6")
    implementation ("org.greenrobot:eventbus:3.2.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0") // HTTP 요청 라이브러리
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.google.code.gson:gson:2.8.9")  // JSON 파싱 라이브러리

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}