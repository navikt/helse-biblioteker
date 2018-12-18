rootProject.name = "helse-biblioteker"

includeBuild("sts-klient") {
    dependencySubstitution {
        substitute(module("helse-biblioteker:sts-klient")).with(project(":"))
    }
}
includeBuild("aktørregister-klient")
