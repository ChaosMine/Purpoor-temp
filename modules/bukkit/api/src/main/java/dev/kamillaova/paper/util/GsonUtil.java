package dev.kamillaova.paper.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class GsonUtil {
	public static final Gson GSON = new GsonBuilder()
		.disableHtmlEscaping()
		.create();

	public static final Gson HUMAN_GSON = new GsonBuilder()
		.disableHtmlEscaping()
		.setPrettyPrinting()
		.setLenient()
		.create();
}
