package org.pokesplash.teampreview.preview;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class Participant {
    private UUID player;
    private int lead;

    public Participant(UUID player) {
        this.player = player;
        lead = 0;
    }

    public UUID getPlayer() {
        return player;
    }

    public int getLead() {
        return lead;
    }

    public void setLead(int lead) {
        this.lead = lead;
    }
}
