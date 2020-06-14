package mekanism.research.common.registries;

import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.tile.TileEntityResearchTerminal;
import mekanism.research.common.tile.accelerator.TileEntityParticleAcceleratorCasing;

public class ResearchTileEntityTypes {
    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(MekanismResearch.MODID);

    // Particle Accelerator
    public static final TileEntityTypeRegistryObject<TileEntityParticleAcceleratorCasing> BASIC_PARTICLE_ACCELERATOR_CASING = TILE_ENTITY_TYPES.register(ResearchBlocks.BASIC_PARTICLE_ACCELERATOR_CASING, () -> new TileEntityParticleAcceleratorCasing(ResearchBlocks.BASIC_PARTICLE_ACCELERATOR_CASING));
    public static final TileEntityTypeRegistryObject<TileEntityParticleAcceleratorCasing> ADVANCED_PARTICLE_ACCELERATOR_CASING = TILE_ENTITY_TYPES.register(ResearchBlocks.ADVANCED_PARTICLE_ACCELERATOR_CASING, () -> new TileEntityParticleAcceleratorCasing(ResearchBlocks.ADVANCED_PARTICLE_ACCELERATOR_CASING));
    public static final TileEntityTypeRegistryObject<TileEntityParticleAcceleratorCasing> ELITE_PARTICLE_ACCELERATOR_CASING = TILE_ENTITY_TYPES.register(ResearchBlocks.ELITE_PARTICLE_ACCELERATOR_CASING, () -> new TileEntityParticleAcceleratorCasing(ResearchBlocks.ELITE_PARTICLE_ACCELERATOR_CASING));
    public static final TileEntityTypeRegistryObject<TileEntityParticleAcceleratorCasing> ULTIMATE_PARTICLE_ACCELERATOR_CASING = TILE_ENTITY_TYPES.register(ResearchBlocks.ULTIMATE_PARTICLE_ACCELERATOR_CASING, () -> new TileEntityParticleAcceleratorCasing(ResearchBlocks.ULTIMATE_PARTICLE_ACCELERATOR_CASING));
    public static final TileEntityTypeRegistryObject<TileEntityParticleAcceleratorCasing> CREATIVE_PARTICLE_ACCELERATOR_CASING = TILE_ENTITY_TYPES.register(ResearchBlocks.CREATIVE_PARTICLE_ACCELERATOR_CASING, () -> new TileEntityParticleAcceleratorCasing(ResearchBlocks.CREATIVE_PARTICLE_ACCELERATOR_CASING));

    // Research Terminal
    public static final TileEntityTypeRegistryObject<TileEntityResearchTerminal> RESEARCH_TERMINAL = TILE_ENTITY_TYPES.register(ResearchBlocks.RESEARCH_TERMINAL, TileEntityResearchTerminal::new);
}
