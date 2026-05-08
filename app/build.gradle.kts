import org.gradle.external.javadoc.StandardJavadocDocletOptions

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.masroofy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.masroofy"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}

afterEvaluate {
    tasks.register<Javadoc>("androidJavadoc") {
        val android = project.extensions.findByName("android") as? com.android.build.gradle.BaseExtension
        
        // 1. Set source code path correctly
        source = project.fileTree("${project.projectDir}/src/main/java") {
            include("**/*.java")
        }
        
        // 2. Classpath - include Android framework AND all dependencies
        if (android != null) {
            classpath += project.files(android.bootClasspath)
        }
        
        // Include compiled classes and dependencies
        val compileTask = tasks.findByName("compileDebugJavaWithJavac") as? JavaCompile
        if (compileTask != null) {
            classpath += compileTask.classpath
            classpath += project.files(compileTask.destinationDirectory)
        }

        // 3. Destination folder in the root for easy access
        destinationDir = file("${project.rootDir}/docs/javadoc")
        
        title = "Masroofy App Documentation"

        (options as? StandardJavadocDocletOptions)?.apply {
            encoding = "UTF-8"
            charSet = "UTF-8"
            // Use quiet mode and ignore warnings/errors
            addStringOption("Xdoclint:none", "-quiet")
        }

        isFailOnError = false
    }
}
