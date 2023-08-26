plugins {
    `gradle-enterprise`
}

rootProject.name = "telegram-bot-kit"

include("tbot-api")

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlwaysIf(System.getenv("CI") == "true")
    }
}
