package org.pokesplash.teampreview;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.pokesplash.teampreview.command.CommandHandler;
import org.pokesplash.teampreview.preview.Participant;
import org.pokesplash.teampreview.preview.Preview;
import org.pokesplash.teampreview.ui.TeamPreviewMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class TeamPreview implements ModInitializer {
	private static final ArrayList<Preview> previews = new ArrayList<>();
	public static MinecraftServer server;



	/**
	 * Runs the mod initializer.
	 */
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(CommandHandler::registerCommands);
		ServerWorldEvents.LOAD.register((t, e) -> server = t);
	}

	/**
	 * Creates a team preview for two players, with a callback that is used for the confirm button.
	 * @param player1 The first player
	 * @param player2 The second player
	 * @param onConfirm Confirm button logic.
	 */
	public static void createPreview(UUID player1, UUID player2,
									 List<Pokemon> player1Pokemon, List<Pokemon> player2Pokemon,
									 Consumer<Preview> onConfirm) {

		// If either player has an existing preview, remove it.
		for (Preview preview : previews) {
			if (preview.containsPlayer(player1) || preview.containsPlayer(player2)) {
				previews.remove(preview);
			}
		}

		// Adds the preview to the list so it can be opened later if needed.
		previews.add(new Preview(player1, player2, player1Pokemon, player2Pokemon, onConfirm));
	}

	/**
	 * Opens the team preview for a player that already has one.
	 * @param player The player to query.
	 */
	public static void openPreview(UUID player) {
		ServerPlayerEntity player1 = getPlayer(player);
		for (Preview preview : previews) {
			if (preview.containsPlayer(player1)) {
				UIManager.openUIForcefully(player1, new TeamPreviewMenu().getPage(player1, preview));
				break;
			}
		}
	}

	/**
	 * Runs the confirm button for the preview.
	 * @param preview The preview to run the callback on.
	 */
	public static void run(Preview preview) {

		// Closes any UI between the players.
		ServerPlayerEntity player1 = getPlayer(preview.getPlayer1().getPlayer());
		ServerPlayerEntity player2 = getPlayer(preview.getPlayer2().getPlayer());
		UIManager.closeUI(player1);
		UIManager.closeUI(player2);

		setLead(preview.getPlayer1());
		setLead(preview.getPlayer2());

		preview.getCallback().accept(preview);

		previews.remove(preview);
	}

	private static void setLead(Participant participant) {

		ServerPlayerEntity player = server.getPlayerManager().getPlayer(participant.getPlayer());

		if (player == null) {
			return;
		}

		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);

		party.swap(0, participant.getLead());
	}

	public static ServerPlayerEntity getPlayer(UUID player) {
		return server.getPlayerManager().getPlayer(player);
	}
}
