package mekanism.research.common;

import mekanism.research.common.capabilities.ResearchCapabilityProvider;
import mekanism.research.common.network.PacketResearchPlayerData;
import net.minecraft.entity.player.ServerPlayerEntity;
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

    @SubscribeEvent
    public void onPlayerLoginEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().world.isRemote) {
            event.getPlayer().getCapability(ResearchCapabilityProvider.RESEARCH_PLAYER_CAPABILITY).ifPresent(c -> PacketResearchPlayerData.sync((ServerPlayerEntity) event.getPlayer()));
        }
    }
}
