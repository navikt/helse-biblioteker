
tasks {
    register("assemble") {
        dependsOn(gradle.includedBuilds.map { it.task(":assemble") })
    }

    register("clean") {
        dependsOn(gradle.includedBuilds.map { it.task(":clean") })
    }

    register("check") {
        dependsOn(gradle.includedBuilds.map { it.task(":check") })
    }

    register("build") {
        dependsOn(gradle.includedBuilds.map { it.task(":build") })
    }

    register("publish") {
        dependsOn(gradle.includedBuilds.map { it.task(":publish") })
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "5.0"
}
