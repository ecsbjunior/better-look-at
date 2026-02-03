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

import javax.annotation.Nonnull;

public class BetterLookAtBenchCommand extends AbstractPlayerCommand {
    private final RequiredArg<String> visibilityArg;

    public BetterLookAtBenchCommand() {
        super("bench", "This command manage bench HUD");

        @SuppressWarnings("unchecked")
        Validator<String>[] validators =
            new Validator[] { new EqualValidator<>("show"), new EqualValidator<>("hide") };

        this.visibilityArg = withRequiredArg(
            "visibility", "Change visibility of bench, available values: show, hide", ArgTypes.STRING)
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

        var visibility = visibilityArg.get(commandContext);

        if (visibility == null) {
            return;
        }

        switch (visibility) {
            case "show" -> config.setShowBench(true);
            case "hide" -> config.setShowBench(false);
        }

        BetterLookAt.CONFIG.save();
    }
}
