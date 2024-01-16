package org.pokesplash.teampreview.preview;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Consumer;

public class Preview {
    private Participant player1;
    private Participant player2;

    private boolean player1Ready;
    private boolean player2Ready;
    private Consumer<Preview> callback;

    public Preview(ServerPlayerEntity player1, ServerPlayerEntity player2, Consumer<Preview> callback) {
        this.player1 = new Participant(player1);
        this.player2 = new Participant(player2);
        this.callback = callback;
    }

    public Participant getPlayer(ServerPlayerEntity player) {
        if (getPlayer1().getPlayer().getUuid().equals(player.getUuid())) {
            return player1;
        }

        if (getPlayer2().getPlayer().getUuid().equals(player.getUuid())) {
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
        return player.getUuid().equals(player1.getPlayer().getUuid()) ||
                player.getUuid().equals(player2.getPlayer().getUuid());
    }

    public Participant getOtherPlayer(ServerPlayerEntity player) {
        if (player1.getPlayer().getUuid().equals(player.getUuid())) {
            return player2;
        }

        if (player2.getPlayer().getUuid().equals(player.getUuid())) {
            return player1;
        }

        return null;
    }

    public void setReady(Participant participant, boolean isReady) {
        if (player1.equals(participant)) {
            player1Ready = isReady;
            return;
        }

        if (player2.equals(participant)) {
            player2Ready = isReady;
            return;
        }
    }

    public Consumer<Preview> getCallback() {
        return callback;
    }
}
