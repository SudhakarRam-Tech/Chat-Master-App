# ChatMaster 💬

A real-time Android chat application built with **Jetpack Compose**, **Firebase**, and **MVVM Clean Architecture**.

---

## Screenshots

| Login / Sign Up | Chat List | Chat Screen |
|---|---|---|
| *(Login & register with email/password)* | *(List of all users)* | *(Real-time messaging + audio)* |

---

## Features

- 🔐 **Authentication** — Email & password login and registration via Firebase Auth
- 👥 **Chat User List** — Fetches all registered users from Firestore in real time
- 💬 **Real-time Messaging** — Send and receive text messages instantly using Firestore snapshot listeners
- 🔔 **Unread Badge** — Messages marked as read when the chat is opened
- 🎨 **Modern UI** — Clean WhatsApp-style chat bubbles built entirely in Jetpack Compose

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Dagger Hilt |
| Auth | Firebase Authentication |
| Database | Firebase Firestore |
| Storage | Firebase Storage (audio files) |
| Async | Kotlin Coroutines + Flow |
| Navigation | Jetpack Navigation Compose |

---

## Architecture

The project follows **Clean Architecture** with three distinct layers:

```
app/
├── core
│   └── util/
│       └── Resource.kt             # Generic Loading/Success/Error wrapper
│
├── data/
│   ├── datasource/
│   │   ├── AuthRemoteDataSource.kt
│   │   └── AuthRemoteDataSourceImpl.kt
│   ├── model/
│   │   ├── ChatUser.kt             # Firestore user document model
│   │   └── Message.kt             # Firestore message document model
│   └── repository/
│       ├── AuthRepositoryImpl.kt
│       ├── ChatRepositoryImpl.kt
│       └── ChatMessageRepositoryImpl.kt
│
├── di/
│   ├── AuthModule.kt               # Firebase Auth + Firestore + Storage providers
│   ├── ChatModule.kt               # Chat user list bindings
│   └── ChatMessageModule.kt        # Chat message bindings
│
├── domain/
│   ├── model/
│   │   └── User.kt
│   ├── repository/
│   │   ├── AuthRepository.kt
│   │   ├── ChatRepository.kt
│   │   └── ChatMessageRepository.kt
│   └── usecase/
│       ├── LoginUseCase.kt
│       ├── RegisterUseCase.kt
│       ├── GetChatUsersUseCase.kt
│       └── ChatMessageUseCases.kt  # GetMessages / SendText / SendAudio / MarkRead
│
├── navigation/
│   ├── Screen.kt                   # Route definitions
│   └── AppNavGraph.kt              # NavHost setup
│
└── ui/
    ├── Login/
    │   ├── LoginScreen.kt
    │   ├── AuthViewModel.kt
    │   └── AuthUiState.kt
    ├── chatList/
    │   ├── ChatListScreen.kt
    │   ├── ChatListViewModel.kt
    │   └── ChatListUiState.kt
    └── chat/
        ├── ChatScreen.kt
        ├── ChatViewModel.kt
        └── ChatUiState.kt
```

---

## Firestore Data Structure

### `chat_users` collection

Stores every registered user's profile. Written on successful registration.

```
chat_users/
  {uid}/
    uid          : "4BhapPXA17ZrMVL5BWndKmTMOps2"   (string)
    name         : "Sudhakar R"                        (string)
    email        : "sudhakar@gmail.com"                (string)
    password     : "sudhakar"                          (string)
    mobile       : "9876501234"                        (string)
    createdAt    : June 29, 2026 at 10:33:29 PM UTC   (timestamp)
```

### `chats` collection

Each chat document ID is built deterministically as `{smallerUid}_{largerUid}` so the same conversation ID is produced regardless of who initiates.

