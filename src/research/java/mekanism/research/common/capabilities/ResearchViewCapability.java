package mekanism.research.common.capabilities;

import mekanism.research.common.capabilities.interfaces.IResearchView;
import net.minecraft.entity.player.PlayerEntity;

import java.util.concurrent.atomic.AtomicLong;

public class ResearchViewCapability implements IResearchView {

    /**
     * Called when a player right clicks on/with the block/item that supports this capability.
     * Retrieves from the player's ResearchPlayerCapability (if present) to display their current
     * available research points
     * @param player - Player currently interacting with block/item
     * @return - current available research points
     */
    @Override
    public long getPlayerResearch(PlayerEntity player) {
        if (!player.world.isRemote) {
            AtomicLong points = new AtomicLong(0L);
            player.getCapability(ResearchCapabilityProvider.RESEARCH_PLAYER_CAPABILITY).ifPresent(c -> {
                points.set(c.getResearchPoints());
            });
            return points.get();
        }
        return 0L;
    }
}
