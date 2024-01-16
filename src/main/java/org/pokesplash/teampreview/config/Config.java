package org.pokesplash.teampreview.config;

import com.google.gson.Gson;
import org.pokesplash.teampreview.TeamPreview;
import org.pokesplash.teampreview.util.Utils;

import java.util.concurrent.CompletableFuture;

public class Config {
	private boolean isExample;

	public Config() {
		isExample = true;
	}

	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(TeamPreview.BASE_PATH,
				"config.json", el -> {
					Gson gson = Utils.newGson();
					Config cfg = gson.fromJson(el, Config.class);
					isExample = cfg.isExample();
				});

		if (!futureRead.join()) {
			TeamPreview.LOGGER.info("No config.json file found for " + TeamPreview.MOD_ID + ". Attempting to generate" +
					" " +
					"one");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(TeamPreview.BASE_PATH,
					"config.json", data);

			if (!futureWrite.join()) {
				TeamPreview.LOGGER.fatal("Could not write config for " + TeamPreview.MOD_ID + ".");
			}
			return;
		}
		TeamPreview.LOGGER.info(TeamPreview.MOD_ID + " config file read successfully");
	}

	public boolean isExample() {
		return isExample;
	}
}
