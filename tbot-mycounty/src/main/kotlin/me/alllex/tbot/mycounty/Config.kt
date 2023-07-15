package me.alllex.tbot.mycounty

data class Config(
    val api: ApiConfig,
    val db: DbConfig
)

data class ApiConfig(
    val host: String,
    val username: String,
    val token: String
)

data class DbConfig(
    val url: String
)
