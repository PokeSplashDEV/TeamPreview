package org.pokesplash.teampreview.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.pokesplash.teampreview.TeamPreview;
import org.pokesplash.teampreview.preview.Participant;
import org.pokesplash.teampreview.preview.Preview;
import org.pokesplash.teampreview.util.PokemonInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TeamPreviewMenu {
    public Page getPage(ServerPlayerEntity user, Preview preview) {

        int lead = preview.getPlayer(user).getLead();
        ServerPlayerEntity opponent = TeamPreview.getPlayer(preview.getOtherPlayer(user).getPlayer());

        Pokemon startingPokemon = preview.getPlayer(user).getPokemon().get(lead);

        ItemStack startingItem = startingPokemon != null ? PokemonItem.from(startingPokemon) :
                new ItemStack(CobblemonItems.POKE_BALL);

        Button startingPokemonButton = GooeyButton.builder()
                .title("Starting Pokemon")
                .display(startingItem)
                .hideFlags(FlagType.All)
                .lore(Text.class, PokemonInfo.parse(startingPokemon))
                .build();

        Button confirm = GooeyButton.builder()
                .title("§2Ready Up")
                .display(new ItemStack(Items.LIME_STAINED_GLASS_PANE))
                .hideFlags(FlagType.All)
                .onClick(e -> {
                    Participant participant = preview.getPlayer(user);
                    preview.setReady(user, participant, true);
                })
                .build();

        Button cancel = GooeyButton.builder()
                .title("§cCancel")
                .display(new ItemStack(Items.RED_STAINED_GLASS_PANE))
                .hideFlags(FlagType.All)
                .onClick(e -> {
                    Participant participant = preview.getPlayer(user);
                    preview.setReady(user, participant, false);
                })

                .build();

        // Gets the user and opponent party Pokemon as lists.
        ArrayList<Button> opponentPokemon = makeButtons(getPokemon(opponent, preview), false, null);
        ArrayList<Button> userPokemon = makeButtons(getPokemon(user, preview), true, e -> {

            // Sets the players lead.
            preview.getPlayer(user).setLead(e);
            // Open the UI with the new lead.
            UIManager.openUIForcefully(user, new TeamPreviewMenu().getPage(user, preview));
        });

        ChestTemplate template = ChestTemplate.builder(5)
                .fill(filler())
                .set(10, opponentPokemon.get(0))
                .set(11, opponentPokemon.get(1))
                .set(12, opponentPokemon.get(2))
                .set(13, opponentPokemon.get(3))
                .set(14, opponentPokemon.get(4))
                .set(15, opponentPokemon.get(5))
                .set(28, userPokemon.get(0))
                .set(29, userPokemon.get(1))
                .set(30, userPokemon.get(2))
                .set(31, userPokemon.get(3))
                .set(32, userPokemon.get(4))
                .set(33, userPokemon.get(5))
                .set(25, startingPokemonButton)
                .set(26, preview.isReady(preview.getPlayer(user)) ? cancel : confirm)
                .build();

        return GooeyPage.builder()
                .title("Team Preview")
                .template(template)
                .build();
    }

    private List<Pokemon> getPokemon(ServerPlayerEntity player, Preview preview) {

        return preview.getPlayer(player).getPokemon();
    }

    private ArrayList<Button> makeButtons(List<Pokemon> pokemons, boolean isUser,
                                          Consumer<Integer> callback) {
        ArrayList<Button> buttons = new ArrayList<>();

        for (int x=0; x < pokemons.size(); x++) {

            Pokemon pokemon = pokemons.get(x);

            // If the Pokemon is null, add a no pokemon button.
            if (pokemon == null) {
                buttons.add(
                        GooeyButton.builder()
                                .title("§cNo Pokemon")
                                .display(new ItemStack(CobblemonItems.POKE_BALL))
                                .hideFlags(FlagType.All)
                                .build()
                );
                continue;
            }

            // Creates the button.
            GooeyButton.Builder button = GooeyButton.builder()
                    .title(pokemon.getDisplayName().getString())
                    .display(PokemonItem.from(pokemon))
                    .hideFlags(FlagType.All);

            if (isUser) {
                int slot = x;
                button.onClick(e -> callback.accept(slot))
                        .lore(Text.class, PokemonInfo.parse(pokemon));
            }

            buttons.add(button.build());
        }

        return buttons;
    }

    private static Button filler() {
        return GooeyButton.builder()
                .title("")
                .display(new ItemStack(Items.WHITE_STAINED_GLASS_PANE))
                .hideFlags(FlagType.All)
                .build();
    }
}
