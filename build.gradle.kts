plugins {
	java
	`java-library`
}

subprojects {
	val libs = rootProject.libs

	apply(plugin = "java")
	apply(plugin = "java-library")

	repositories {
		mavenCentral()
		maven("https://oss.sonatype.org/content/repositories/snapshots/")
	}

	tasks {
		withType<JavaCompile>().configureEach {
			options.run {
				encoding = Charsets.UTF_8.name()
				compilerArgs.addAll(listOf(
					"-Xlint:all,-serial,-processing,-classfile",
					"--add-exports=java.base/jdk.internal.misc=ALL-UNNAMED",
					"--add-exports=java.base/jdk.internal=ALL-UNNAMED" // for @ValueBased
				))
			}
		}
		withType<Javadoc>().configureEach {
			(options as StandardJavadocDocletOptions).apply {
				encoding = Charsets.UTF_8.name()
				addStringOption("Xdoclint:none", "-quiet")
				addMultilineStringsOption("-add-exports").value = listOf(
					"java.base/jdk.internal.misc=ALL-UNNAMED"
				)
			}
		}
	}

	testing {
		suites {
			val test by getting(JvmTestSuite::class) {
				useJUnitJupiter(libs.versions.junit.get())

				dependencies {
					implementation(libs.junit.vintage.engine)
					implementation(libs.hamcrest)
				}
			}
		}
	}
}
