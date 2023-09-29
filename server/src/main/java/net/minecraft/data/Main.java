package net.minecraft.data;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.data.structures.DebugReportNBT;
import net.minecraft.server.ho;
import net.minecraft.server.hv;
import net.minecraft.server.hw;
import net.minecraft.server.hx;
import net.minecraft.server.ie;
import net.minecraft.server.ik;
import net.minecraft.server.jg;
import net.minecraft.server.jp;
import net.minecraft.server.jq;
import net.minecraft.server.js;
import net.minecraft.server.jt;
import net.minecraft.server.ju;
import net.minecraft.server.jv;
import net.minecraft.server.ks;

public class Main {

    public Main() {}

    public static void main(String[] astring) throws IOException {
        OptionParser optionparser = new OptionParser();
        OptionSpec<Void> optionspec = optionparser.accepts("help", "Show the help menu").forHelp();
        OptionSpec<Void> optionspec1 = optionparser.accepts("server", "Include server generators");
        OptionSpec<Void> optionspec2 = optionparser.accepts("client", "Include client generators");
        OptionSpec<Void> optionspec3 = optionparser.accepts("dev", "Include development tools");
        OptionSpec<Void> optionspec4 = optionparser.accepts("reports", "Include data reports");
        OptionSpec<Void> optionspec5 = optionparser.accepts("validate", "Validate inputs");
        OptionSpec<Void> optionspec6 = optionparser.accepts("all", "Include all generators");
        OptionSpec<String> optionspec7 = optionparser.accepts("output", "Output folder").withRequiredArg().defaultsTo("generated", new String[0]);
        OptionSpec<String> optionspec8 = optionparser.accepts("input", "Input folder").withRequiredArg();
        OptionSet optionset = optionparser.parse(astring);

        if (!optionset.has(optionspec) && optionset.hasOptions()) {
            Path path = Paths.get((String) optionspec7.value(optionset));
            boolean flag = optionset.has(optionspec6);
            boolean flag1 = flag || optionset.has(optionspec2);
            boolean flag2 = flag || optionset.has(optionspec1);
            boolean flag3 = flag || optionset.has(optionspec3);
            boolean flag4 = flag || optionset.has(optionspec4);
            boolean flag5 = flag || optionset.has(optionspec5);
            DebugReportGenerator debugreportgenerator = a(path, (Collection) optionset.valuesOf(optionspec8).stream().map((s) -> {
                return Paths.get(s);
            }).toList(), flag1, flag2, flag3, flag4, flag5);

            debugreportgenerator.c();
        } else {
            optionparser.printHelpOn(System.out);
        }
    }

    public static DebugReportGenerator a(Path path, Collection<Path> collection, boolean flag, boolean flag1, boolean flag2, boolean flag3, boolean flag4) {
        DebugReportGenerator debugreportgenerator = new DebugReportGenerator(path, collection);

        if (flag || flag1) {
            debugreportgenerator.a((new jp(debugreportgenerator)).a(new jq()));
        }

        if (flag) {
            debugreportgenerator.a(new ik(debugreportgenerator));
        }

        if (flag1) {
            debugreportgenerator.a(new ju(debugreportgenerator));
            js js = new js(debugreportgenerator);

            debugreportgenerator.a(js);
            debugreportgenerator.a(new jv(debugreportgenerator, js));
            debugreportgenerator.a(new jt(debugreportgenerator));
            debugreportgenerator.a(new jg(debugreportgenerator));
            debugreportgenerator.a(new ho(debugreportgenerator));
            debugreportgenerator.a(new ie(debugreportgenerator));
        }

        if (flag2) {
            debugreportgenerator.a(new DebugReportNBT(debugreportgenerator));
        }

        if (flag3) {
            debugreportgenerator.a(new hv(debugreportgenerator));
            debugreportgenerator.a(new hx(debugreportgenerator));
            debugreportgenerator.a(new hw(debugreportgenerator));
            debugreportgenerator.a(new ks(debugreportgenerator));
        }

        return debugreportgenerator;
    }
}
