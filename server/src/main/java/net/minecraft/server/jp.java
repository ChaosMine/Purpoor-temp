package net.minecraft.server;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
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

public class jp implements DebugReportProvider {

    @Nullable
    private static final Path b = null;
    private static final Logger LOGGER = LogManager.getLogger();
    private final DebugReportGenerator d;
    private final List<jp.a> e = Lists.newArrayList();

    public jp(DebugReportGenerator debugreportgenerator) {
        this.d = debugreportgenerator;
    }

    public jp a(jp.a jp_a) {
        this.e.add(jp_a);
        return this;
    }

    private NBTTagCompound a(String s, NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = nbttagcompound;

        jp.a jp_a;

        for (Iterator iterator = this.e.iterator(); iterator.hasNext(); nbttagcompound1 = jp_a.a(s, nbttagcompound1)) {
            jp_a = (jp.a) iterator.next();
        }

        return nbttagcompound1;
    }

    @Override
    public void a(HashCache hashcache) throws IOException {
        Path path = this.d.b();
        List<CompletableFuture<jp.b>> list = Lists.newArrayList();
        Iterator iterator = this.d.a().iterator();

        while (iterator.hasNext()) {
            Path path1 = (Path) iterator.next();

            Files.walk(path1).filter((path2) -> {
                return path2.toString().endsWith(".snbt");
            }).forEach((path2) -> {
                list.add(CompletableFuture.supplyAsync(() -> {
                    return this.a(path2, this.a(path1, path2));
                }, SystemUtils.f()));
            });
        }

        ((List) SystemUtils.b((List) list).join()).stream().filter(Objects::nonNull).forEach((jp_b) -> {
            this.a(hashcache, jp_b, path);
        });
    }

    @Override
    public String a() {
        return "SNBT -> NBT";
    }

    private String a(Path path, Path path1) {
        String s = path.relativize(path1).toString().replaceAll("\\\\", "/");

        return s.substring(0, s.length() - ".snbt".length());
    }

    @Nullable
    private jp.b a(Path path, String s) {
        try {
            BufferedReader bufferedreader = Files.newBufferedReader(path);
            Throwable throwable = null;

            jp.b jp_b;

            try {
                String s1 = IOUtils.toString(bufferedreader);
                NBTTagCompound nbttagcompound = this.a(s, MojangsonParser.parse(s1));
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();

                NBTCompressedStreamTools.a(nbttagcompound, (OutputStream) bytearrayoutputstream);
                byte[] abyte = bytearrayoutputstream.toByteArray();
                String s2 = jp.a.hashBytes(abyte).toString();
                String s3;

                if (jp.b != null) {
                    s3 = nbttagcompound.a("    ", 0).getString() + "\n";
                } else {
                    s3 = null;
                }

                jp_b = new jp.b(s, abyte, s3, s2);
            } catch (Throwable throwable1) {
                throwable = throwable1;
                throw throwable1;
            } finally {
                if (bufferedreader != null) {
                    if (throwable != null) {
                        try {
                            bufferedreader.close();
                        } catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    } else {
                        bufferedreader.close();
                    }
                }

            }

            return jp_b;
        } catch (CommandSyntaxException commandsyntaxexception) {
            jp.LOGGER.error("Couldn't convert {} from SNBT to NBT at {} as it's invalid SNBT", s, path, commandsyntaxexception);
        } catch (IOException ioexception) {
            jp.LOGGER.error("Couldn't convert {} from SNBT to NBT at {}", s, path, ioexception);
        }

        return null;
    }

    private void a(HashCache hashcache, jp.b jp_b, Path path) {
        Path path1;

        if (jp_b.c != null) {
            path1 = jp.b.resolve(jp_b.a + ".snbt");

            try {
                FileUtils.write(path1.toFile(), jp_b.c, StandardCharsets.UTF_8);
            } catch (IOException ioexception) {
                jp.LOGGER.error("Couldn't write structure SNBT {} at {}", jp_b.a, path1, ioexception);
            }
        }

        path1 = path.resolve(jp_b.a + ".nbt");

        try {
            if (!Objects.equals(hashcache.a(path1), jp_b.d) || !Files.exists(path1, new LinkOption[0])) {
                Files.createDirectories(path1.getParent());
                OutputStream outputstream = Files.newOutputStream(path1);
                Throwable throwable = null;

                try {
                    outputstream.write(jp_b.b);
                } catch (Throwable throwable1) {
                    throwable = throwable1;
                    throw throwable1;
                } finally {
                    if (outputstream != null) {
                        if (throwable != null) {
                            try {
                                outputstream.close();
                            } catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        } else {
                            outputstream.close();
                        }
                    }

                }
            }

            hashcache.a(path1, jp_b.d);
        } catch (IOException ioexception1) {
            jp.LOGGER.error("Couldn't write structure {} at {}", jp_b.a, path1, ioexception1);
        }

    }

    @FunctionalInterface
    public interface a {

        NBTTagCompound a(String s, NBTTagCompound nbttagcompound);
    }

    static class b {

        private final String a;
        private final byte[] b;
        @Nullable
        private final String c;
        private final String d;

        public b(String s, byte[] abyte, @Nullable String s1, String s2) {
            this.a = s;
            this.b = abyte;
            this.c = s1;
            this.d = s2;
        }
    }
}
