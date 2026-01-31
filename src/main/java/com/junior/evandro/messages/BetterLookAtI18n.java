package com.junior.evandro.messages;

import com.hypixel.hytale.server.core.Message;
import com.junior.evandro.BetterLookAt;
import com.junior.evandro.utils.BetterLookAtColor;

public class BetterLookAtI18n {
    public static final String NO_KEY = "server.%s.no".formatted(BetterLookAt.NAME);
    public static final String YES_KEY = "server.%s.yes".formatted(BetterLookAt.NAME);
    public static final String FUEL_KEY = "server.%s.fuel".formatted(BetterLookAt.NAME);
    public static final String CONSUMABLE_KEY = "server.%s.consumable".formatted(BetterLookAt.NAME);
    public static final String INVULNERABLE_KEY = "server.%s.invulnerable".formatted(BetterLookAt.NAME);
    public static final String INVALID_ARGUMENT_VALUE_KEY = "server.%s.invalidArgumentValue".formatted(BetterLookAt.NAME);
    public static final String ONLY_PLAYERS_CAN_USE_THIS_COMMAND_KEY = "server.%s.onlyPlayersCanUseThisCommand".formatted(BetterLookAt.NAME);

    public static Message __(String key) {
        return Message.translation(key);
    }

    public static Message no() {
        return BetterLookAtI18n.__(BetterLookAtI18n.NO_KEY);
    }

    public static Message yes() {
        return BetterLookAtI18n.__(BetterLookAtI18n.YES_KEY);
    }

    public static Message fuel(double quality) {
        var isFuel = quality > 1;
        var fuelMessage = BetterLookAtI18n.__(BetterLookAtI18n.FUEL_KEY);
        var isFuelMessage = BetterLookAtMessage.toMessage(isFuel);
        var fuelQualityMessage = BetterLookAtMessage.wrapParentheses(BetterLookAtMessage.toMessage(quality));

        return Message.join(
            fuelMessage,
            BetterLookAtMessage.colon(),
            BetterLookAtMessage.space(),
            isFuelMessage,
            BetterLookAtMessage.space(),
            BetterLookAtMessage.showIf(fuelQualityMessage, isFuel)
        );
    }

    public static Message consumable(boolean consumable) {
        var consumableMessage = BetterLookAtI18n.__(BetterLookAtI18n.CONSUMABLE_KEY);
        var isConsumableMessage = BetterLookAtMessage.toMessage(consumable);

        return Message.join(
            consumableMessage,
            BetterLookAtMessage.colon(),
            BetterLookAtMessage.space(),
            isConsumableMessage
        );
    }

    public static Message invulnerable(boolean invulnerable) {
        var invulnerableMessage = BetterLookAtI18n.__(BetterLookAtI18n.INVULNERABLE_KEY);
        var isInvulnerableMessage = BetterLookAtMessage.toMessage(invulnerable);

        return Message.join(
            invulnerableMessage,
            BetterLookAtMessage.colon(),
            BetterLookAtMessage.space(),
            isInvulnerableMessage
        );
    }

    public static Message invalidArgumentValue(String argument, String value) {
        var prefixMessage = Message.raw("[BetterLookAt]: ").bold(true);
        var invalidArgumentValueMessage = BetterLookAtI18n
            .__(BetterLookAtI18n.INVALID_ARGUMENT_VALUE_KEY)
            .param("argument", argument)
            .param("value", value);

        return Message.join(prefixMessage, invalidArgumentValueMessage).color(BetterLookAtColor.DANGER);
    }

    public static Message onlyPlayersCanUseThisCommand(String command) {
        return BetterLookAtI18n
            .__(BetterLookAtI18n.ONLY_PLAYERS_CAN_USE_THIS_COMMAND_KEY)
            .param("command", command).color(BetterLookAtColor.YELLOW);
    }
}
