package com.junior.evandro.utils;

import com.hypixel.hytale.server.core.Message;

public class BetterLookAtMessage {
    public static Message showIf(Message message, boolean value) {
        return value ? message : Message.raw("");
    }

    public static Message wrapParentheses(Message value) {
        return Message.join(Message.raw("(").color(BetterLookAtColor.PRIMARY), value, Message.raw(")").color(BetterLookAtColor.PRIMARY));
    }

    public static Message toMessage(int value) {
        return Message.raw(String.format("%d", value)).color(BetterLookAtColor.PRIMARY).bold(true);
    }

    public static Message toMessage(double value) {
        return Message.raw(String.format("%.02f", value)).color(BetterLookAtColor.PRIMARY).bold(true);
    }

    public static Message toMessage(boolean value) {
        return !value ?
            Message.raw("No").color(BetterLookAtColor.DANGER).bold(true) :
            Message.raw("Yes").color(BetterLookAtColor.SUCCESS).bold(true);
    }
}
