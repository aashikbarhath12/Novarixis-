# Nebular

**Powered by Blue Moon** · by Novarixis
*A Brighter Mind for a Bigger Tomorrow*

An Android AI-assistant app built with Kotlin + Jetpack Compose + Material 3,
matching the reference UI/UX (dark and light themes, splash, onboarding, home,
chat with model selector, smart slide menu, settings).

## Start here

👉 **Read `SETUP.md`** for how to open this in Android Studio and run it.
There's one small manual step (regenerating the Gradle wrapper jar) that
Android Studio handles automatically on first open.

## Quick facts

- **Language/UI**: Kotlin, Jetpack Compose, Material 3
- **Architecture**: MVVM + Clean Architecture, Hilt DI, Room, DataStore, Retrofit
- **Min SDK**: 24 (Android 7.0) · **Target SDK**: 34
- **Chat works immediately** in demo mode — no backend or API key required to
  see the full streaming chat UI in action. See `SETUP.md` for wiring a real
  AI backend later.
- **Dark/Light toggle** in Settings is live and persisted (DataStore), driving
  the whole app's theme.
- Custom-drawn nebula/orb graphics (Compose Canvas) match the reference art,
  no placeholder boxes.

## Project layout

```
app/src/main/kotlin/com/novarixis/nebular/
├── core/ui/theme/        Design tokens: colors, type, spacing, theme
├── core/ui/graphics/     Nebula logo mark + glowing orb (Canvas-drawn)
├── core/ui/components/   Reusable buttons, cards, text fields, chat bubbles
├── domain/model/         AIModel, User, Chat data classes
├── domain/service/       Service interfaces (AIService, ChatRepository)
├── data/db/              Room entities, DAOs, database
├── data/network/         Retrofit client, secure key placeholder
├── data/service/         AIService + ChatRepository implementations
├── feature/splash/       Animated splash screen
├── feature/onboarding/   4-screen onboarding pager
├── feature/home/         Home dashboard with feature cards
├── feature/chat/         Chat screen + model selector bottom sheet
├── feature/menu/         Smart slide-out navigation menu
├── feature/settings/     Settings screen (theme, language, notifications)
└── di/                   Hilt dependency injection module
```

## Why no .apk file

Building an installable `.apk` requires compiling with the Android SDK and
Gradle's Android plugin — this project was authored in an environment without
that toolchain available, so it ships as source you build yourself (Android
Studio does this in a few clicks — see `SETUP.md`). Once opened and synced,
**Run ▶** in Android Studio produces and installs the APK on your device or
emulator directly.
