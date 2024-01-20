package org.pokesplash.teampreview.preview;

import ca.landonjw.gooeylibs2.api.UIManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.pokesplash.teampreview.TeamPreview;
import org.pokesplash.teampreview.ui.TeamPreviewMenu;

import java.util.UUID;
import java.util.function.Consumer;

public class Preview {
    private Participant player1;
    private Participant player2;

    private boolean player1Ready;
    private boolean player2Ready;
    private Consumer<Preview> callback;

    public Preview(UUID player1, UUID player2, Consumer<Preview> callback) {
        this.player1 = new Participant(player1);
        this.player2 = new Participant(player2);
        this.callback = callback;
    }

    public Participant getPlayer(ServerPlayerEntity player) {
        if (getPlayer1().getPlayer().equals(player.getUuid())) {
            return player1;
        }

        if (getPlayer2().getPlayer().equals(player.getUuid())) {
            return player2;
        }

        return null;
    }

    public Participant getPlayer1() {
        return player1;
    }

    public Participant getPlayer2() {
        return player2;
    }

    public boolean containsPlayer(ServerPlayerEntity player) {
        return player.getUuid().equals(player1.getPlayer()) ||
                player.getUuid().equals(player2.getPlayer());
    }

    public boolean containsPlayer(UUID player) {
        return player.equals(player1.getPlayer()) ||
                player.equals(player2.getPlayer());
    }

    public Participant getOtherPlayer(ServerPlayerEntity player) {
        if (player1.getPlayer().equals(player.getUuid())) {
            return player2;
        }

        if (player2.getPlayer().equals(player.getUuid())) {
            return player1;
        }

        return null;
    }

    public void setReady(ServerPlayerEntity player, Participant participant, boolean isReady) {
        if (player1.equals(participant)) {
            player1Ready = isReady;
        } else if (player2.equals(participant)) {
            player2Ready = isReady;
        }

        // If both are ready, run the callback.
        if (player1Ready && player2Ready) {
            TeamPreview.run(this);
        } else {
            UIManager.openUIForcefully(player, new TeamPreviewMenu().getPage(player, this));
        }
    }

    public boolean isReady(Participant participant) {
        if (participant.getPlayer().equals(player1.getPlayer())) {
            return player1Ready;
        }

        if (participant.getPlayer().equals(player2.getPlayer())) {
            return player2Ready;
        }

        return false;
    }

    public Consumer<Preview> getCallback() {
        return callback;
    }
}
