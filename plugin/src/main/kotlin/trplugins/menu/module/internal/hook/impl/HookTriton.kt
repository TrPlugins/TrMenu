package trplugins.menu.module.internal.hook.impl

import com.rexcantor64.triton.Triton
import com.rexcantor64.triton.api.players.LanguagePlayer
import org.bukkit.entity.Player
import trplugins.menu.module.internal.hook.HookAbstract

/**
 * @author Rubenicos
 * @date 2022/06/01 09:18
 */
class HookTriton : HookAbstract() {

    private val triton: Triton? = if (isHooked) Triton.get() else null
        get() {
            if (field == null) reportAbuse()
            return field
        }

    fun getText(player: Player, code: String, args: Array<String>): String? {
        val langPlayer: LanguagePlayer = triton?.playerManager?.get(player.uniqueId) ?: return null
        return triton?.languageManager?.getText(langPlayer, code, *args)
    }

    override fun getPluginName(): String {
        return "Triton"
    }
}