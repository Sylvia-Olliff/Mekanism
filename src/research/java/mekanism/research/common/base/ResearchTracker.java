package mekanism.research.common.base;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ResearchTracker {

    private static final long MAX_RESEARCH_POINTS = 500_000_000L;

    private final UUID playerId;
    private final Set<Integer> accelerators = new ObjectOpenHashSet<>();

    private long researchPoints;

    public ResearchTracker(UUID uuid) {
        playerId = uuid;
        researchPoints = 0L;
    }

    public ResearchTracker(UUID uuid, int acceleratorId) {
        playerId = uuid;
        researchPoints = 0L;
        accelerators.add(acceleratorId);
    }

    public void clear() {
        // instance will only ever be for a single player so don't clear that value
        researchPoints = 0L;
        accelerators.clear();
    }

    public boolean isAcceleratorActive(int acceleratorId) { return accelerators.contains(acceleratorId); }

    public void setAccelerator(int acceleratorId) {
        if (accelerators.contains(acceleratorId))
            accelerators.remove(acceleratorId);
        else
            accelerators.add(acceleratorId);
    }

    public UUID getPlayerId() { return playerId; }

    public long getResearchPoints() { return researchPoints; }

    public boolean hasEnoughPoints(long cost) { return cost <= researchPoints; }

    public void spendPoints(long cost) { researchPoints -= cost; }

    public void genPoints(long points) {

        if (researchPoints >= MAX_RESEARCH_POINTS)
            return;

        if ((researchPoints + points) >= MAX_RESEARCH_POINTS)
            researchPoints = MAX_RESEARCH_POINTS;
        else
            researchPoints += points;
    }
}
