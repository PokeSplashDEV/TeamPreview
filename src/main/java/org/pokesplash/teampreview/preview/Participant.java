package org.pokesplash.teampreview.preview;

import net.minecraft.server.network.ServerPlayerEntity;

public class Participant {
    private ServerPlayerEntity player;
    private int lead;

    public Participant(ServerPlayerEntity player) {
        this.player = player;
        lead = 0;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }

    public int getLead() {
        return lead;
    }

    public void setLead(int lead) {
        this.lead = lead;
    }
}
