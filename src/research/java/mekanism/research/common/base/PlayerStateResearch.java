package mekanism.research.common.base;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.IWorld;

import java.util.Set;
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
    // Particle Accelerator state tracking
    //
    // ----------------------

    public void setAcceleratorState(UUID uuid, int acceleratorId, boolean isActive, boolean isLocal) {
        boolean changed;

        if (!researchTrackers.containsKey(uuid))
        {
            researchTrackers.put(uuid, new ResearchTracker(uuid, acceleratorId));
            changed = true;
        }
        else
        {
            changed = isActive != researchTrackers.get(uuid).isAcceleratorActive(acceleratorId);
            researchTrackers.get(uuid).setAccelerator(acceleratorId);
        }

        // If something changed and we're in a remote world, take appropriate action
        if (changed && world.isRemote()) {
            // If the player is the "local" player, we need to tell the server the state has changed
            if (isLocal) {
                //TODO: Send an appropriate packet update to the server
            }

            //TODO: check config settings and play active accelerator sounds
        }
    }

    public TreeMap<UUID, ResearchTracker> getResearchTrackers() { return researchTrackers; }

    public ResearchTracker getPlayerResearch(PlayerEntity p) { return researchTrackers.get(p.getUniqueID()); }

    public boolean isAcceleratorActive(PlayerEntity p, int acceleratorId) { return (researchTrackers.containsKey(p.getUniqueID()) && researchTrackers.get(p.getUniqueID()).isAcceleratorActive(acceleratorId)); }
}
