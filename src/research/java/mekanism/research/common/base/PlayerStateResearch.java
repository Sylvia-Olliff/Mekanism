package mekanism.research.common.base;

import mekanism.api.math.FloatingLong;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.IWorld;
import java.util.TreeMap;
import java.util.UUID;

public class PlayerStateResearch {

    private final TreeMap<UUID, ResearchTracker> researchTrackers = new TreeMap<>();

    private IWorld world;

    public void clear() {
        researchTrackers.clear();
    }

    public void clearPlayer(UUID uuid) {
        researchTrackers.remove(uuid);
    }

    public void init(IWorld world) { this.world = world; }

    // ----------------------
    //
    // Research Player state tracking
    //
    // ----------------------

    public void setResearchPlayerState(UUID uuid, long currentPoints, boolean isLocal) {

        if (!researchTrackers.containsKey(uuid))
            researchTrackers.put(uuid, new ResearchTracker(uuid, this.world, currentPoints));
        else
            researchTrackers.get(uuid).setResearchPoints(currentPoints);

        if (world.isRemote() && isLocal) {
            // If the player is the "local" player, we need to tell the server the state has changed

            //TODO: Send an appropriate packet update to the server
        }
    }

    public TreeMap<UUID, ResearchTracker> getResearchTrackers() { return researchTrackers; }

    public ResearchTracker getPlayerResearch(PlayerEntity p) {
        if (!researchTrackers.containsKey(p.getUniqueID()))
            researchTrackers.put(p.getUniqueID(), new ResearchTracker(p.getUniqueID(), this.world, FloatingLong.ZERO));

        return researchTrackers.get(p.getUniqueID());
    }

    public ResearchTracker getPlayerResearch(UUID uuid) {
        if (!researchTrackers.containsKey(uuid))
            researchTrackers.put(uuid, new ResearchTracker(uuid, this.world, FloatingLong.ZERO));

        return researchTrackers.get(uuid);
    }
}
