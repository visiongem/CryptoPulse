# Keep annotations and inner classes (kotlinx.serialization needs them)
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# kotlinx.serialization — keep generated $$serializer classes and Companion serializers
-keep,includedescriptorclasses class io.github.visiongem.cryptopulse.**$$serializer { *; }
-keepclassmembers class io.github.visiongem.cryptopulse.** {
    *** Companion;
}
-keepclasseswithmembers class io.github.visiongem.cryptopulse.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Retrofit — keep service interfaces and method annotations
-keepclasseswithmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-keepclasseswithmembers class kotlin.Metadata { *; }

# OkHttp / Okio — typically handled by their consumer rules, but keep platform classes
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Glance widget receivers — system instantiates by class name
-keep class * extends androidx.glance.appwidget.GlanceAppWidget { *; }
-keep class * extends androidx.glance.appwidget.GlanceAppWidgetReceiver { *; }

# Compose runtime
-keep class androidx.compose.runtime.** { *; }
-keepclassmembers class * { @androidx.compose.runtime.Composable <methods>; }

# Coroutines
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }

# WorkManager
-keep class * extends androidx.work.ListenableWorker { *; }