```
chats/
  {uid1_uid2}/
    messages/
      {messageId}/
        messageId     : "abc123"                       (string)
        senderId      : "uid_A"                        (string)
        receiverId    : "uid_B"                        (string)
        message       : "Hey! How are you?"            (string)
        messageType   : "text"                         (string) — "text" | "audio"
        audioUrl      : null                           (string?) — Firebase Storage URL
        audioDuration : null                           (string?) — e.g. "02:30"
        timestamp     : Timestamp                      (timestamp)
        isRead        : false                          (boolean)
```

## Firebase Setup

### 1. Create a Firebase project

1. Go to [Firebase Console](https://console.firebase.google.com)
2. Create a new project → **ChatMaster**
3. Register an Android app with package name `com.sk.chatmaster`
4. Download `google-services.json` → place it in `app/`

### 2. Enable Authentication

Firebase Console → **Authentication** → Sign-in method → **Email/Password** → Enable

### 3. Create Firestore Database

Firebase Console → **Firestore Database** → Create database → Start in **production mode**

#### Firestore Security Rules

```js
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {

    // Any signed-in user can read user list; only owner can write their own doc
    match /chat_users/{userId} {
      allow read:  if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }

    // Only the sender or receiver can read/write messages
    match /chats/{chatId}/messages/{messageId} {
      allow read, write: if request.auth != null
        && (request.auth.uid == resource.data.senderId
         || request.auth.uid == resource.data.receiverId);
    }
  }
}
```

### 4. Enable Firebase Storage

Firebase Console → **Storage** → Get started

#### Storage Security Rules

```js
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /chat_audio/{chatId}/{fileName} {
      allow read, write: if request.auth != null;
    }
  }
}
```

### 5. Add SHA-1 fingerprint

```bash
keytool -list -v \
  -keystore ~/.android/debug.keystore \
  -alias androiddebugkey \
  -storepass android \
  -keypass android
```

Copy the **SHA1** value → Firebase Console → Project Settings → Your Android app → **Add fingerprint**

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 17
- Android SDK 26+
- A Firebase project (see setup above)

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/yourusername/chatmaster.git
cd chatmaster

# 2. Place your google-services.json in the app/ directory

# 3. Open in Android Studio and sync Gradle

# 4. Run on emulator or device
```

> ⚠️ The emulator must use a **Google Play** system image (not just "Google APIs") for Firebase to work correctly.

---

## Gradle Dependencies

```kotlin
// app/build.gradle.kts

// Firebase BOM
implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
implementation("com.google.firebase:firebase-auth-ktx")
implementation("com.google.firebase:firebase-firestore-ktx")
implementation("com.google.firebase:firebase-storage-ktx")

// Hilt
implementation("com.google.dagger:hilt-android:2.51.1")
ksp("com.google.dagger:hilt-android-compiler:2.51.1")
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.7")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.2")
```

```kotlin
// build.gradle.kts (root)
id("com.google.dagger.hilt.android") version "2.51.1" apply false
id("com.google.devtools.ksp")        version "2.0.0-1.0.21" apply false
id("com.google.gms.google-services") version "4.4.2" apply false
```

---

## AndroidManifest Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />

<application
    android:name=".ChatMasterApp"
    android:windowSoftInputMode="adjustResize"
    ...>
```

---

## Audio Message Flow

```
User holds Mic button
    ↓
AudioRecorder.start()         → records to app cache as .mp3
Timer increments every second → shown as "Recording 00:12"
    ↓
User releases / taps Send
     ↓
ChatMessageRepositoryImpl     → writes Firestore message doc
                                 messageType: "audio"
                                 audioUrl: "https://firebasestorage..."
                                 audioDuration: "00:12"
    ↓
Real-time listener            → both users see the audio bubble instantly
```

---

## Known Limitations

- Passwords are stored as plain text in Firestore — Firebase Auth already secures credentials, so this field should be removed in production
- Audio playback UI is visual only — wire `ExoPlayer` or `MediaPlayer` to the play button in `AudioBubble`
- Push notifications (FCM) not yet implemented
- Image/file attachments not yet implemented

---

## License

```
MIT License — free to use, modify, and distribute.
```

---

## Author

**Sudhakar** — [GitHub](https://github.com/yourusername)
