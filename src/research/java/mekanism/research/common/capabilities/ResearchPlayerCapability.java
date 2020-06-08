package mekanism.research.common.capabilities;

import mcp.MethodsReturnNonnullByDefault;
import mekanism.api.Action;
import mekanism.api.IContentsListener;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ResearchPlayerCapability implements INBTSerializable<CompoundNBT>, IContentsListener {

    private final long MAX_RESEARCH_POINTS;
    @Nullable
    private final IContentsListener listener;
    private long researchPoints;

    public ResearchPlayerCapability(long maxResearch, @Nullable IContentsListener listener) {
        this.MAX_RESEARCH_POINTS = maxResearch;
        this.listener = listener;
    }

    public long getResearchPoints() { return researchPoints; }

    public void setResearchPoints(long points) {
        researchPoints = Math.min(points, MAX_RESEARCH_POINTS);
        onContentsChanged();
    }

    public long addPoints(long amount, Action action) {
        if (amount + researchPoints > MAX_RESEARCH_POINTS) {
            if (action.execute())
            {
                researchPoints = MAX_RESEARCH_POINTS;
                onContentsChanged();
            }
            return (amount + researchPoints) - MAX_RESEARCH_POINTS;
        }

        if (action.execute())
        {
            researchPoints += amount;
            onContentsChanged();
        }
        return 0L;
    }


    public long removePoints(long amount, Action action) {
        if (researchPoints - amount < 0L) {
            long pointsRemoved = researchPoints;
            if (action.execute())
            {
                researchPoints = 0L;
                onContentsChanged();
            }

            return pointsRemoved;
        }

        if (action.execute())
        {
            researchPoints -= amount;
            onContentsChanged();
        }

        return amount;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        if (researchPoints != 0) {
            nbt.putLong("research", researchPoints);
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.contains("research", NBT.TAG_LONG))
            setResearchPoints(nbt.getLong("research"));
        else
            setResearchPoints(0L);
    }

    @Override
    public void onContentsChanged() {
        if (listener != null) {
            listener.onContentsChanged();
        }
    }
}
