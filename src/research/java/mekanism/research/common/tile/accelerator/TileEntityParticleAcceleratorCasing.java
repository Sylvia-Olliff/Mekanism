package mekanism.research.common.tile.accelerator;

import mekanism.api.providers.IBlockProvider;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.content.accelerator.ParticleAcceleratorMultiblockData;
import mekanism.research.common.registries.ResearchBlocks;
import mekanism.research.common.tier.AcceleratorTier;

public class TileEntityParticleAcceleratorCasing extends TileEntityMultiblock<ParticleAcceleratorMultiblockData> {

    private AcceleratorTier tier;

    public TileEntityParticleAcceleratorCasing(IBlockProvider blockProvider) { super(blockProvider); }

    @Override
    protected void presetVariables() {
        tier = Attribute.getTier(getBlockType(), AcceleratorTier.class);
    }

    @Override
    public ParticleAcceleratorMultiblockData createMultiblock() {
        return new ParticleAcceleratorMultiblockData(this);
    }

    @Override
    public MultiblockManager<ParticleAcceleratorMultiblockData> getManager() {
        return MekanismResearch.particleAcceleratorManager;
    }

    public AcceleratorTier getTier() { return tier; }
}
