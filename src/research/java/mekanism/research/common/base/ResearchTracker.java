package mekanism.research.common.base;

import com.sun.javaws.exceptions.InvalidArgumentException;
import mekanism.api.math.FloatingLong;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.network.PacketResearchUpdate;
import net.minecraft.world.IWorld;

import java.util.UUID;

public class ResearchTracker {

    //TODO: Add NBT saving for player research data.
    //TODO: Add tracking of unlocked research.
    private static final FloatingLong MAX_RESEARCH_POINTS = FloatingLong.createConst(500_000_000L);

    private final UUID playerId;
    private final IWorld world;
    private final Object lockObj = new Object();

    private FloatingLong researchPoints;

    public ResearchTracker(UUID uuid, IWorld world) {
        this.playerId = uuid;
        this.researchPoints = FloatingLong.ZERO;
        this.world = world;
    }

    public ResearchTracker(UUID uuid, IWorld world, FloatingLong points) {
        this.playerId = uuid;
        this.world = world;
        this.researchPoints = points;
    }

    public FloatingLong getResearchPoints() { return researchPoints; }

    public UUID getPlayerId() { return playerId; }

    public void setResearchPoints(FloatingLong points) {
        this.researchPoints = points;
    }

    public void addPoints(FloatingLong points) {
        synchronized (lockObj) {
            if (researchPoints.greaterOrEqual(MAX_RESEARCH_POINTS))
                return;

            if (researchPoints.longValue() + points.longValue() >= MAX_RESEARCH_POINTS.longValue())
                researchPoints = MAX_RESEARCH_POINTS;
            else
                researchPoints.add(points);

            if (!this.world.isRemote()) {
                MekanismResearch.packetHandler.sendToAllTracking(new PacketResearchUpdate(playerId, points), world.getPlayerByUuid(playerId));
            }
        }
    }

    public boolean canResearch(FloatingLong pointCost) { return pointCost.smallerOrEqual(researchPoints); }

    public void spendPoints(FloatingLong pointCost) {
        synchronized (lockObj) {
            researchPoints.subtract(pointCost);

            if (!this.world.isRemote()) {
                MekanismResearch.packetHandler.sendToAllTracking(new PacketResearchUpdate(playerId, FloatingLong.create(0).subtract(pointCost)), world.getPlayerByUuid(playerId));
            }
        }
    }
}
