subprojects {
    gradle.buildFinished {
        buildDir.deleteRecursively()
    }
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}