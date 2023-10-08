enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

fun includeFromModules(vararg projectPaths: String) {
	include(*projectPaths)
	projectPaths.forEach { path ->
		val dir = "./modules/${path.replace(":", "/")}"
		generateSequence(path) {
			it.substringBeforeLast(":", "").takeIf(String::isNotBlank)
		}.forEachIndexed { idx, projectPath ->
			project(projectPath).projectDir = file(dir + "/..".repeat(idx))
		}
	}
}

includeFromModules(
	":paper:mojangapi",
	":paper:log4j-plugin",
	":bukkit:api",
	":minecraft:server",
)

rootProject.name = "Purpoor"
