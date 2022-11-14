package com.rabbitminers.createtools.config;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.common.ForgeConfigSpec;

public class CTConfig
{
    public static final CTConfig.General GENERAL;
    public static final ForgeConfigSpec spec;

    static {
        final Pair<General, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CTConfig.General::new);
        spec = specPair.getRight();
        GENERAL = specPair.getLeft();
    }

    public static class General {
        public ForgeConfigSpec.BooleanValue leaves;
        public ForgeConfigSpec.BooleanValue useMaterials;

        public ForgeConfigSpec.IntValue totalLimit;
        public ForgeConfigSpec.IntValue tickLimit;
        public ForgeConfigSpec.IntValue mode;
        public ForgeConfigSpec.DoubleValue damageMultiplier;
        public ForgeConfigSpec.DoubleValue speed;
        public ForgeConfigSpec.DoubleValue durabilityMultiplier;

        General(ForgeConfigSpec.Builder builder)
        {
            builder.comment("General configuration settings")
                    .push("General");

            totalLimit = builder
                    .comment("Hard limit of the amount that can be broken in one go.")
                    .translation("createtools.config.totalLimit")
                    .defineInRange("totalLimit", 1024, 1, Integer.MAX_VALUE);

            tickLimit = builder
                    .comment("Hard limit of the amount that can be broken in one go.")
                    .translation("createtools.config.tickLimit")
                    .defineInRange("tickLimit", 32, 1, Integer.MAX_VALUE);

            mode = builder
                    .comment("Valid modes:" +
                            "0: Only chop blocks with the same blockid" +
                            "1: Chop all wooden blocks")
                    .translation("createtools.config.mode")
                    .defineInRange("mode", 0, 0, 1);

            leaves = builder
                    .comment("Harvest leaves too.")
                    .translation("createtools.config.leaves")
                    .define("leaves", false);

            useMaterials = builder
                    .comment("Allow axes to chain break any wooden material along with the blocks in the `destroy_connected` tag. False = only blocks in the tag can be broken.")
                    .translation("createtools.config.useMaterials")
                    .define("useMaterials", true);

            damageMultiplier = builder
                    .comment("Multiplier used for attack damage. Tool material * this value = axe damage")
                    .translation("createtools.config.damageMultiplier")
                    .defineInRange("damageMultiplier", 3.2, 0, Integer.MAX_VALUE);

            durabilityMultiplier = builder
                    .comment("Multiplier used for durability. Tool material * this value = axe durability")
                    .translation("createtools.config.durabilityMultiplier")
                    .defineInRange("durabilityMultiplier", 1.5, 0, Integer.MAX_VALUE);

            speed = builder
                    .comment("Speed used for attack speed. 4 - this value = axe speed")
                    .translation("createtools.config.speed")
                    .defineInRange("speed", -3.3, -3.9, 0);

        }
    }
}