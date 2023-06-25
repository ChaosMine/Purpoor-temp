dependencies {
	api("org.ow2.asm:asm:9.5")
	api("org.ow2.asm:asm-commons:9.5")
	api("it.unimi.dsi:fastutil:8.5.12")
	api("com.google.guava:guava:32.0.1-jre")
	implementation("commons-lang:commons-lang:2.6")
	api("net.md-5:bungeecord-chat:1.16-R0.4")
	implementation("io.netty:netty-all:4.1.92.Final")
	implementation("org.yaml:snakeyaml:1.33")
	implementation("org.slf4j:slf4j-api:1.7.36")

	api(platform("net.kyori:adventure-bom:4.14.0"))
	api("net.kyori:adventure-api")
	api("net.kyori:adventure-text-serializer-gson")
	api("net.kyori:adventure-text-serializer-legacy")
	api("net.kyori:adventure-text-serializer-plain")
	api("net.kyori:adventure-text-minimessage")

	api("org.apache.maven:maven-resolver-provider:3.9.1")
	api("org.apache.maven.resolver:maven-resolver-connector-basic:1.9.7")
	api("org.apache.maven.resolver:maven-resolver-transport-http:1.9.7")

	compileOnlyApi("org.jetbrains:annotations:24.0.1")
}
