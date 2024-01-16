package org.pokesplash.teampreview.util;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.ArrayList;
import java.util.Collection;


public abstract class PokemonInfo {
    public static Collection<Text> parse(Pokemon pokemon) {

        if (pokemon == null) {
            return new ArrayList<>();
        }

        Collection<Text> lore = new ArrayList<>();
        Style dark_aqua = Style.EMPTY.withColor(TextColor.parse("dark_aqua"));
        Style dark_green = Style.EMPTY.withColor(TextColor.parse("dark_green"));
        Style dark_purple = Style.EMPTY.withColor(TextColor.parse("dark_purple"));
        Style gold = Style.EMPTY.withColor(TextColor.parse("gold"));
        Style gray = Style.EMPTY.withColor(TextColor.parse("gray"));
        Style green = Style.EMPTY.withColor(TextColor.parse("green"));
        Style red = Style.EMPTY.withColor(TextColor.parse("red"));
        Style light_purple = Style.EMPTY.withColor(TextColor.parse("light_purple"));
        Style yellow = Style.EMPTY.withColor(TextColor.parse("yellow"));
        Style white = Style.EMPTY.withColor(TextColor.parse("white"));

        lore.add(Text.translatable("cobblemon.ui.info.species").setStyle(dark_green).append(": ")
                .append(pokemon.getSpecies().getTranslatedName().setStyle(green)));

        MutableText types = Text.empty().setStyle(green);
        for (ElementalType type : pokemon.getSpecies().getTypes()) {
            types.append(" ").append(type.getDisplayName());
        }
        lore.add(Text.translatable("cobblemon.ui.info.type").setStyle(dark_green).append(":").append(types));

        lore.add(Text.translatable("cobblemon.ui.info.nature").setStyle(dark_green).append(": ")
                .append(Text.translatable(pokemon.getNature().getDisplayName()).setStyle(green)));

        MutableText ability = Text.translatable("cobblemon.ui.info.ability").setStyle(dark_green).append(": ")
                .append(Text.translatable(pokemon.getAbility().getDisplayName()).setStyle(green));
        lore.add(ability);

        lore.add(Text.translatable("cobblemon.ui.stats").setStyle(gray).append(": "));

        lore.add(Text.translatable("cobblemon.ui.stats.hp").setStyle(light_purple)
                .append(": " + pokemon.getStat(Stats.HP)));
        lore.add(Text.translatable("cobblemon.ui.stats.atk").setStyle(red)
                .append(": " + pokemon.getStat(Stats.ATTACK)));
        lore.add(Text.translatable("cobblemon.ui.stats.def").setStyle(gold)
                .append(": " + pokemon.getStat(Stats.DEFENCE)));
        lore.add(Text.translatable("cobblemon.ui.stats.sp_atk").setStyle(dark_purple)
                .append(": " + pokemon.getStat(Stats.SPECIAL_ATTACK)));
        lore.add(Text.translatable("cobblemon.ui.stats.sp_def").setStyle(yellow)
                .append(": " + pokemon.getStat(Stats.SPECIAL_DEFENCE)));
        lore.add(Text.translatable("cobblemon.ui.stats.speed").setStyle(dark_aqua)
                .append(": " + pokemon.getStat(Stats.SPEED)));

        lore.add(Text.translatable("cobblemon.ui.stats.friendship").setStyle(dark_green)
                .append(": §a" + pokemon.getFriendship()));

        lore.add(Text.translatable("cobblemon.ui.moves").setStyle(gold).append(": "));
        for (Move move : pokemon.getMoveSet().getMoves()) {
            lore.add(Text.translatable(move.getTemplate().getDisplayName().getString()).setStyle(white));
        }

        return lore;
    }
}