# Telegram Bot API in Kotlin



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
- [ ] Move required fields before optional

### Beta

- [ ] Cleaner package structure
- [ ] Strict library member visibility
- [ ] Public API validation
- [ ] Forward compatibility: published versions of the library should not break with new API versions (union types and enums)
- [ ] Support for integration tests bots
