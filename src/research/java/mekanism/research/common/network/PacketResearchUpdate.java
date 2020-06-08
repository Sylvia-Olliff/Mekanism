package mekanism.research.common.network;

import mekanism.common.network.BasePacketHandler;
import mekanism.research.common.MekanismResearch;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import java.util.UUID;
import java.util.function.Supplier;

public class PacketResearchUpdate {

    private final UUID uuid;
    private final long points;

    public PacketResearchUpdate(UUID uuid, long points) {
        this.uuid = uuid;
        this.points = points;
    }

    public static void handle(PacketResearchUpdate message, Supplier<Context> context) {
        PlayerEntity player = BasePacketHandler.getPlayer(context);
        if (player == null)
            return;

        context.get().enqueueWork(() -> {
            MekanismResearch.playerStateResearch.getPlayerResearch(player).addPoints(message.points);

            if (!player.world.isRemote) {
                MekanismResearch.packetHandler.sendToAllTracking(new PacketResearchPlayerData(message.uuid), player);
            }
        });
        context.get().setPacketHandled(true);
    }

    public static void encode(PacketResearchUpdate pkt, PacketBuffer buf) {
        buf.writeUniqueId(pkt.uuid);
        buf.writeLong(pkt.points);
    }

    public static PacketResearchUpdate decode(PacketBuffer buf) {
        return new PacketResearchUpdate(buf.readUniqueId(), buf.readLong());
    }
}
