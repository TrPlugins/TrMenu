package trplugins.menu.util

class Cooldown {
    var plugin: String
    val packName: String
    val packSeconds: Long
    private val data = HashMap<String, Long>()

    constructor(n: String, s: Int) {
        packName = n
        packSeconds = s.toLong()
        plugin = "null"
    }

    constructor(n: String, s: Long) {
        packName = n
        packSeconds = s
        plugin = "null"
    }

    fun unRegister(player: String) {
        data.remove(player)
    }

    fun getCooldown(player: String): Long {
        return this.getCooldown(player, 0L)
    }

    fun getCooldown(player: String, cutSeconds: Long): Long {
        return if (!data.containsKey(player)) {
            0L
        } else {
            val difference = System.currentTimeMillis() + cutSeconds - data[player]!!
            if (difference >= packSeconds) 0L else packSeconds - difference
        }
    }

    fun isCooldown(player: String): Boolean {
        return this.isCooldown(player, 0L)
    }

    fun isCooldown(player: String, cutSeconds: Long): Boolean {
        return if (!data.containsKey(player)) {
            data[player] = System.currentTimeMillis()
            false
        } else if (this.getCooldown(player, cutSeconds) <= 0L) {
            data[player] = System.currentTimeMillis()
            false
        } else {
            true
        }
    }

    fun reset(player: String) {
        data.remove(player)
    }

}