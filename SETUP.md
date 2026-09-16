# Opening Nebular in Android Studio

## One manual step required: regenerate the Gradle wrapper jar

This project includes `gradlew`, `gradlew.bat`, and `gradle/wrapper/gradle-wrapper.properties`,
but **not** the binary `gradle-wrapper.jar` (binaries can't be authored as plain text).
Android Studio fixes this automatically the first time you open the project:

1. Open Android Studio → **Open** → select the unzipped `Nebular` folder.
2. If Studio asks to "Sync" or shows a Gradle wrapper warning, click **OK / Sync Now**.
   Studio will detect the missing jar and offer to regenerate it, or you can run once
   from a terminal in the project root (with Gradle installed globally, or via Studio's
   bundled Gradle):
   ```
   gradle wrapper --gradle-version 8.6
   ```
   This writes the missing `gradle/wrapper/gradle-wrapper.jar` and you won't need to
   do it again.
3. After that, `./gradlew build` and the normal **Run ▶** button in Android Studio work
   as expected.

## Build & Run
1. File → Open → select this project's root folder.
2. Let Gradle sync finish (first sync downloads dependencies — needs internet).
3. Select the `app` run configuration and a device/emulator running **API 24+**.
4. Click **Run ▶**.

## What's demo-ready out of the box
- Full navigation: Splash → Onboarding → Home → Chat → Settings, plus the slide menu
  and model selector.
- Chat works immediately in **demo mode**: no API key is required. `AIServiceImpl`
  detects the missing key and streams a simulated response so you can see the full
  streaming UI, typing indicator, and message bubbles working end-to-end.
- Dark/Light mode toggle in Settings is wired to DataStore and actually changes the
  whole app's theme live.
- Chat history persists locally via Room.

## Connecting a real AI backend
1. Open `data/network/ApiClient.kt` and set `BASE_URL` to your real endpoint.
2. Call `SecureKeyManager.setApiKey("...")` somewhere early (e.g. after login), ideally
   reading the key from Android Keystore / EncryptedSharedPreferences rather than a
   literal string.
3. `AIServiceImpl.streamMessage` will then use the real backend path instead of demo mode.
   Replace the placeholder word-by-word chunking with real SSE/WebSocket streaming from
   your provider for token-accurate output.

## Known simplifications (intentional, for a clean starting point)
- No image generation, file upload/analysis, or code-execution sandbox yet — the screens
  described in the original spec (Create, Explore, Blue Code, Agents, Library, History,
  Subscription) are represented as Home feature-card entry points; wiring their detail
  screens is the natural next step and follows the same patterns as Chat/Settings.
- Payment (`PaymentService`) is intentionally not implemented — plug in Google Play
  Billing per Play Store policy before shipping anything with real payments.
- Launcher icon is a hand-drawn vector matching the nebula mark, not a designer asset.
  Swap `res/drawable/ic_launcher_foreground.xml` and `ic_launcher_background.xml` (or the
  mipmap PNGs) with real artwork whenever you have it — Android Studio's Image Asset
  Studio (right-click `res` → New → Image Asset) is the easiest way to regenerate all
  densities from a single source image.
