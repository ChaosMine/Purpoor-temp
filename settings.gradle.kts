enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

fun includeFromModules(vararg projectPaths: String) {
	include(*projectPaths)
	projectPaths.forEach { path ->
		project(path).projectDir = file("./modules/${path.replace(":", "/")}")
	}
}

includeFromModules(
	":paper:mojangapi",
	":paper:log4j-plugin",
	":bukkit:api",
	":minecraft:server",
)

rootProject.name = "Purpoor"
