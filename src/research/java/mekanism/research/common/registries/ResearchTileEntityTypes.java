package mekanism.research.common.registries;

import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.tile.accelerator.TileEntityParticleAcceleratorBlock;

public class ResearchTileEntityTypes {
    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(MekanismResearch.MODID);

    // Particle Accelerator
    public static final TileEntityTypeRegistryObject<TileEntityParticleAcceleratorBlock> PARTICLE_ACCELERATOR_CASING = TILE_ENTITY_TYPES.register(ResearchBlocks.PARTICLE_ACCELERATOR_CASING, TileEntityParticleAcceleratorBlock::new);
}
