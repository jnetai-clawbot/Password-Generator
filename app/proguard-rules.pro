# Add project specific ProGuard rules here.
-keepattributes *Annotation*

# Keep ViewBinding classes
-keep class com.passwordgenerator.app.databinding.** { *; }

# Keep serialization if used
-keepattributes Signature
