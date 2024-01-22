package org.pokesplash.teampreview.preview;

import com.cobblemon.mod.common.pokemon.Pokemon;

import java.util.List;
import java.util.UUID;

public class Participant {
    private final UUID player;
    private final List<Pokemon> pokemon;
    private int lead;

    public Participant(UUID player, List<Pokemon> pokemon) {
        this.player = player;
        lead = 0;

        this.pokemon = pokemon;

        while (this.pokemon.size() < 6) {
            this.pokemon.add(null);
        }

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

    public List<Pokemon> getPokemon() {
        return pokemon;
    }
}
