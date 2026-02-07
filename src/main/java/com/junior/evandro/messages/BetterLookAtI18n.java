package com.junior.evandro.messages;

import com.hypixel.hytale.server.core.Message;
import com.junior.evandro.BetterLookAt;
import com.junior.evandro.ecs.data.BetterLookAtFarmingGrowthStatus;
import com.junior.evandro.utils.BetterLookAtColor;

public class BetterLookAtI18n {
    public static final String NO_KEY = "server.%s.no".formatted(BetterLookAt.NAME);
    public static final String YES_KEY = "server.%s.yes".formatted(BetterLookAt.NAME);

    public static final String FUEL_KEY = "server.%s.fuel".formatted(BetterLookAt.NAME);
    public static final String CONSUMABLE_KEY = "server.%s.consumable".formatted(BetterLookAt.NAME);
    public static final String BENCH_TIER_KEY = "server.%s.benchTier".formatted(BetterLookAt.NAME);
    public static final String INVULNERABLE_KEY = "server.%s.invulnerable".formatted(BetterLookAt.NAME);
    public static final String BENCH_FUELS_KEY = "server.%s.benchFuels".formatted(BetterLookAt.NAME);
    public static final String BENCH_INPUTS_KEY = "server.%s.benchInputs".formatted(BetterLookAt.NAME);
    public static final String BENCH_OUTPUTS_KEY = "server.%s.benchOutputs".formatted(BetterLookAt.NAME);

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

    public static Message benchTier(int tier) {
        var benchTierMessage = BetterLookAtI18n.__(BetterLookAtI18n.BENCH_TIER_KEY);
        var benchTierLevelMessage = BetterLookAtMessage.toMessage(tier);

        return Message.join(
            benchTierMessage,
            BetterLookAtMessage.colon(),
            BetterLookAtMessage.space(),
            benchTierLevelMessage
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

    public static Message benchFuels() {
        return BetterLookAtI18n.__(BetterLookAtI18n.BENCH_FUELS_KEY);
    }

    public static Message benchInputs() {
        return BetterLookAtI18n.__(BetterLookAtI18n.BENCH_INPUTS_KEY);
    }

    public static Message benchOutputs() {
        return BetterLookAtI18n.__(BetterLookAtI18n.BENCH_OUTPUTS_KEY);
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

    public static class Farming {
        private static final String BASE_KEY = "farming";

        public static class CurrentStageInfo {
            private static final String BASE_KEY = "currentStageInfo";
            private static final String STAGE_KEY = "server.%s.%s.%s.stage".formatted(
                BetterLookAt.NAME, Farming.BASE_KEY, CurrentStageInfo.BASE_KEY);

            public static Message stage(int stage, int maxStage) {
                var stageMessage = BetterLookAtI18n.__(STAGE_KEY);
                var stageNumberMessage = Message.raw("%d".formatted(stage)).bold(true);
                var maxStageNumberMessage = Message.raw("%d".formatted(maxStage)).bold(true);

                return Message.join(
                    stageMessage,
                    BetterLookAtMessage.colon(),
                    BetterLookAtMessage.space(),
                    BetterLookAtMessage.wrapParentheses(
                        Message.join(stageNumberMessage, BetterLookAtMessage.slash(), maxStageNumberMessage)
                            .color(BetterLookAtColor.PRIMARY)
                    )
                );
            }
        }

        public static class RemainingTime {
            private static final String BASE_KEY = "remainingTime";
            private static final String REMAINING_TIME_KEY = "server.%s.%s.%s.remainingTime".formatted(
                BetterLookAt.NAME, Farming.BASE_KEY, RemainingTime.BASE_KEY);

            public static Message remainingTime() {
                var remainingTimeMessage = BetterLookAtI18n.__(REMAINING_TIME_KEY);

                return Message.join(
                    remainingTimeMessage,
                    BetterLookAtMessage.colon()
                );
            }
        }

        public static class LightStatus {
            private static final String BASE_KEY = "lightStatus";
            private static final String LIGHT_KEY = "server.%s.%s.%s.light".formatted(
                BetterLookAt.NAME, Farming.BASE_KEY, LightStatus.BASE_KEY);

            public static Message light() {
                var lightMessage = BetterLookAtI18n.__(LIGHT_KEY);

                return Message.join(
                    lightMessage,
                    BetterLookAtMessage.colon()
                );
            }
        }

        public static class WaterStatus {
            private static final String BASE_KEY = "waterStatus";
            private static final String WATER_KEY = "server.%s.%s.%s.water".formatted(
                BetterLookAt.NAME, Farming.BASE_KEY, WaterStatus.BASE_KEY);

            public static Message water() {
                var waterMessage = BetterLookAtI18n.__(WATER_KEY);

                return Message.join(
                    waterMessage,
                    BetterLookAtMessage.colon()
                );
            }
        }

        public static class FertilizerStatus {
            private static final String BASE_KEY = "fertilizerStatus";
            private static final String FERTILIZER_KEY = "server.%s.%s.%s.fertilizer".formatted(
                BetterLookAt.NAME, Farming.BASE_KEY, FertilizerStatus.BASE_KEY);

            public static Message fertilizer() {
                var fertilizerMessage = BetterLookAtI18n.__(FERTILIZER_KEY);

                return Message.join(
                    fertilizerMessage,
                    BetterLookAtMessage.colon()
                );
            }
        }

        public static class GrowthStatus {
            private static final String BASE_KEY = "growthStatus";
            private static final String OK_KEY = "server.%s.%s.%s.ok".formatted(
                BetterLookAt.NAME, Farming.BASE_KEY, GrowthStatus.BASE_KEY);
            private static final String MISSING_KEY = "server.%s.%s.%s.missing".formatted(
                BetterLookAt.NAME, Farming.BASE_KEY, GrowthStatus.BASE_KEY);

            public static Message status(BetterLookAtFarmingGrowthStatus status) {
                var statusKey = switch (status) {
                    case Ok -> OK_KEY;
                    case Missing -> MISSING_KEY;
                };

                var statusMessage = BetterLookAtI18n.__(statusKey);

                switch (status) {
                    case Ok -> statusMessage.color(BetterLookAtColor.SUCCESS);
                    case Missing -> statusMessage.color(BetterLookAtColor.YELLOW);
                }

                return Message.join(
                    statusMessage
                );
            }
        }
    }
}
