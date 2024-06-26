// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"
}

buildscript {
//    ext.kotlin_version = "1.8.10"
    dependencies {
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        classpath ("org.jetbrains.kotlin:kotlin-serialization:1.8.10")
        classpath("com.google.dagger:hilt-android-testing:2.38.1@aar")
        // Other classpaths
    }
}
