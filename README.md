# Telegram Bot API in Kotlin

[![Maven Central](https://img.shields.io/maven-central/v/me.alllex.telegram.botkit/tbot-api-jvm.svg?color=success)](https://central.sonatype.com/namespace/me.alllex.telegram.botkit)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Gradle build](https://github.com/alllex/telegram-bot-kit/actions/workflows/check.yml/badge.svg)](https://github.com/alllex/telegram-bot-kit/actions/workflows/check.yml)

Idiomatic, fluent and type-safe Kotlin bindings for [Telegram Bot API](https://core.telegram.org/bots/api).

```kotlin
val poller = TelegramBotApiPoller(TelegramBotApiClient(botApiToken))
poller.start(TelegramBotUpdateListener(
    onMessage = { message ->
        message.reply(
            text = "Hello, *${message.from?.firstName ?: "stranger"}*!",
            parseMode = ParseMode.MARKDOWN,
            replyMarkup = inlineKeyboard {
                buttonLink("Telegram", "https://telegram.org")
                row {
                    button("Bot", "bot")
                    button("API", "api")
                }
            }
        )
    },
    onCallbackQuery = { callbackQuery ->
        when (callbackQuery.data) {
            "bot" -> callbackQuery.answer("🤖")
            "api" -> callbackQuery.answer("🚀")
            else -> callbackQuery.answer("🤷")
        }
    }
))
```

## Usage

<details open>
<summary>Using with Gradle for JVM projects</summary>

```kotlin
dependencies {
    implementation("me.alllex.telegram.botkit:tbot-api-jvm:0.4.0")
}
```

</details>

<details>
<summary>Using with Maven for JVM projects</summary>

```xml
<dependency>
  <groupId>me.alllex.telegram.botkit</groupId>
  <artifactId>tbot-api-jvm</artifactId>
  <version>0.4.0</version>
</dependency>
```

</details>

## Compatibility

The bindings are generated directly from the source-of-truth [Bot API spec](https://core.telegram.org/bots/api).

| Telegram Bot API | tbot-api library |
|------------------|------------------|
| `7.3`            | `0.5.0`          |
| `6.9`            | `0.4.0`          |

| tbot-api library | Requirement               |
|------------------|---------------------------|
| `0.4.0`+         | Kotlin `1.9.0+`, JVM `8+` |


## Developing

### Updating API to a new version of Telegram Bot API

```
./gradlew :buildSrc:updateApiSpec --no-configuration-cache
./gradlew generateTelegramBotApi
./gradlew apiDump
./gradlew check
```

## Roadmap

### Alpha

- [ ] Error handling

### Beta

- [ ] Cleaner package structure
- [ ] Strict library member visibility
- [ ] Forward compatibility: published versions of the library should not break with new API versions (union types and enums)
- [ ] Support for integration tests bots

## License

Distributed under the MIT License. See `LICENSE` for more information.
