# VWO Mobile Insights Android Demo App

A demo Android application showcasing the integration and capabilities of VWO Insights SDK for mobile analytics and session recording.

## 📱 About

This demo app demonstrates how to integrate VWO Insights into an Android application. It includes sample screens for e-commerce flows (product browsing, cart management) and housing listings to showcase session recording and analytics capabilities.

## ✨ Features

- **Session Recording:** Capture user interactions and replay sessions
- **Custom Events:** Track specific user actions and events
- **Screen Tracking:** Automatic screen view tracking
- **Custom Variables:** Tag sessions with custom metadata
- **QR Code Integration:** Quick setup via QR code scanning

## 🚀 Getting Started

### Prerequisites

- **Android Studio:** Latest version (Ladybug or later recommended)
- **JDK (for Gradle sync):** Version 17 recommended (Java 21 is not compatible with Gradle 8.0 in this project)
- **Gradle Wrapper:** 8.0 (configured via `gradle/wrapper/gradle-wrapper.properties`)
- **Minimum Android SDK:** API 21 (Android 5.0)
- **Target Android SDK:** API 35 (Android 16) or latest available
- **VWO Insights Account:** [Sign up at VWO](https://vwo.com)

### Setup Instructions

1. **Clone the repository**

   ```bash
   git clone https://github.com/wingify/vwo-insights-mobile-android-demo.git
   cd vwo-insights-mobile-android-demo
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned repository folder
   - Click "OK"

3. **Sync Gradle files**
   - Android Studio will automatically start syncing
   - Wait for the sync to complete
   - If prompted, accept any Gradle plugin updates

4. **Build and Run**
   - Connect an Android device or start an emulator
   - Click the "Run" button (or press Shift+F10)
   - The app will install and launch

### 🔑 VWO Mobile Insights Credentials Setup

**This app requires VWO Insights credentials to function. No credentials are hardcoded in the repository.**

#### Option 1: In-App Manual Entry

1. Launch the app
2. On first launch, you'll see a login screen
3. Tap "Enter API Key & Account ID"
4. Enter your credentials:
   - **API Key:** Your 32-character VWO Insights API key
   - **Account ID:** Your VWO account ID

#### Option 2: QR Code Scanning (Recommended)

1. Generate a QR code containing: `{API_KEY},{ACCOUNT_ID}`
2. In the app, tap the QR scanner icon
3. Scan your credentials QR code
4. The app will automatically populate your credentials

#### Getting Your Credentials

1. Sign up or log in at [https://vwo.com](https://vwo.com)
2. Navigate to **Mobile Insights** section
3. Create a new Android app project or select existing
4. Copy your **API Key** and **Account ID** from the project settings
5. Enter them in the app or generate a QR code

**Note:** The app will remember your credentials locally. You only need to enter them once.

## 🏗️ Project Structure

```text
vwo-insights-mobile-android-demo/
├── demo/                          # Demo application module
│   ├── src/main/
│   │   ├── java/com/vwo/sampleapp/
│   │   │   ├── activities/        # Activity classes
│   │   │   ├── adapters/          # RecyclerView adapters
│   │   │   ├── app/               # Application class
│   │   │   ├── fragments/         # Fragment classes
│   │   │   ├── models/            # Data models
│   │   │   └── utils/             # Utility classes
│   │   └── res/                   # Resources (layouts, drawables, etc.)
│   └── build.gradle               # Demo app build configuration
├── library/                       # VWO SDK library module
│   └── build.gradle               # Library build configuration
└── build.gradle                   # Project-level build configuration
```

## 📚 Demo App Sections

### 1. **Mobile Shopping**

- Browse mobile phones
- Add items to cart and favorites
- Product details with ratings
- Shopping cart management

### 2. **Housing Listings**

- Browse property listings
- Filter and sort properties
- View property details
- Contact property owners

### 3. **Session Recording**

- Start/stop recording sessions
- View session URLs
- Test custom events and variables

## 🔧 Technical Details

### Dependencies

The app uses the following key dependencies:

```gradle
// VWO Insights SDK
implementation 'com.vwo:insights:0.5.0'

// AndroidX Libraries
implementation 'androidx.appcompat:appcompat:1.3.1'
implementation 'androidx.constraintlayout:constraintlayout:1.1.2'
implementation 'androidx.lifecycle:lifecycle-extensions:2.0.0'
implementation 'androidx.room:room-runtime:2.0.0'

// Other Libraries
implementation 'com.jakewharton.timber:timber:4.7.1'
implementation 'com.github.bumptech.glide:glide:4.7.1'
implementation 'com.github.yuriy-budiyev:code-scanner:2.3.2'
```

### Permissions

Required permissions in `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.CAMERA"/> <!-- For QR scanning -->
```

## 🔒 Security & Privacy

- **No hardcoded credentials:** All credentials are provided by users
- **No Firebase required:** This demo does not use Firebase or Crashlytics
- **Local storage only:** Credentials are stored locally on device using SharedPreferences
- **No data collection:** This is a demo app; no user data is sent to third parties

## 🛠️ Building for Production

To build a release version:

1. Generate your signing keystore
2. Configure signing in `gradle.properties`:

   ```properties
   ALIAS=your_key_alias
   KEY_PASSWORD=your_key_password
   KEYSTORE_PASSWORD=your_keystore_password
   RELATIVE_PATH=/path/to/your.keystore
   ```

3. Build release APK:

   ```bash
   ./gradlew assembleRelease
   ```

## 📖 Additional Resources

- [VWO Insights Documentation](https://developers.vwo.com/docs/mobile-insights)
- [VWO Knowledge Base](https://help.vwo.com/)
- [Android Developer Guide](https://developer.android.com/guide)

## ⚙️ Troubleshooting

### Build Issues

**Problem:** Gradle sync fails

```text
Solution: File → Invalidate Caches → Invalidate and Restart
```

**Problem:** Incompatible Java and Gradle versions (for example, Java 21 + Gradle 8.0)

```text
Solution: Set Gradle JDK to Java 17 in Android Studio:
Settings → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK
```

**Problem:** SDK version mismatch

```text
Solution: Update compileSdk and targetSdk in build.gradle to match your installed SDK
```

### Runtime Issues

**Problem:** App crashes on launch

```text
Solution: Check if you've entered valid VWO credentials. Clear app data and re-enter.
```

**Problem:** Sessions not recording

```text
Solution: Ensure you have internet connectivity and valid VWO credentials.
```

## 📞 Support

- **Issues:** [GitHub Issues](https://github.com/wingify/vwo-insights-mobile-android-demo/issues)
- **Email:** `support@vwo.com`
- **Website:** [https://vwo.com](https://vwo.com)

## 🤝 Contributing

We value your contributions! Please check our [contributing guidelines](https://github.com/wingify/vwo-insights-mobile-android-demo/blob/main/CONTRIBUTING.md) before submitting a PR.

## 📜 Code of Conduct

We maintain a [Code of Conduct](https://github.com/wingify/vwo-insights-mobile-android-demo/blob/main/CODE_OF_CONDUCT.md) to ensure a welcoming environment for all contributors.

## 📄 License

[Apache License, Version 2.0](https://github.com/wingify/vwo-insights-mobile-android-demo/blob/main/LICENSE)

Copyright 2025 Wingify Software Pvt. Ltd.
