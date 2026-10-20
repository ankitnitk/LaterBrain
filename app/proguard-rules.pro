# Compose
-keepattributes *Annotation*
-keepclasseswithmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Room ships its own keep rules; enum names are stored in the database, so keep them stable.
-keepclassmembers enum com.laterbrain.app.data.** { *; }

# Keep line numbers for crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
