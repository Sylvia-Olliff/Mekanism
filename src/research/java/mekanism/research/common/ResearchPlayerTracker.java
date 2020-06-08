package mekanism.research.common;

import mekanism.research.common.capabilities.ResearchCapabilityProvider;
import mekanism.research.common.network.PacketResearchPlayerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ResearchPlayerTracker {

    public ResearchPlayerTracker() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        MekanismResearch.playerStateResearch.clearPlayer(event.getPlayer().getUniqueID());
    }

    @SubscribeEvent
    public void onPlayerLoginEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().world.isRemote) {
            event.getPlayer().getCapability(ResearchCapabilityProvider.RESEARCH_PLAYER_CAPABILITY).ifPresent(c -> PacketResearchPlayerData.sync((ServerPlayerEntity) event.getPlayer()));
        }
    }

    @SubscribeEvent
    public void onPlayerDimChangedEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        MekanismResearch.playerStateResearch.clearPlayer(event.getPlayer().getUniqueID());
        event.getPlayer().getCapability(ResearchCapabilityProvider.RESEARCH_PLAYER_CAPABILITY).ifPresent(c -> PacketResearchPlayerData.sync((ServerPlayerEntity) event.getPlayer()));
    }

    @SubscribeEvent
    public void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(MekanismResearch.rl("research_capability"), new ResearchCapabilityProvider());
        }
    }
}
