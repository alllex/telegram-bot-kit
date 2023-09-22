# Telegram Bot API in Kotlin

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

## Compatibility

The bindings are generated directly from the source-of-truth [Bot API spec](https://core.telegram.org/bots/api).

| Telegram Bot API | tbot-api library |
|------------------|------------------|
| 6.9              | 0.3.0            |


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

- [ ] Squash history
- [ ] Error handling

### Beta

- [ ] Cleaner package structure
- [ ] Strict library member visibility
- [ ] Forward compatibility: published versions of the library should not break with new API versions (union types and enums)
- [ ] Support for integration tests bots
