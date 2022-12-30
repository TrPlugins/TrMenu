rootProject.name = "TrMenu"

file("project").listFiles()!!.filter { it.isDirectory }.forEach {
    include("project:${it.name}")
}

include("api", "plugin")