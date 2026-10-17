# Imposter Game

A fun party word game inspired by Internet Culture

## What is this game?

Imposter Game is a multiplayer party game where players are assigned secret roles (Imposter or Citizen) and must use word clues to identify the imposter among them.

## Key Features

- Play with 2-10 friends locally
- Multiple game modes: Classic Imposter, Related Words, Quick Round, and Custom Games
- Create your own custom word packs with personalized words and hints
- Beautiful interface with light and dark themes
- Simple setup - just open the app and play!

## How to Play

1. Gather 2-10 players with Android devices
2. Everyone joins the same game session
3. Players are secretly assigned roles (most are Citizens, 1-2 are Imposters)
4. Citizens see a secret word, Imposters see a different word
5. Players take turns giving one-word clues about their word
6. Use the clues to figure out who the Imposter is!
7. Vote to eject the suspected Imposter
8. Reveal the roles and see if you guessed right!

## Custom Word Packs

Create your own word packs:
- Go to Settings → Word Packs → Create Pack
- Enter a pack name and description
- Add word pairs: Citizen Word | Imposter Word | Hint (optional)
- Example: "Apple|Fruit|Red and crunchy"
- Use your custom packs in any game!

## Game Modes

- **Classic Imposter**: Imposters see no word, must bluff
- **Related Words**: Both see related words (e.g., "Doctor" vs "Nurse")
- **Quick Round**: Fast-paced single round games
- **Custom Game**: Use your personalized word packs

## Download

Download the latest Android APK from the project's [Releases](../../releases) page. Open the downloaded APK on your Android device and follow the install prompts. If prompted, allow your browser or file manager to install unknown apps.

## Build from Source

Install Android Studio with JDK 17 and Android SDK 34, then open this project and select **Build > Build APK(s)**. Or, with Gradle installed, run from the project root:

```sh
gradle :app:assembleDebug
```

The debug APK is created at `app/build/outputs/apk/debug/app-debug.apk`.

Enjoy playing with friends and family!