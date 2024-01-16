package org.pokesplash.teampreview;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import org.pokesplash.teampreview.command.CommandHandler;
import org.pokesplash.teampreview.preview.Participant;
import org.pokesplash.teampreview.preview.Preview;
import org.pokesplash.teampreview.ui.TeamPreviewMenu;

import java.util.ArrayList;
import java.util.function.Consumer;

public class TeamPreview implements ModInitializer {
	private static final ArrayList<Preview> previews = new ArrayList<>();

	/**
	 * Runs the mod initializer.
	 */
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(CommandHandler::registerCommands);
	}

	/**
	 * Creates a team preview for two players, with a callback that is used for the confirm button.
	 * @param player1 The first player
	 * @param player2 The second player
	 * @param onConfirm Confirm button logic.
	 */
	public static void createPreview(ServerPlayerEntity player1, ServerPlayerEntity player2,
									 Consumer<Preview> onConfirm) {
		// Adds the preview to the list so it can be opened later if needed.
		previews.add(new Preview(player1, player2, onConfirm));
	}

	/**
	 * Opens the team preview for a player that already has one.
	 * @param player The player to query.
	 */
	public static void openPreview(ServerPlayerEntity player) {
		for (Preview preview : previews) {
			if (preview.containsPlayer(player)) {
				UIManager.openUIForcefully(player, new TeamPreviewMenu().getPage(player, preview));
				break;
			}
		}
	}

	/**
	 * Runs the confirm button for the preview.
	 * @param preview The preview to run the callback on.
	 */
	public static void run(Preview preview) {

		setLead(preview.getPlayer1());
		setLead(preview.getPlayer2());

		preview.getCallback().accept(preview);

		previews.remove(preview);
	}

	private static void setLead(Participant participant) {
		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(participant.getPlayer());

		party.swap(0, participant.getLead());
	}
}
