package net.pl3x.purpur;

import com.destroystokyo.paper.VersionHistoryManager;
import com.destroystokyo.paper.util.VersionFetcher;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PurpurVersionFetcher implements VersionFetcher {
    private static final String JENKINS_URL = "https://ci.pl3x.net/job/Purpur/lastSuccessfulBuild/buildNumber";
    private static final String GITHUB_BRANCH_NAME = "master";

    @Override
    public long getCacheTime() {
        return 720000;
    }

    @Nonnull
    @Override
    public Component getVersionMessage(@Nonnull String serverVersion) {
        String[] parts = serverVersion.substring("git-Purpur-".length()).split("[-\\s]");
        final Component updateMessage = getUpdateStatusMessage("pl3xgaming/Purpur", GITHUB_BRANCH_NAME, parts[0]);
        final Component history = getHistory();

        return history != null ? TextComponent.ofChildren(updateMessage, Component.newline(), history) : updateMessage;
    }

    private static Component getUpdateStatusMessage(@Nonnull String repo, @Nonnull String branch, @Nonnull String versionInfo) {
        int distance;
        try {
            int jenkinsBuild = Integer.parseInt(versionInfo);
            distance = fetchDistanceFromJenkins(jenkinsBuild);
        } catch (NumberFormatException ignored) {
            versionInfo = versionInfo.replace("\"", "");
            distance = fetchDistanceFromGitHub(repo, branch, versionInfo);
        }

        switch (distance) {
            case -1:
                return Component.text("Error obtaining version information", NamedTextColor.YELLOW);
            case 0:
                return Component.text("You are running the latest version", NamedTextColor.GREEN);
            case -2:
                return Component.text("Unknown version", NamedTextColor.YELLOW);
            default:
                return Component.text("You are " + distance + " version(s) behind", NamedTextColor.YELLOW);
        }
    }

    private static int fetchDistanceFromJenkins(int jenkinsBuild) {
        try {
            try (BufferedReader reader = Resources.asCharSource(new URL(JENKINS_URL), Charsets.UTF_8).openBufferedStream()) {
                return Integer.decode(reader.readLine()) - jenkinsBuild;
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                return -2;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Contributed by Techcable <Techcable@outlook.com> in GH-65
    private static int fetchDistanceFromGitHub(@Nonnull String repo, @Nonnull String branch, @Nonnull String hash) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.github.com/repos/" + repo + "/compare/" + branch + "..." + hash).openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) return -2; // Unknown commit
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charsets.UTF_8))) {
                JsonObject obj = new Gson().fromJson(reader, JsonObject.class);
                String status = obj.get("status").getAsString();
                switch (status) {
                    case "identical":
                        return 0;
                    case "behind":
                        return obj.get("behind_by").getAsInt();
                    default:
                        return -1;
                }
            } catch (JsonSyntaxException | NumberFormatException e) {
                e.printStackTrace();
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Nullable
    private Component getHistory() {
        final VersionHistoryManager.VersionData data = VersionHistoryManager.INSTANCE.getVersionData();
        if (data == null) {
            return null;
        }

        final String oldVersion = data.getOldVersion();
        if (oldVersion == null) {
            return null;
        }

        return Component.text("Previous version: " + oldVersion, NamedTextColor.GRAY, TextDecoration.ITALIC);
    }
}
