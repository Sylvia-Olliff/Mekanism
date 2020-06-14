package mekanism.research.common.registries;

import mekanism.api.math.FloatingLongSupplier;
import mekanism.common.block.attribute.AttributeStateFacing;
import mekanism.common.block.attribute.Attributes.AttributeSecurity;
import mekanism.common.block.attribute.Attributes.AttributeMultiblock;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.BlockTypeTile.BlockTileBuilder;
import mekanism.research.common.ResearchLang;
import mekanism.research.common.config.MekanismResearchConfig;
import mekanism.research.common.content.blocktype.BlockShapes;
import mekanism.research.common.tile.TileEntityResearchTerminal;
import mekanism.research.common.tile.accelerator.TileEntityParticleAcceleratorCasing;

public class ResearchBlockTypes {

    // Basic Particle Accelerator Casing
    public static final BlockTypeTile<TileEntityParticleAcceleratorCasing> BASIC_PARTICLE_ACCELERATOR_CASING = BlockTileBuilder
            .createBlock(() -> ResearchTileEntityTypes.BASIC_PARTICLE_ACCELERATOR_CASING, ResearchLang.DESCRIPTION_PARTICLE_ACCELERATOR_CASING)
            .withEnergyConfig(MekanismResearchConfig.research.accelBasicPowerPerBlock, MekanismResearchConfig.research.accelBasicPowerPerBlock) // TODO: Add a separate config for storage
            .with(new AttributeMultiblock(), new AttributeStateFacing(), new AttributeSecurity())
            .build();

    // Advanced Particle Accelerator Casing
    public static final BlockTypeTile<TileEntityParticleAcceleratorCasing> ADVANCED_PARTICLE_ACCELERATOR_CASING = BlockTileBuilder
            .createBlock(() -> ResearchTileEntityTypes.ADVANCED_PARTICLE_ACCELERATOR_CASING, ResearchLang.DESCRIPTION_PARTICLE_ACCELERATOR_CASING)
            .withEnergyConfig(MekanismResearchConfig.research.accelAdvPowerPerBlock, MekanismResearchConfig.research.accelAdvPowerPerBlock)
            .with(new AttributeMultiblock(), new AttributeStateFacing(), new AttributeSecurity())
            .build();

    // Elite Particle Accelerator Casing
    public static final BlockTypeTile<TileEntityParticleAcceleratorCasing> ELITE_PARTICLE_ACCELERATOR_CASING = BlockTileBuilder
            .createBlock(() -> ResearchTileEntityTypes.ELITE_PARTICLE_ACCELERATOR_CASING, ResearchLang.DESCRIPTION_PARTICLE_ACCELERATOR_CASING)
            .withEnergyConfig(MekanismResearchConfig.research.accelElitePowerPerBlock, MekanismResearchConfig.research.accelElitePowerPerBlock)
            .with(new AttributeMultiblock(), new AttributeStateFacing(), new AttributeSecurity())
            .build();

    // Ultimate Particle Accelerator Casing
    public static final BlockTypeTile<TileEntityParticleAcceleratorCasing> ULTIMATE_PARTICLE_ACCELERATOR_CASING = BlockTileBuilder
            .createBlock(() -> ResearchTileEntityTypes.ULTIMATE_PARTICLE_ACCELERATOR_CASING, ResearchLang.DESCRIPTION_PARTICLE_ACCELERATOR_CASING)
            .withEnergyConfig(MekanismResearchConfig.research.accelUltimatePowerPerBlock, MekanismResearchConfig.research.accelUltimatePowerPerBlock)
            .with(new AttributeMultiblock(), new AttributeStateFacing(), new AttributeSecurity())
            .build();

    // Creative Particle Accelerator Casing
    public static final BlockTypeTile<TileEntityParticleAcceleratorCasing> CREATIVE_PARTICLE_ACCELERATOR_CASING = BlockTileBuilder
            .createBlock(() -> ResearchTileEntityTypes.CREATIVE_PARTICLE_ACCELERATOR_CASING, ResearchLang.DESCRIPTION_PARTICLE_ACCELERATOR_CASING)
            .withEnergyConfig(MekanismResearchConfig.research.accelBasicPowerPerBlock, MekanismResearchConfig.research.accelBasicPowerPerBlock)
            .with(new AttributeMultiblock(), new AttributeStateFacing(), new AttributeSecurity())
            .build();

    // Research Terminal
    public static final BlockTypeTile<TileEntityResearchTerminal> RESEARCH_TERMINAL = BlockTileBuilder
            .createBlock(() -> ResearchTileEntityTypes.RESEARCH_TERMINAL, ResearchLang.DESCRIPTION_RESEARCH_TERMINAL)
            .with(new AttributeStateFacing())
            .withGui(() -> ResearchContainerTypes.RESEARCH_TERMINAL)
            .withCustomShape(BlockShapes.RESEARCH_TERMINAL)
            .build();
}
