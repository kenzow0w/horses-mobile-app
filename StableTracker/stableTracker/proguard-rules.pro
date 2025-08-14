# Keep Compose generated classes
-keep class androidx.compose.** { *; }
-keep class * extends androidx.compose.runtime.Composable
-dontwarn kotlin.**
-dontwarn kotlinx.coroutines.**