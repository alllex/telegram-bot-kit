# Telegram Bot API in Kotlin






## Developing

### Updating the API spec

```
./gradlew :buildSrc:updateApiSpec
```

## Roadmap

### Alpha

- [ ] Move required fields before optional

### Beta

- [ ] Cleaner package structure
- [ ] Strict library member visibility
- [ ] Public API validation
- [ ] Forward compatibility: published versions of the library should not break with new API versions (union types and enums)
- [ ] Support for integration tests bots
