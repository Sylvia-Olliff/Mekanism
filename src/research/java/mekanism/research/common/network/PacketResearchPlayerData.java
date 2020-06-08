package mekanism.research.common.network;

import mekanism.common.network.BasePacketHandler;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.capabilities.ResearchCapabilityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketResearchPlayerData {

    private final UUID uuid;
    private final long currentPoints;

    public PacketResearchPlayerData(UUID uuid) {
        this.uuid = uuid;
        currentPoints = MekanismResearch.playerStateResearch.getPlayerResearch(uuid).getResearchPoints();
    }

    private PacketResearchPlayerData(UUID uuid, long points) {
        this.uuid = uuid;
        this.currentPoints = points;
    }

    public static void sync(ServerPlayerEntity player) {
        player.getCapability(ResearchCapabilityProvider.RESEARCH_PLAYER_CAPABILITY).ifPresent(c ->
                MekanismResearch.packetHandler.sendTo(new PacketResearchPlayerData(player.getUniqueID(), c.getResearchPoints()), player));
    }

    public static void handle(PacketResearchPlayerData message, Supplier<Context> context) {
        PlayerEntity player = BasePacketHandler.getPlayer(context);
        if (player == null)
            return;

        context.get().enqueueWork(() -> {
            MekanismResearch.playerStateResearch.setResearchPlayerState(message.uuid, message.currentPoints, false);
            player.getCapability(ResearchCapabilityProvider.RESEARCH_PLAYER_CAPABILITY).ifPresent(c -> c.setResearchPoints(message.currentPoints));

            if (!player.world.isRemote) {
                MekanismResearch.packetHandler.sendToAllTracking(new PacketResearchPlayerData(message.uuid), player);
            }
        });
        context.get().setPacketHandled(true);
    }

    public static void encode(PacketResearchPlayerData pkt, PacketBuffer buf) {
        buf.writeUniqueId(pkt.uuid);
        buf.writeLong(pkt.currentPoints);
    }

    public static PacketResearchPlayerData decode(PacketBuffer buf) {
        return new PacketResearchPlayerData(buf.readUniqueId(), buf.readLong());
    }
}
