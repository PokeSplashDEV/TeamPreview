package org.pokesplash.teampreview.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.pokesplash.teampreview.util.LuckPermsUtils;

public class BaseCommand {
	public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> root = CommandManager
				.literal("teampreview")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(),
								CommandHandler.basePermission + ".base");
					} else {
						return true;
					}
				})
				.executes(this::run);

		LiteralCommandNode<ServerCommandSource> registeredCommand = dispatcher.register(root);

		dispatcher.register(CommandManager.literal("preview").requires(
				ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(), CommandHandler.basePermission + ".base");
					} else {
						return true;
					}
				})
				.redirect(registeredCommand).executes(this::run));

		registeredCommand.addChild(new ReloadCommand().build());

	}

	public int run(CommandContext<ServerCommandSource> context) {
		System.out.println("Base command run");
		return 1;
	}
}
