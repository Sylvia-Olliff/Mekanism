package mekanism.research.common.content.accelerator;

import mekanism.api.Action;
import mekanism.api.inventory.AutomationType;
import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.inventory.container.sync.dynamic.ContainerSync;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.tier.AcceleratorTier;
import mekanism.research.common.tile.accelerator.TileEntityParticleAcceleratorCasing;
import net.minecraft.world.World;

import java.util.UUID;

public class ParticleAcceleratorMultiblockData extends MultiblockData {
    private static final FloatingLong MAX_ENERGY = FloatingLong.createConst(500_000_000);
    private static final int TICK_FREQUENCY = 50;
    private long ticks;
    private AcceleratorTier tier;
    private final UUID ownerId;
    private boolean isOperating;

    private final FloatingLong powerConsumptionRate;
    private final long researchGenerationRate;

    @ContainerSync
    public BasicEnergyContainer energyContainer;

    public ParticleAcceleratorMultiblockData(TileEntityParticleAcceleratorCasing tile) {
        super(tile);

        energyContainers.add(energyContainer = BasicEnergyContainer.output(MAX_ENERGY, this));
        ticks = 0L;
        isOperating = false;
        ownerId = tile.getSecurity().getOwnerUUID();

        tier = tile.getTier(); // TODO: Breakpoint here to determine what exactly is null
        powerConsumptionRate = tier.getPowerConsumption().copy().multiply(tile.getStructure().size());
        researchGenerationRate = tier.getResearchGeneration() * tile.getStructure().size();
    }

    @Override
    public boolean tick(World world) {
        ++ticks;

        if (energyContainer.isEmpty() || energyContainer.extract(powerConsumptionRate, Action.SIMULATE, AutomationType.INTERNAL).smallerOrEqual(FloatingLong.ZERO))
        {
            //TODO: Send notifications as necessary that the accelerator is no longer operating.
            if (isOperating)
                isOperating = false;

            return super.tick(world);
        }
        else
        {
            //TODO: Send notifications as necessary that the accelerator is now operating.
            if (!isOperating)
                isOperating = true;

            if (energyContainer.extract(powerConsumptionRate, Action.EXECUTE, AutomationType.INTERNAL).isZero())
                isOperating = false;
        }

        if (isOperating && ticks % TICK_FREQUENCY == 0) {
            MekanismResearch.playerStateResearch.getPlayerResearch(ownerId).addPoints(researchGenerationRate);
            ticks = 0L;
        }

        return super.tick(world);
    }
}
