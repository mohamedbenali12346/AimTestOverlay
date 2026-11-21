AimTest Overlay Android project

This is a visual-only floating overlay demo.
- Floating arrow button can be dragged.
- Tapping it animates a visual marker upward by 150 Android view pixels over 200 ms.
- It does not inject touch events, control aim, or fire in games/apps.

Build:
Open this folder in Android Studio, sync Gradle, then Build > Build APK(s).
Install app-debug.apk.
On the phone, open AimTest Overlay, grant "Display over other apps", then tap "Start floating button".
Tap floating arrow to see the visual movement. Stop from the app's Stop button.

Note: Android overlay permissions and behavior may vary by device/vendor.
