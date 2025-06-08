plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // No pongas: kotlin("android") version "2.0.21"
    id("com.google.dagger.hilt.android") version "2.48" apply false
}