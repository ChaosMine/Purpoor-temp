import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer

plugins {
	id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
	api(project(":bukkit:api"))
	api(libs.log4j.api)
	implementation(libs.log4j.core)
	annotationProcessor(libs.log4j.core)
	implementation(libs.log4j.iostreams)
	implementation(libs.log4j.slf4j.impl)
	runtimeOnly(libs.disruptor)
	implementation("com.google.code.gson:gson:2.10.1")

	implementation("com.mojang:authlib:2.1.28")
	api("com.mojang:datafixerupper:4.0.26") {
		exclude(group = "org.slf4j", module = "slf4j-api")
	}
	implementation(libs.jopt.simple)
	implementation(libs.terminalconsoleappender)
	implementation(projects.paper.log4jPlugin)
	implementation("commons-lang:commons-lang:2.6")
	api(projects.paper.mojangapi)
	api("commons-io:commons-io:2.13.0")

	api(libs.netty.buffer)
	api(libs.netty.codec)
	api(libs.netty.handler)
	api(libs.netty.transport)
	api(libs.netty.transport.epoll.classes)
	api(libs.netty.transport.epoll.native) { artifact { classifier = "linux-x86_64" } }
	api(libs.netty.transport.ioUring) { artifact { classifier = "linux-x86_64" } }
	implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

	runtimeOnly(libs.jline.terminal.jansi)

	constraints {
		implementation(libs.jline.reader)
	}
}

sourceSets {
	main {
		java {
			srcDir("src/main/netty_patches")
		}
	}
}

tasks {
	withType<JavaCompile>().configureEach {
		options.run {
			encoding = Charsets.UTF_8.name()
			compilerArgs.addAll(listOf(
				"-Xlint:all,-serial,-processing,-classfile",
				"--add-exports=java.base/jdk.internal.misc=ALL-UNNAMED",
				"--add-exports=java.base/jdk.internal=ALL-UNNAMED", // for @ValueBased
				"-Xmaxerrs", "100000",
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
	assemble { dependsOn(shadowJar) }
	shadowJar {
		archiveFileName.set("Paper-${project.version}.jar")

		mergeServiceFiles()
		transform(Log4j2PluginsCacheFileTransformer())

		exclude("**/module-info.class")

		manifest {
			attributes(
				"Main-Class" to "org.bukkit.craftbukkit.Main",
				"Specification-Title" to "Bukkit",
				"Implementation-Title" to "CraftBukkit",
				"Implementation-Version" to project.version,
				"Implementation-Vendor" to project.version,
				"Specification-Version" to "unknown",
				"Specification-Vendor" to "Bukkit Team",
				"Multi-Release" to "true"
			)

			arrayOf("net", "com", "org").forEach {
				attributes("$it/bukkit/", "Sealed" to true)
			}
		}
	}
}
