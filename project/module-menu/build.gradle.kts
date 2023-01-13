val taboolibVersion: String by project

dependencies {
    compileOnly(project(":project:common"))
    compileOnly("io.izzel.taboolib:common:$taboolibVersion")
    compileOnly("cc.trixey.invero:framework-common:1.0.0")
    compileOnly("cc.trixey.invero:framework-bukkit:1.0.0")
}