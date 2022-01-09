package trplugins.menu.module.internal.hook.impl

import ink.ptms.zaphkiel.ZaphkielAPI
import trplugins.menu.module.internal.hook.HookAbstract
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial
import taboolib.platform.util.buildItem

class HookZaphkiel: HookAbstract() {

    private val empty = buildItem(XMaterial.BEDROCK) { name = "UNHOOKED_${super.name.uppercase()}" }

    private val notFound = buildItem(XMaterial.BEDROCK) { name = "NOT_FOUND_${super.name.uppercase()}" }

    fun getItem(id: String, player: Player? = null): ItemStack {
        if (checkHooked()) {
            return ZaphkielAPI.getItemStack(id, player) ?: notFound
        }
        return empty
    }

}