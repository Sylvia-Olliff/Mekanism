package mekanism.research.common.tile.accelerator;

import mekanism.api.providers.IBlockProvider;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.content.accelerator.ParticleAcceleratorMultiblockData;
import mekanism.research.common.registries.ResearchBlocks;

public class TileEntityParticleAcceleratorBlock extends TileEntityMultiblock<ParticleAcceleratorMultiblockData> {

    public TileEntityParticleAcceleratorBlock() { this(ResearchBlocks.PARTICLE_ACCELERATOR_CASING); }

    public TileEntityParticleAcceleratorBlock(IBlockProvider blockProvider) { super(blockProvider); }

    @Override
    public ParticleAcceleratorMultiblockData createMultiblock() {
        return new ParticleAcceleratorMultiblockData(this);
    }

    @Override
    public FormationProtocol<ParticleAcceleratorMultiblockData> getFormationProtocol() {
        return null;
    }

    @Override
    public MultiblockManager<ParticleAcceleratorMultiblockData> getManager() {
        return MekanismResearch.particleAcceleratorManager;
    }
}
