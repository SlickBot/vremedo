# Keep readable stack traces in release crash reports.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ARSO API responses are parsed by Gson via reflection.
-keep class eu.slickbot.arso.model.** { *; }
-keepclassmembers enum eu.slickbot.arso.model.** { *; }
