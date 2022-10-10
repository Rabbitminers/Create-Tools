package com.rabbitminers.createtools.util;

public class CTComponents {
    public enum Components {
        BLAZE_BURNER("blaze_burner", 64, "Automatically smelts all items"),
        CRUSHING_WHEEL("crushing_wheel", 128, "Mill any block drops"),
        MILL_STONE("mill_stone", 32, "Mill any block drops"),
        FILTER("filter", 16, "Filter items that can be collected while tool is held"),
        MECHANICAL_PRESS("press", 48, "Press any item drops"),
        EXTENDO_GRIP("extendo_grip", 32, "Extends interaction range"),
        FLYWHEEL("flywheel", 0, "Increase SU efficiency"),
        ITEM_VAULT("item_vault", 32, "Store items within the tool"),
        ROTATIONAL_SPEED_CONTROLLER("rotational_speed_controller", 1, "Increases or decreases the speed of tools, at the cost of SU"),
        MECHANICAL_ARM("mechanical_arm", 64, "access nearby inventories"),
        FAN("fan", 128, "Pull items towards the player");
        private final String name;
        private final int SUusage;
        private final String description;

        Components(
            String name,
            int SUusage,
            String description
        ) {
            this.name = name;
            this.SUusage = SUusage;
            this.description = description;

        }

        public String getName() {
            return name;
        }

        public int getSUusage() {
            return SUusage;
        }

        public String getDescription() {
            return description;
        }
    }
}
