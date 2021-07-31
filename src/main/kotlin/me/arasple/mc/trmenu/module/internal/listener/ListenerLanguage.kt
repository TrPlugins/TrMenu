package me.arasple.mc.trmenu.module.internal.listener

import me.arasple.mc.trmenu.TrMenu
import taboolib.common.platform.SubscribeEvent
import taboolib.module.lang.event.PlayerSelectLocaleEvent

object ListenerLanguage {

    @SubscribeEvent
    fun e(e: PlayerSelectLocaleEvent) {
        e.locale = TrMenu.SETTINGS.getString("Options.Language")
    }

}