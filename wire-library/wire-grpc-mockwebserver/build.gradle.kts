import ru.vyarus.gradle.plugin.animalsniffer.AnimalSnifferExtension

plugins {
  id("ru.vyarus.animalsniffer")
  kotlin("jvm")
}

val main by sourceSets.getting
configure<AnimalSnifferExtension> {
  sourceSets = listOf(main)
}

dependencies {
  implementation(project(":wire-runtime"))
  implementation(project(":wire-grpc-client"))
  implementation(libs.okio.core)
  api(deps.okhttp.mockwebserver)
}
