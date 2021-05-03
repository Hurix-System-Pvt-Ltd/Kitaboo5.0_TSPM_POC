# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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
-keep class com.hurix.reader.kitaboosdkrenderer.PlayerActivity{ *;}

-keep class com.hurix.reader.kitaboosdkrenderer.AudioBookActivity{ *;}
-keep class com.hurix.reader.kitaboosdkrenderer.AudioMediaPlayer{ *;}
-keep class com.hurix.reader.kitaboosdkrenderer.HtmlActivityPlayer{ *;}
-keep class com.hurix.reader.kitaboosdkrenderer.VideoMediaPlayer{ *;}
-keep class com.hurix.reader.kitaboosdkrenderer.sdkRenderer.ServiceHandler{ *;}
-keep class com.hurix.reader.kitaboosdkrenderer.views.CustomPlayerUIConstants{ *;}
-keep class com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager{ *;}
-keep class com.hurix.reader.kitaboosdkrenderer.customviews.WorldbookSearchItemPOJO{ *;}
-keep class com.hurix.reader.kitaboosdkrenderer.customviews.CustomExpandableList{ *;}
-keep class com.hurix.reader.kitaboosdkrenderer.customviews.CustomLTPMTOBViewAdapter{ *;}
-keep class com.hurix.reader.kitaboosdkrenderer.customviews.CustomTOCEnterpriseView{ *;}
-keep class com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo{ *;}
-keep class com.hurix.reader.kitaboosdkrenderer.ServiceHandler{ *;}
-keep class com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.ReaderThemeController{ *;}


