# Later Brain

**You saved it for a reason. We'll remind you when that reason arrives.**

Later Brain is an Android app for all the things you mean to come back to: a restaurant a friend mentioned, an article about sleep, a recipe for the weekend, an idea in the shower.

It isn't a bookmark app, and it isn't a read-later app. Those make you remember to go back and look. Later Brain does the remembering for you: it brings each thing back **at the moment it's useful**. Recipes on Saturday morning, the sleep article in the evening, the restaurant when you have a free night.

- Free. No ads.
- No account, no sign-up, no login.
- Everything stays on your phone. The app doesn't even have permission to use the internet.

---

## How to use it

### Saving something

**From any app (the main way):**
1. In Chrome, Google Maps, YouTube, or anywhere else, tap **Share**.
2. Pick **Save to Later Brain**.
3. Choose when you'd like a reminder: *Tonight*, *Weekend*, *When I'm nearby*, *Next time I'm free* or *Surprise me*.
4. Tap **Save**. You're taken straight back to what you were doing.

**Inside the app:**
Tap **Quick add** and type whatever you want to remember.

Later Brain guesses what kind of thing it is (restaurant, recipe, article, video and so on) from the website and the words you used. If it guesses wrong, just tap the right one.

### Your list

The home screen is your drawer of saved things:
- **Maybe today**: the one item that fits right now best.
- **Waiting for the right moment**: everything else you've saved.
- **Done**: things you've finished. Tap the arrow to bring one back.

Tap any item to open its link, mark it done, edit it or delete it.

---

## Your privacy

This is a core promise, not small print.

- **Nothing leaves your phone.** There's no server, no cloud and no account.
- **No tracking or analytics** of any kind.
- **No internet permission.** Android itself stops the app from sending anything anywhere.
- **No cloud backup.** Your saved items aren't copied to Google backup.
- Future features (calendar, places) will work the same way: everything is worked out on the phone and nothing is stored that isn't needed.

---

## What's built so far

| Stage | What it adds | Status |
|---|---|---|
| Phase 1 | Saving from the Share menu, quick add, your list, item details | ✅ Done |
| Phase 2 | Reminders (notifications) with Done / Later / Never again, quiet hours | Coming next |
| Phase 3 | Calendar awareness (spotting free evenings) and smarter timing | Planned |
| Phase 4 | Welcome screens, settings, privacy policy, Play Store release | Planned |
| v1.1 | "When I'm nearby" place reminders | Planned |

See [RELEASE_NOTES.md](RELEASE_NOTES.md) for what changed in each version.

---

## For developers

**Built with:** Kotlin, Jetpack Compose (Material 3), Room (the on-phone database), Navigation Compose. No dependency-injection library: a small `AppContainer` class sets things up.

**Requirements:** Android 8.0 (API 26) or newer. Targets Android 16 (API 36).

### Project layout

```
app/src/main/java/com/laterbrain/app/
├── LaterBrainApp.kt, AppContainer.kt   App start-up and shared objects
├── MainActivity.kt                    The main app and screen navigation
├── ShareActivity.kt                   The "Save to Later Brain" share screen
├── data/                              Database: saved items, status, reminder choice
├── capture/                           Reading shared links/text, guessing the category
├── domain/                            Picking the "Maybe today" item
└── ui/                                Screens: home, add/edit, detail, theme
```

### Building

There's no need to install Android Studio to get an APK. Every push to `main` is built by **GitHub Actions**, which:
1. runs the unit tests,
2. builds a signed **APK** (to install on your phone) and **AAB** (for the Play Store),
3. puts the latest APK on the [Releases page](../../releases/tag/latest).

To build locally you need Android Studio (or the Android SDK) and JDK 17. Then run `gradle wrapper --gradle-version 8.11.1` once, followed by `./gradlew assembleDebug`.

### Signing

Release builds are signed with a dedicated Later Brain keystore, separate from any other app. It's kept **outside** this repository and never committed. GitHub Actions gets it from these repository secrets: `KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`.

> ⚠️ If the keystore is lost, the app can never be updated on the Play Store. Keep a backup somewhere safe.

### Play Store

Everything for the Play Store listing (texts, form answers, closed-test steps, screenshot list) is in [store/PLAY_STORE_LISTING.md](store/PLAY_STORE_LISTING.md). The store icon and feature graphic are in `store/graphics/`; to redraw them, run `python store/make_graphics.py`.

The privacy policy is published from the `docs/` folder at https://ankitnitk.github.io/LaterBrain/privacy-policy.html

### Releasing a new version

```powershell
.\bump-version.ps1 1.0.1
```
This raises the version number and the internal build number. Then update `RELEASE_NOTES.md`, commit and push.
