rootProject.name = "TrMenu"

include("api", "plugin")

file("project").listFiles()!!.filter { it.isDirectory }.forEach {
    include("project:${it.name}")
}