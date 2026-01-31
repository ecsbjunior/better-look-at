package com.junior.evandro.commands;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.junior.evandro.BetterLookAt;
import com.junior.evandro.config.BetterLookAtPosition;
import com.junior.evandro.messages.BetterLookAtI18n;
import com.junior.evandro.utils.BetterLookAtColor;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class BetterLookAtCommand extends AbstractAsyncCommand {
    private final OptionalArg<Boolean> enabledArg;
    private final OptionalArg<Boolean> showChestArg;
    private final OptionalArg<String> positionArg;

    public BetterLookAtCommand() {
        super("betterlookat", "You can configure BetterLookAt with this command");

        this.enabledArg = this.withOptionalArg("enabled", "true, false", ArgTypes.BOOLEAN);
        this.showChestArg = this.withOptionalArg("showchest", "true, false", ArgTypes.BOOLEAN);
        this.positionArg = this.withOptionalArg("position",
            "top-left, top-center, top-right, bottom-left, bottom-right", ArgTypes.STRING);

        this.addAliases("bla");
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

                var enabledArgumentValue = this.enabledArg.get(context);

                if (enabledArgumentValue != null) {
                    config.setEnabled(enabledArgumentValue);
                }

                var showChestArgumentValue = this.showChestArg.get(context);

                if (showChestArgumentValue != null) {
                    config.setShowChestContent(showChestArgumentValue);
                }

                var positionArgumentValue = this.positionArg.get(context);

                if (positionArgumentValue != null) {
                    var position = switch (positionArgumentValue) {
                        case "top-left" -> BetterLookAtPosition.TOP_LEFT;
                        case "top-center" -> BetterLookAtPosition.TOP_CENTER;
                        case "top-right" -> BetterLookAtPosition.TOP_RIGHT;
//                        case "bottom-left" -> BetterLookAtPosition.BOTTOM_LEFT;
//                        case "bottom-right" -> BetterLookAtPosition.BOTTOM_RIGHT;
                        default -> null;
                    };

                    if (position == null) {
                        context.sendMessage(
                            BetterLookAtI18n
                                .invalidArgumentValue("position", positionArgumentValue)
                                .color(BetterLookAtColor.DANGER));
                    } else {
                        config.setPosition(position);
                    }
                }

                BetterLookAt.CONFIG.save();
            }, world);
        } else {
            context.sendMessage(BetterLookAtI18n.onlyPlayersCanUseThisCommand(BetterLookAt.NAME));
        }

        return CompletableFuture.completedFuture(null);
    }
}
