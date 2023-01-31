package dev.kamillaova.paper.server.config;

import dev.kamillaova.paper.util.GsonUtil;
import io.netty.util.ResourceLeakDetector;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public record BootstrapConfig(
	int nettyThreads,
	ResourceLeakDetector.Level nettyLeakDetectorLevel
) {
	public static final BootstrapConfig DEFAULT = new BootstrapConfig(
		4,
		ResourceLeakDetector.Level.DISABLED
	);

	public static BootstrapConfig load(Path path) {
		try {
			if (!Files.exists(path)) {
				DEFAULT.save(path);
				return DEFAULT;
			}

			return GsonUtil.HUMAN_GSON.fromJson(Files.newBufferedReader(path), BootstrapConfig.class);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public void save(Path path) {
		try {
			var json = GsonUtil.HUMAN_GSON.toJson(this);
			Files.writeString(path, json);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
