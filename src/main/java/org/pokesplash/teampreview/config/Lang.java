package org.pokesplash.teampreview.config;

import com.google.gson.Gson;
import org.pokesplash.teampreview.TeamPreview;
import org.pokesplash.teampreview.util.Utils;

import java.util.concurrent.CompletableFuture;

public class Lang {
	private String title;
	private String fillerMaterial;

	public Lang() {
		title = TeamPreview.MOD_ID;
		fillerMaterial = "minecraft:white_stained_glass_pane";
	}

	public String getTitle() {
		return title;
	}

	public String getFillerMaterial() {
		return fillerMaterial;
	}

	/**
	 * Method to initialize the config.
	 */
	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(TeamPreview.BASE_PATH, "lang.json",
				el -> {
					Gson gson = Utils.newGson();
					Lang lang = gson.fromJson(el, Lang.class);
					title = lang.getTitle();
					fillerMaterial = lang.getFillerMaterial();
				});

		if (!futureRead.join()) {
			TeamPreview.LOGGER.info("No lang.json file found for " + TeamPreview.MOD_ID + ". Attempting to " +
					"generate " +
					"one.");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(TeamPreview.BASE_PATH, "lang.json", data);

			if (!futureWrite.join()) {
				TeamPreview.LOGGER.fatal("Could not write lang.json for " + TeamPreview.MOD_ID + ".");
			}
			return;
		}
		TeamPreview.LOGGER.info(TeamPreview.MOD_ID + " lang file read successfully.");
	}
}
