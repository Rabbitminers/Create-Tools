package com.rabbitminers.createtools.events;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ItemPickup {
    @SubscribeEvent
    public void ItemPickupEvent(PlayerEvent.ItemPickupEvent e) {
        System.out.println(e.getStack());
    }

}
