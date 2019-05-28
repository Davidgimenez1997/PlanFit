# hide the original source file name.
#-renamesourcefileattribute SourceFile

#ButterKnife

# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

#Recycleview
-keep class android.support.v7.widget.RecyclerView { *; }

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#Modelos
#-keep class com.utad.david.planfit.Model.** { *; }
#-keep class com.utad.david.planfit.** {*;}

# Basic ProGuard rules for Firebase Android SDK 2.0.0+
-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }

#Firebase Auth
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

-keep class com.google.firebase.quickstart.database.java.viewholder.** {
    *;
}

-keepclassmembers class com.google.firebase.quickstart.database.java.models.** {
    *;
}

