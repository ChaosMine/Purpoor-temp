package net.minecraft.server;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.SystemUtils;
import net.minecraft.data.DebugReportGenerator;
import net.minecraft.data.DebugReportProvider;
import net.minecraft.data.HashCache;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class jp implements DebugReportProvider {
	@Nullable
	private static final java.nio.file.Path b = null;
	private static final Logger LOGGER = LogManager.getLogger();
	private final DebugReportGenerator d;
	private final List<jp.a> e = Lists.newArrayList();

	public jp(DebugReportGenerator var0) {
		this.d = var0;
	}

	public jp a(jp.a var0) {
		this.e.add(var0);
		return this;
	}

	private NBTTagCompound a(String var0, NBTTagCompound var1) {
		NBTTagCompound var2 = var1;

		for (jp.a var4 : this.e) {
			var2 = var4.a(var0, var2);
		}

		return var2;
	}

	@Override
	public void a(HashCache var0) throws IOException {
		java.nio.file.Path var1 = this.d.b();
		List<CompletableFuture<jp.b>> var2 = Lists.newArrayList();

		for (java.nio.file.Path var4 : this.d.a()) {
			try (var st = Files.walk(var4)) {
				st.filter(var0x -> var0x.toString().endsWith(".snbt"))
					.forEach(var2x -> var2.add(CompletableFuture.supplyAsync(() -> this.a(var2x, this.a(var4, var2x)), SystemUtils.f())));
			}
		}

		SystemUtils.b(var2).join().stream().filter(Objects::nonNull).forEach(var2x -> this.a(var0, var2x, var1));
	}

	@Override
	public String a() {
		return "SNBT -> NBT";
	}

	private String a(java.nio.file.Path var0, java.nio.file.Path var1) {
		String var2 = var0.relativize(var1).toString().replaceAll("\\\\", "/");
		return var2.substring(0, var2.length() - ".snbt".length());
	}

	@Nullable
	private jp.b a(java.nio.file.Path var0, String var1) {
		try {
			BufferedReader var2 = Files.newBufferedReader(var0);
			Throwable var4 = null;

			jp.b var11;
			try {
				String var4x = IOUtils.toString(var2);
				NBTTagCompound var5 = this.a(var1, MojangsonParser.parse(var4x));
				ByteArrayOutputStream var6 = new ByteArrayOutputStream();
				NBTCompressedStreamTools.a(var5, var6);
				byte[] var7 = var6.toByteArray();
				String var8 = a.hashBytes(var7).toString();

				var11 = new jp.b(var1, var7, null, var8);
			} catch (Throwable var22) {
				var4 = var22;
				throw var22;
			} finally {
				if (var4 != null) {
					try {
						var2.close();
					} catch (Throwable var21) {
						var4.addSuppressed(var21);
					}
				} else {
					var2.close();
				}
			}

			return var11;
		} catch (CommandSyntaxException var24) {
			LOGGER.error("Couldn't convert {} from SNBT to NBT at {} as it's invalid SNBT", var1, var0, var24);
		} catch (IOException var25) {
			LOGGER.error("Couldn't convert {} from SNBT to NBT at {}", var1, var0, var25);
		}

		return null;
	}

	private void a(HashCache var0, jp.b var1, java.nio.file.Path var2) {
		if (var1.c != null) {
			java.nio.file.Path var3 = b.resolve(var1.a + ".snbt");

			try {
				FileUtils.write(var3.toFile(), var1.c, StandardCharsets.UTF_8);
			} catch (IOException var18) {
				LOGGER.error("Couldn't write structure SNBT {} at {}", var1.a, var3, var18);
			}
		}

		java.nio.file.Path var3 = var2.resolve(var1.a + ".nbt");

		try {
			if (!Objects.equals(var0.a(var3), var1.d) || !Files.exists(var3)) {
				Files.createDirectories(var3.getParent());
				OutputStream var4 = Files.newOutputStream(var3);
				Throwable var6 = null;

				try {
					var4.write(var1.b);
				} catch (Throwable var17) {
					var6 = var17;
					throw var17;
				} finally {
					if (var4 != null) {
						if (var6 != null) {
							try {
								var4.close();
							} catch (Throwable var16) {
								var6.addSuppressed(var16);
							}
						} else {
							var4.close();
						}
					}
				}
			}

			var0.a(var3, var1.d);
		} catch (IOException var20) {
			LOGGER.error("Couldn't write structure {} at {}", var1.a, var3, var20);
		}
	}

	@FunctionalInterface
	public interface a {
		NBTTagCompound a(String var1, NBTTagCompound var2);
	}

	static class b {
		private final String a;
		private final byte[] b;
		@Nullable
		private final String c;
		private final String d;

		public b(String var0, byte[] var1, @Nullable String var2, String var3) {
			this.a = var0;
			this.b = var1;
			this.c = var2;
			this.d = var3;
		}
	}
}
