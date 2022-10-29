rootProject.name = "TrMenu"

include("plugin")

file("project").listFiles()!!.filter { it.isDirectory }.forEach {
    include("project:${it.name}")
}
include("project:module-invero-legacy")
findProject(":project:module-invero-legacy")?.name = "module-invero-legacy"
include("project:module-test")
findProject(":project:module-test")?.name = "module-test"
