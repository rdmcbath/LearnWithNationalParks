# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Rebecca\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Preverification is irrelevant for the dex compiler and the Dalvik VM, so we can switch it off with the -dontpreverify option.
-dontpreverify
-repackageclasses

# Retain source file names and line numbers for stack traces to make debugging easier
-keepattributes SourceFile, LineNumberTable
# Preserve logging info on custom exceptions
-keep public class * extends java.lang.Exception
# Remove verbose, debug, and info logs from the code
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
}

### Test rules for Proguard ###
-dontwarn android.test.**
-dontwarn android.support.test.**
-keep class org.junit.** { *; }
-dontwarn org.junit.**
-keep class junit.** { *; }
-dontwarn junit.**
-keep class sun.misc.** { *; }
-dontwarn sun.misc.**
-dontnote sun.misc.**

# Hide benign apache legacy notes - https://code.google.com/p/android/issues/detail?id=194513
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**

### Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
-dontwarn okhttp3.**
-dontnote okio.**
-dontwarn javax.annotation.**

### Retrofit 2.X ## https://square.github.io/retrofit/ ##
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java
-dontwarn retrofit2.**
-dontnote retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

### Jackson 2.x
-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}
-keep @com.fasterxml.jackson.annotation.JsonIgnoreProperties class * { *; }
-keep class com.fasterxml.** { *; }
-keep class org.codehaus.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepclassmembers public final enum com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility {
    public static final com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility *;
}

-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn com.fasterxml.jackson.databind.**
-dontnote com.fasterxml.**
-dontnote org.codehaus.**

# Internal DTOs
#-keep class com.nextraq.WorkerConnect.** {*;}
#-dontnote com.nextraq.WorkerConnect.**
-keep class com.rdm.android.learningwithnationalparks.networkFlickr.** {*;}
-keep class com.rdm.android.learningwithnationalparks.networkLessons.** {*;}
-keep class com.rdm.android.learningwithnationalparks.data.** {*;}
