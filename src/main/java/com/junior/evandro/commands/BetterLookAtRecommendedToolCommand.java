package com.junior.evandro.commands;

import com.hypixel.hytale.codec.validation.Validator;
import com.hypixel.hytale.codec.validation.validator.EqualValidator;
import com.hypixel.hytale.codec.validation.validator.OrValidator;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.junior.evandro.BetterLookAt;
import com.junior.evandro.config.BetterLookAtScale;

import javax.annotation.Nonnull;

public class BetterLookAtRecommendedToolCommand extends AbstractPlayerCommand {
    private final OptionalArg<String> scaleArg;
    private final OptionalArg<String> visibilityArg;

    public BetterLookAtRecommendedToolCommand() {
        super("recommended-tool", "This command manage recommended tool HUD");

        this.addAliases("rt");

        @SuppressWarnings("unchecked")
        Validator<String>[] scaleValidators = new Validator[] {
            new EqualValidator<>("xsm"),
            new EqualValidator<>("extra-small"),
            new EqualValidator<>("sm"),
            new EqualValidator<>("small"),
            new EqualValidator<>("md"),
            new EqualValidator<>("medium"),
            new EqualValidator<>("lg"),
            new EqualValidator<>("large"),
            new EqualValidator<>("xlg"),
            new EqualValidator<>("extra-large")
        };

        this.scaleArg = withOptionalArg(
            "scale", "Change scale of recommended tool, available values: xsm, extra-small, sm, small, md, medium, lg, large, xlg, extra-large", ArgTypes.STRING)
            .addValidator(new OrValidator<>(scaleValidators));

        @SuppressWarnings("unchecked")
        Validator<String>[] visibilityValidators =
            new Validator[] { new EqualValidator<>("show"), new EqualValidator<>("hide") };

        this.visibilityArg = withOptionalArg(
            "visibility", "Change visibility of recommended tool, available values: show, hide", ArgTypes.STRING)
            .addValidator(new OrValidator<>(visibilityValidators));
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

        if (visibility != null) {
            switch (visibility) {
                case "show" -> config.setShowRecommendedTool(true);
                case "hide" -> config.setShowRecommendedTool(false);
            }
        }

        var scale = scaleArg.get(commandContext);

        if (scale != null) {
            switch (scale) {
                case "xsm", "extra-small" -> config.setRecommendedToolScale(BetterLookAtScale.ExtraSmall);
                case "sm", "small" -> config.setRecommendedToolScale(BetterLookAtScale.Small);
                case "md", "medium" -> config.setRecommendedToolScale(BetterLookAtScale.Medium);
                case "lg", "large" -> config.setRecommendedToolScale(BetterLookAtScale.Large);
                case "xlg", "extra-large" -> config.setRecommendedToolScale(BetterLookAtScale.ExtraLarge);
            }
        }

        BetterLookAt.CONFIG.save();
    }
}
