dependencies {
    tabooModule("common")
    tabooModule("common-5")
    tabooModule("module-configuration")
    tabooModule("module-kether")

    tabooModule("platform-bukkit")

    api(project(":project:common-outside"))
    api(project(":project:module-invero-impl"))
}