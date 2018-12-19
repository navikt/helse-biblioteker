import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.11"
    id("io.codearte.nexus-staging") version "0.12.0"
    id("de.marcphilipp.nexus-publish") version "0.1.1" apply false
    id("org.jetbrains.dokka") version "0.9.17" apply false
}

// version is replaced by travis
val libVersion = "1"

nexusStaging {
    username = System.getenv("OSSRH_JIRA_USERNAME")
    password = System.getenv("OSSRH_JIRA_PASSWORD")
    packageGroup = "no.nav"
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
    }
}

subprojects {
    group = "no.nav.helse"
    version = libVersion

    val junitJupiterVersion = "5.3.1"

    val artifactDescription = "Libraries for Helse"
    val repoUrl = "https://github.com/navikt/helse-biblioteker.git"
    val scmUrl = "scm:git:https://github.com/navikt/helse-biblioteker.git"

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java-library")
    apply(plugin = "signing")
    apply(plugin = "de.marcphilipp.nexus-publish")
    apply(plugin = "org.jetbrains.dokka")

    dependencies {
        implementation(kotlin("stdlib"))

        testCompile("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
        testCompile("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
        testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.named<KotlinCompile>("compileKotlin") {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.named<KotlinCompile>("compileTestKotlin") {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    tasks.withType<Wrapper> {
        gradleVersion = "5.0"
    }


    val dokka = tasks.withType<DokkaTask> {
        outputFormat = "html"
        outputDirectory = "$buildDir/javadoc"
    }

    val sourcesJar by tasks.registering(Jar::class) {
        classifier = "sources"
        from(sourceSets["main"].allSource)
    }

    val javadocJar by tasks.registering(Jar::class) {
        dependsOn(dokka)
        classifier = "javadoc"
        from(buildDir.resolve("javadoc"))
    }

    artifacts {
        add("archives", sourcesJar)
        add("archives", javadocJar)
    }

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                artifact(sourcesJar.get())
                artifact(javadocJar.get())

                pom {
                    name.set(project.name)
                    description.set(artifactDescription)
                    url.set(repoUrl)
                    withXml {
                        asNode().appendNode("packaging", "jar")
                    }
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                            distribution.set("repo")
                        }
                    }
                    developers {
                        developer {
                            organization.set("NAV (Arbeids- og velferdsdirektoratet) - The Norwegian Labour and Welfare Administration")
                            organizationUrl.set("https://www.nav.no")
                        }
                    }
                    scm {
                        connection.set(scmUrl)
                        developerConnection.set(scmUrl)
                        url.set(repoUrl)
                    }
                }
            }
        }
    }

    ext["signing.gnupg.keyName"] = System.getenv("GPG_KEY_NAME")
    ext["signing.gnupg.passphrase"] = System.getenv("GPG_PASSPHRASE")
    ext["signing.gnupg.useLegacyGpg"] = true

    configure<SigningExtension> {
        useGpgCmd()
        sign((extensions.getByName("publishing") as PublishingExtension).publications["mavenJava"])
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "5.0"
}
