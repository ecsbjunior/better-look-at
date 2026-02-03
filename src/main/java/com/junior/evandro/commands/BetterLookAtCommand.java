package com.junior.evandro.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.junior.evandro.BetterLookAt;
import com.junior.evandro.messages.BetterLookAtI18n;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class BetterLookAtCommand extends AbstractAsyncCommand {
    public BetterLookAtCommand() {
        super("betterlookat", "You can configure BetterLookAt with this command");

        this.addAliases("bla");

        this.addSubCommand(new BetterLookAtBenchCommand());
        this.addSubCommand(new BetterLookAtChestCommand());
        this.addSubCommand(new BetterLookAtPositionCommand());
        this.addSubCommand(new BetterLookAtCommand.ShowCommand());
        this.addSubCommand(new BetterLookAtCommand.HideCommand());

        this.setPermissionGroup(GameMode.Adventure);
    }

    @Nonnull
    @Override
    protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext context) {
        var sender = context.sender();

        if (sender instanceof Player player) {
            var world = player.getWorld();

            if (world == null) {
                return CompletableFuture.completedFuture(null);
            }

            return CompletableFuture.runAsync(() -> {
                var config = BetterLookAt.CONFIG.get();

                BetterLookAt.CONFIG.save();
            }, world);
        } else {
            context.sendMessage(BetterLookAtI18n.onlyPlayersCanUseThisCommand(BetterLookAt.NAME));
        }

        return CompletableFuture.completedFuture(null);
    }

    private static class ShowCommand extends AbstractAsyncPlayerCommand {
        public ShowCommand() {
            super("show", "This command show HUD");
        }

        @Nonnull
        @Override
        protected CompletableFuture<Void> executeAsync(
            @Nonnull CommandContext commandContext,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> storeRef,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world
        ) {
            return CompletableFuture.runAsync(() -> {
                var config = BetterLookAt.CONFIG.get();

                config.setEnabled(true);

                BetterLookAt.CONFIG.save();
            }, world);
        }
    }

    private static class HideCommand extends AbstractAsyncPlayerCommand {
        public HideCommand() {
            super("hide", "This command hide HUD");
        }

        @Nonnull
        @Override
        protected CompletableFuture<Void> executeAsync(
            @Nonnull CommandContext commandContext,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> storeRef,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world
        ) {
            return CompletableFuture.runAsync(() -> {
                var config = BetterLookAt.CONFIG.get();

                config.setEnabled(false);

                BetterLookAt.CONFIG.save();
            }, world);
        }
    }
}
