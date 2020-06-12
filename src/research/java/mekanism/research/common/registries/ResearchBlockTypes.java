package mekanism.research.common.registries;

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

    // Particle Accelerator Casing
    public static final BlockTypeTile<TileEntityParticleAcceleratorCasing> PARTICLE_ACCELERATOR_CASING = BlockTileBuilder
            .createBlock(() -> ResearchTileEntityTypes.PARTICLE_ACCELERATOR_CASING, ResearchLang.DESCRIPTION_PARTICLE_ACCELERATOR_CASING)
            .withEnergyConfig(MekanismResearchConfig.research.accelBasicPowerPerBlock, MekanismResearchConfig.research.accelBasicPowerPerBlock) // TODO: Add a separate config for storage
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
