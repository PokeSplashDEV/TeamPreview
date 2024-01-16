package org.pokesplash.teampreview.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.pokesplash.teampreview.TeamPreview;

public class BaseCommand {
	public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> root = CommandManager
				.literal("teampreview").executes(this::run);

		LiteralCommandNode<ServerCommandSource> registeredCommand = dispatcher.register(root);

		dispatcher.register(CommandManager.literal("preview").redirect(registeredCommand).executes(this::run));

	}

	public int run(CommandContext<ServerCommandSource> context) {

		if (context.getSource().isExecutedByPlayer()) {
			return 1;
		}

		TeamPreview.openPreview(context.getSource().getPlayer());
		return 1;
	}
}
