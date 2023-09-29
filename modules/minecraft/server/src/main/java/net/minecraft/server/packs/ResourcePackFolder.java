package net.minecraft.server.packs;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.ResourceKeyInvalidException;
import net.minecraft.SystemUtils;
import net.minecraft.resources.MinecraftKey;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;

public class ResourcePackFolder extends ResourcePackAbstract {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final boolean c = SystemUtils.i() == SystemUtils.OS.WINDOWS;
	private static final CharMatcher d = CharMatcher.is('\\');

	public ResourcePackFolder(File var0) {
		super(var0);
	}

	public static boolean a(File var0, String var1) throws IOException {
		String var2 = var0.getCanonicalPath();
		if (c) {
			var2 = d.replaceFrom(var2, '/');
		}

		return var2.endsWith(var1);
	}

	@Override
	protected InputStream a(String var0) throws IOException {
		File var1 = this.e(var0);
		if (var1 == null) {
			throw new ResourceNotFoundException(this.a, var0);
		} else {
			return new FileInputStream(var1);
		}
	}

	@Override
	protected boolean c(String var0) {
		return this.e(var0) != null;
	}

	@Nullable
	private File e(String var0) {
		try {
			File var1 = new File(this.a, var0);
			if (var1.isFile() && a(var1, var0)) {
				return var1;
			}
		} catch (IOException ignored) {}

		return null;
	}

	@Override
	public Set<String> a(EnumResourcePackType var0) {
		Set<String> var1 = Sets.newHashSet();
		File var2 = new File(this.a, var0.a());
		File[] var3 = var2.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
		if (var3 != null) {
			for (File var7 : var3) {
				String var8 = a(var2, var7);
				if (var8.equals(var8.toLowerCase(Locale.ROOT))) {
					var1.add(var8.substring(0, var8.length() - 1));
				} else {
					this.d(var8);
				}
			}
		}

		return var1;
	}

	@Override
	public void close() {
	}

	@Override
	public Collection<MinecraftKey> a(EnumResourcePackType var0, String var1, String var2, int var3, Predicate<String> var4) {
		File var5 = new File(this.a, var0.a());
		List<MinecraftKey> var6 = Lists.newArrayList();
		this.a(new File(new File(var5, var1), var2), var3, var1, var6, var2 + "/", var4);
		return var6;
	}

	private void a(File var0, int var1, String var2, List<MinecraftKey> var3, String var4, Predicate<String> var5) {
		File[] var6 = var0.listFiles();
		if (var6 != null) {
			for (File var10 : var6) {
				if (var10.isDirectory()) {
					if (var1 > 0) {
						this.a(var10, var1 - 1, var2, var3, var4 + var10.getName() + "/", var5);
					}
				} else if (!var10.getName().endsWith(".mcmeta") && var5.test(var10.getName())) {
					try {
						var3.add(new MinecraftKey(var2, var4 + var10.getName()));
					} catch (ResourceKeyInvalidException var13) {
						LOGGER.error(var13.getMessage());
					}
				}
			}
		}
	}
}
