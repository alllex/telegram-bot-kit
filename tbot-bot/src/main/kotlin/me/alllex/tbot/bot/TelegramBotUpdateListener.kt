package me.alllex.tbot.api.client

import me.alllex.tbot.api.model.Update


interface TelegramBotUpdateListener {

    fun onUpdate(update: Update)

}
