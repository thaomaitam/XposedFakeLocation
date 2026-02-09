# LibXposed API 100 Keep Rules
-keep class * extends io.github.libxposed.api.XposedModule {
    public <init>(io.github.libxposed.api.XposedInterface, io.github.libxposed.api.XposedModuleInterface$ModuleLoadedParam);
}

# Keep all Hooker classes
-keep class * implements io.github.libxposed.api.XposedInterface$Hooker {
    public static void before(...);
    public static void after(...);
    public static ** before(...);
}

# Keep dummy annotations
-keep @interface io.github.libxposed.api.annotations.*
-keep @io.github.libxposed.api.annotations.XposedHooker class * {
    @io.github.libxposed.api.annotations.BeforeInvocation public static *;
    @io.github.libxposed.api.annotations.AfterInvocation public static *;
}

# Keep Bridge and Arch layers
-keep class com.noobexon.xposedfakelocation.xposed.bridge.** { *; }
-keep class com.noobexon.xposedfakelocation.xposed.arch.** { *; }
-keep class * extends com.noobexon.xposedfakelocation.xposed.arch.IHook { *; }

# Keep XposedProvider
-keep class io.github.libxposed.service.XposedProvider

# Keep HiddenApiBypass
-keep class org.lsposed.hiddenapibypass.** { *; }