package cc.trixey.mc.trmenu.util

import cc.trixey.mc.trmenu.TrMenu.printWarning
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.util.Strings
import taboolib.common.util.Strings.similarDegree
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.library.xseries.XMaterial
import taboolib.platform.util.buildItem
import kotlin.jvm.optionals.getOrElse

/**
 * TrMenu
 * cc.trixey.mc.trmenu.util.ItemUtil
 *
 * @author Score2
 * @since 2023/01/01 23:06
 */

fun String.itemStack(): ItemStack {
    // 1.8-1.12
    if ("\\S+\\s*((:\\s*)\\d+)(\\s*)|\\d+".toRegex().matches(this)) {
        val split = split("", limit = 2).map { it.removeBlank() }
        val id = split[0]
        val data = split.getOrNull(1)?.toIntOrNull() ?: 0

        val mat: Material? = if ("\\d+".toRegex().matches(id)) {
            // pure number
            kotlin.runCatching {
                Material::class.java.invokeMethod<Material>(
                    "getMaterial",
                    id.toInt(),
                    isStatic = true
                )!!
            }.getOrElse {
                it.printWarning("parse material")
                XMaterial.matchXMaterial(id.toInt(), -1)
                    .getOrElse { XMaterial.STONE }
                    .parseMaterial()
            }

        } else {
            kotlin.runCatching {
                Material.getMaterial(id)
            }.getOrNull()
        }

        return buildItem(mat ?: id.material()) {
            damage = data
        }
    }

    return buildItem(this.material())
}

fun String.material(): Material {
    val material = XMaterial.values().find {
        it.name.equals(this, true) || it.legacy.any { it == this }
    } ?: XMaterial.values().maxByOrNull {
        similarDegree(this, it.name)
    }
    return material?.parseMaterial() ?: Material.STONE
}