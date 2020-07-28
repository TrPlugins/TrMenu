package me.arasple.mc.trmenu

import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.loader.PluginRedefine
import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.dependency.Dependency
import io.izzel.taboolib.module.inject.TInject

/**
 * @author Arasple
 * @date 2020/2/26 10:05
 */
@Plugin.Version(5.3)
@Dependency(maven = "org.kotlinlang:kotlin-stdlib:1.3.72", url = "http://repo.ptms.ink/repository/maven-releases/org/kotlinlang/kotlin-stdlib/1.3.72/kotlin-stdlib-1.3.72.jar;" + "https://skymc.oss-cn-shanghai.aliyuncs.com/libs/kotlin-stdlib-1.3.72-1.3.72.jar")
object TrMenu : PluginRedefine() {

    @TInject("settings.yml", locale = "Locale", migrate = true)
    lateinit var SETTINGS: TConfig

    @TInject(state = TInject.State.STARTING, init = "init", active = "active", cancel = "cancel")
    lateinit var LOADER: TrMenuLoader

}