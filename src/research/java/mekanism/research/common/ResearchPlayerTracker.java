package mekanism.research.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ResearchPlayerTracker {

    public ResearchPlayerTracker() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.getPlayer().world.isRemote) {
            MekanismResearch.playerStateResearch.clearPlayer(event.getPlayer().getUniqueID());
        }
    }
}
