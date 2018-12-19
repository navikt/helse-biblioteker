plugins {
    id("io.codearte.nexus-staging") version "0.12.0"
}

// version is replaced by travis
val libVersion = "1"

nexusStaging {
    username = System.getenv("OSSRH_JIRA_USERNAME")
    password = System.getenv("OSSRH_JIRA_PASSWORD")
    packageGroup = "no.nav"
}

subprojects {
    group = "no.nav.helse"
    version = libVersion
}

tasks.withType<Wrapper> {
    gradleVersion = "5.0"
}
