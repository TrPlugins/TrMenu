dependencies {
    tabooModule("common")
    tabooModule("platform-bukkit")
    tabooModule("module-nms")

    api(project(":project:implementation-bukkit"))
    api(project(":project:module-invero-impl"))
    api(project(":project:module-invero-legacy"))
}