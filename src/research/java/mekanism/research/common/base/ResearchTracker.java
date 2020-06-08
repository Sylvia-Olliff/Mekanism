package mekanism.research.common.base;

import mekanism.research.common.MekanismResearch;
import mekanism.research.common.network.PacketResearchUpdate;
import net.minecraft.world.IWorld;

import java.util.UUID;

public class ResearchTracker {

    //TODO: Add NBT saving for player research data.
    //TODO: Add tracking of unlocked research.
    private static final long MAX_RESEARCH_POINTS = 500_000_000L;

    private final UUID playerId;
    private final IWorld world;
    private final Object lockObj = new Object();

    private long researchPoints;

    public ResearchTracker(UUID uuid, IWorld world) {
        this.playerId = uuid;
        this.researchPoints = 0L;
        this.world = world;
    }

    public ResearchTracker(UUID uuid, IWorld world, long points) {
        this.playerId = uuid;
        this.world = world;
        this.researchPoints = points;
    }

    public long getResearchPoints() { return researchPoints; }

    public UUID getPlayerId() { return playerId; }

    public void setResearchPoints(long points) {
        this.researchPoints = points;
    }

    public void addPoints(long points) {
        synchronized (lockObj) {
            if (researchPoints >= MAX_RESEARCH_POINTS)
                return;

            if (researchPoints + points >= MAX_RESEARCH_POINTS)
                researchPoints = MAX_RESEARCH_POINTS;
            else
                researchPoints += points;

            if (!this.world.isRemote()) {
                MekanismResearch.packetHandler.sendToAllTracking(new PacketResearchUpdate(playerId, points), world.getPlayerByUuid(playerId));
            }
        }
    }

    public boolean canResearch(long pointCost) { return pointCost < researchPoints; }

    public void spendPoints(long pointCost) {
        synchronized (lockObj) {
            if (canResearch(pointCost)) {
                researchPoints -= pointCost;

                if (!this.world.isRemote()) {
                    MekanismResearch.packetHandler.sendToAllTracking(new PacketResearchUpdate(playerId, -pointCost), world.getPlayerByUuid(playerId));
                }
            }
        }
    }
}
