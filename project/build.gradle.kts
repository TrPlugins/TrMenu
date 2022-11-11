subprojects {
    dependencies {
        compileOnly("ink.ptms:nms-all:1.0.0")
        compileOnly("ink.ptms.core:v11902:11902-minimize:mapped")
        compileOnly("ink.ptms.core:v11902:11902-minimize:universal")
        compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    }
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}