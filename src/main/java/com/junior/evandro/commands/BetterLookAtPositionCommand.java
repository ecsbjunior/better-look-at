package com.junior.evandro.commands;

import com.hypixel.hytale.codec.validation.Validator;
import com.hypixel.hytale.codec.validation.validator.EqualValidator;
import com.hypixel.hytale.codec.validation.validator.OrValidator;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.junior.evandro.BetterLookAt;
import com.junior.evandro.config.BetterLookAtPosition;
import com.junior.evandro.messages.BetterLookAtI18n;
import com.junior.evandro.utils.BetterLookAtColor;

import javax.annotation.Nonnull;

public class BetterLookAtPositionCommand extends AbstractPlayerCommand {
    private final RequiredArg<String> positionArg;

    public BetterLookAtPositionCommand() {
        super("position", "This command change hud position");

        @SuppressWarnings("unchecked")
        Validator<String>[] validators = new Validator[] {
            new EqualValidator<>("top-left"),
            new EqualValidator<>("top-center"),
            new EqualValidator<>("top-right")
        };

        this.positionArg = withRequiredArg(
            "position", "Change position of hud, available values: top-left, top-center, top-right", ArgTypes.STRING)
            .addValidator(new OrValidator<>(validators));
    }

    @Override
    protected void execute(
        @Nonnull CommandContext commandContext,
        @Nonnull Store<EntityStore> store,
        @Nonnull Ref<EntityStore> storeRef,
        @Nonnull PlayerRef playerRef,
        @Nonnull World world
    ) {
        var config = BetterLookAt.CONFIG.get();

        var positionValue = this.positionArg.get(commandContext);

        var position = switch (positionValue) {
            case "top-left" -> BetterLookAtPosition.TOP_LEFT;
            case "top-center" -> BetterLookAtPosition.TOP_CENTER;
            case "top-right" -> BetterLookAtPosition.TOP_RIGHT;
            default -> null;
        };

        if (position == null) {
            commandContext.sendMessage(
                BetterLookAtI18n
                    .invalidArgumentValue("position", positionValue)
                    .color(BetterLookAtColor.DANGER));
        } else {
            config.setPosition(position);
        }

        BetterLookAt.CONFIG.save();
    }
}
