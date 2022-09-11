rootProject.name = "TrMenu"

include("plugin")

file("project").listFiles()!!.filter { it.isDirectory }.forEach {
    include("project:${it.name}")
}