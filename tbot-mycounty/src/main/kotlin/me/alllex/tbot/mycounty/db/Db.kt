package me.alllex.tbot.mycounty.db

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import me.alllex.tbot.mycounty.util.newSingleThreadExecutor
import me.alllex.tbot.mycounty.util.shutdownAndAwaitTermination
import org.sqlite.SQLiteConfig
import java.sql.Connection
import java.sql.DriverManager


class Db(
    private val dbUrl: String
) {

    private val executor = newSingleThreadExecutor("db")
    private val coDispatcher = executor.asCoroutineDispatcher()

    private lateinit var conn: Connection

    val connection: Connection get() = conn

    fun start() {
        val config = SQLiteConfig().apply {
            // Otherwise, among other things, ON DELETE CASCADE has no effect.
            this.enforceForeignKeys(true)
        }
        conn = DriverManager.getConnection(dbUrl, config.toProperties())
        conn.isValid(10)
        ensureSchema()
    }

    fun stop() {
        executor.shutdownAndAwaitTermination()
        if (::conn.isInitialized) conn.close()
    }

    suspend fun <T> run(block: suspend CoroutineScope.() -> T): T {
        return withContext(coDispatcher, block)
    }

    private fun ensureSchema() {
        conn.createStatement().use { stmt ->
            stmt.execute(
                """
                create table if not exists users (
                    id integer primary key,
                    tg_chat_id integer not null unique,
                    timezone text not null default 'UTC'
                )
            """.trimIndent()
            )

            stmt.execute(
                """
                create table if not exists counters (
                    id integer primary key,
                    name text not null,
                    user_id integer references users on delete cascade
                )
            """.trimIndent()
            )

            stmt.execute(
                """
                create table if not exists counter_updates (
                    id integer primary key,
                    timestamp text not null,
                    counter_id integer references counters on delete cascade,
                    diff integer not null
                )
            """.trimIndent()
            )
        }
    }

    override fun toString(): String = "Db($dbUrl)"
}
