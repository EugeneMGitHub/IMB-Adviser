plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    //DAGGER HILT
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
    // KOTLIN SYMBOL PROCCESING (KSP)
    // https://developer.android.com/build/migrate-to-ksp
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.shareappsettingswithgiraffe"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.shareappsettingswithgiraffe"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }


        // added this for adding schemas for room migration
        ksp {
            arg("room.incremental", "true")
            arg("room.exportSchema", "true")
            arg("room.schemaLocation", "$projectDir/schemas")
        }

    }

    // added this for testing migrations before
    // implementing it so that the app doesn't crash
    // https://developer.android.com/training/data-storage/room/migrating-db-versions
    sourceSets {
        // Adds exported schema location as test app assets.
        getByName("androidTest").assets.srcDir("$projectDir/schemas")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // COMPOSE DEPENDENCIES
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.navigation:navigation-compose:2.7.0-beta02")
    // these for huge collection of icons
    // implementation "androidx.compose.material:material-icons-extended:$compose_ui_version"
    implementation("androidx.compose.material:material")
    //??????
    implementation ("androidx.compose.runtime:runtime-livedata:1.4.3")
    implementation("androidx.compose.runtime:runtime:1.4.3")

    // COROUTINES DEPENDENCIES
    // 1.6.4
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.2")
    // Coroutine Lifecycle Scopes
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation ("androidx.activity:activity-ktx:1.7.2")
    implementation ("androidx.compose.runtime:runtime-livedata:1.4.3")

    // ?????????????????????????????
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.6.1")

    // Если ругается про JDK и java version, то делаем jvmTarget, JavaVersion, targetCompatibility одинаковыми (например 17). Чтобы выбрать JDK идем в Settings->Build,Exucuation....->BuildTools->Gradle-> и выбираем соответсвующую указанным версиям JDK (в моем случае это было Jetbrain runtime version 17)
    // DAGGER HILT DEPENDENCIES
    implementation ("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-android-compiler:2.47")
    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")
    // WORK MANAGER
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")




    // ROOM DEPENDENCIES
//    implementation ("androidx.room:room-runtime:2.5.2")
////    kapt("androidx.room:room-compiler:2.5.2")
//    // Kotlin Extensions and Coroutines support for Room
//    implementation ("androidx.room:room-ktx:2.5.2")
//    // ksp for Room (can use instead of Kapt)
//    // https://developer.android.com/build/migrate-to-ksp
//    ksp("androidx.room:room-compiler:2.5.2")
//    androidTestImplementation ("androidx.room:room-testing:2.5.2")
//    // assertion library for testing
//    androidTestImplementation ("com.google.truth:truth:1.1.3")
    implementation ("androidx.room:room-runtime:2.5.2")
    implementation ("androidx.room:room-ktx:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")
    androidTestImplementation ("androidx.room:room-testing:2.5.2")
    androidTestImplementation ("com.google.truth:truth:1.1.3")


    // RETROFIT DEPENDENCIES
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.3")
    // Coil - загрузка картинок по url
    // implementation("io.coil-kt:coil-compose:2.2.0")


    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}