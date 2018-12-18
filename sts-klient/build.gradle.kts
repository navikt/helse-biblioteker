import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val fuelVersion = "1.15.1"
val orgJsonVersion = "20180813"
val wireMockVersion = "2.19.0"

val junitJupiterVersion = "5.3.1"

group = "no.nav.helse"
val artifact = "sts-klient"
// version is placed by travis
version = 1
val artifactDescription = "Libraries for Helse"
val repoUrl = "https://github.com/navikt/helse-biblioteker.git"
val scmUrl = "scm:git:https://github.com/navikt/helse-biblioteker.git"

plugins {
   kotlin("jvm") version "1.3.11"
   `java-library`
   `maven-publish`
   signing
   id("org.jetbrains.dokka") version "0.9.17"
   id("io.codearte.nexus-staging") version "0.12.0"
}

buildscript {
   dependencies {
      classpath("org.junit.platform:junit-platform-gradle-plugin:1.2.0")
   }
}

dependencies {
   implementation(kotlin("stdlib"))

   compile("org.json:json:$orgJsonVersion")
   compile("com.github.kittinunf.fuel:fuel:$fuelVersion")

   testCompile("com.github.tomakehurst:wiremock:$wireMockVersion")

   testCompile("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
   testCompile("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
   testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
}

repositories {
   jcenter()
   mavenCentral()
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

publishing {
   publications {
      register("mavenJava", MavenPublication::class) {
         from(components["java"])
         artifact(sourcesJar.get())
         artifact(javadocJar.get())

         artifactId = artifact

         pom {
            name.set(artifact)
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

   repositories {
      maven {
         credentials {
            username = System.getenv("OSSRH_JIRA_USERNAME")
            password = System.getenv("OSSRH_JIRA_PASSWORD")
         }
         val version = "${project.version}"
         url = if (version.endsWith("-SNAPSHOT")) {
            uri("https://oss.sonatype.org/content/repositories/snapshots")
         } else {
            uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
         }
      }
   }
}

ext["signing.gnupg.keyName"] = System.getenv("GPG_KEY_NAME")
ext["signing.gnupg.passphrase"] = System.getenv("GPG_PASSPHRASE")
ext["signing.gnupg.useLegacyGpg"] = true

signing {
   useGpgCmd()
   sign(publishing.publications["mavenJava"])
}

nexusStaging {
   username = System.getenv("OSSRH_JIRA_USERNAME")
   password = System.getenv("OSSRH_JIRA_PASSWORD")
   packageGroup = "no.nav"
}
