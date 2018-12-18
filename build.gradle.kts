tasks {
    register("assemble") {
        dependsOn(gradle.includedBuild("sts-klient").task(":assemble"))
        group = "Application"
        description = "Runs the :assemble task"
    }

    register("clean") {
        dependsOn(gradle.includedBuild("sts-klient").task(":clean"))
        group = "Application"
        description = "Runs the :clean task"
    }

    register("check") {
        dependsOn(gradle.includedBuild("sts-klient").task(":check"))
        group = "Application"
        description = "Runs the :check task"
    }

    register("build") {
        dependsOn(gradle.includedBuild("sts-klient").task(":build"))
        group = "Application"
        description = "Runs the :build task"
    }

    register("publish") {
        dependsOn(gradle.includedBuild("sts-klient").task(":publish"))
        group = "Application"
        description = "Runs the :publish task"
    }

    register("closeAndReleaseRepository") {
        dependsOn(gradle.includedBuild("sts-klient").task(":closeAndReleaseRepository"))
        group = "Application"
        description = "Runs the :closeAndReleaseRepository task"
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "5.0"
}
