package me.arasple.mc.trmenu.modules.function.hook.impl

import me.arasple.mc.trmenu.modules.function.hook.HookInstance
import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.context.ContextManager
import net.luckperms.api.model.group.Group
import net.luckperms.api.model.group.GroupManager
import net.luckperms.api.model.user.User
import net.luckperms.api.model.user.UserManager
import net.luckperms.api.track.TrackManager


/**
 * @author Arasple
 * @date 2020/8/27 19:17
 * var hook = utils.getLuckPerms()
 */
class HookLuckPerms : HookInstance() {

    lateinit var api: LuckPerms

    override fun getDepend(): String {
        return "LuckPerms"
    }

    override fun initialization() {
        api = LuckPermsProvider.get()
    }

    fun getGroup(name: String): Group? {
        return getGroupManager().getGroup(name)
    }

    fun getUser(name: String): User? {
        return getUserManager().getUser(name)
    }

    fun getGroupManager(): GroupManager {
        return api.groupManager
    }

    fun getUserManager(): UserManager {
        return api.userManager
    }

    fun getTrackManager(): TrackManager {
        return api.trackManager
    }

    fun getContextManager(): ContextManager {
        return api.contextManager
    }

    fun listGroups(): List<String> {
        return api.groupManager.loadedGroups.map { it.name }.sorted()
    }

}