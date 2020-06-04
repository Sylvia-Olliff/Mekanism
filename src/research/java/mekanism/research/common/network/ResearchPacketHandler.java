package mekanism.research.common.network;

import mekanism.common.Mekanism;
import mekanism.common.network.BasePacketHandler;
import mekanism.research.common.MekanismResearch;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ResearchPacketHandler extends BasePacketHandler {
    private static final SimpleChannel netHandler = createChannel(MekanismResearch.rl(MekanismResearch.MODID));

    @Override
    protected SimpleChannel getChannel() {
        return netHandler;
    }

    @Override
    public void initialize() {
        // Client to Server messages

        // Server to Client messages
        registerServerToClient(PacketResearchPlayerData.class, PacketResearchPlayerData::encode, PacketResearchPlayerData::decode, PacketResearchPlayerData::handle);
        registerServerToClient(PacketResearchUpdate.class, PacketResearchUpdate::encode, PacketResearchUpdate::decode, PacketResearchUpdate::handle);
    }
}
