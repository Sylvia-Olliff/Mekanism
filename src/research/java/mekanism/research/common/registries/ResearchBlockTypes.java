package mekanism.research.common.registries;

import mekanism.common.block.attribute.AttributeEnergy;
import mekanism.common.block.attribute.AttributeStateFacing;
import mekanism.common.block.attribute.Attributes.AttributeMultiblock;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.BlockTypeTile.BlockTileBuilder;
import mekanism.research.common.ResearchLang;
import mekanism.research.common.config.MekanismResearchConfig;
import mekanism.research.common.tile.accelerator.TileEntityParticleAcceleratorBlock;

public class ResearchBlockTypes {

    // Particle Accelerator Casing
    public static final BlockTypeTile<TileEntityParticleAcceleratorBlock> PARTICLE_ACCELERATOR_CASING = BlockTileBuilder
            .createBlock(() -> ResearchTileEntityTypes.PARTICLE_ACCELERATOR_CASING, ResearchLang.DESCRIPTION_PARTICLE_ACCELERATOR_CASING)
            .withEnergyConfig(MekanismResearchConfig.research.accelBasicPowerPerBlock, MekanismResearchConfig.research.accelBasicPowerPerBlock) // TODO: Add a separate config for storage
            .with(new AttributeMultiblock(), new AttributeStateFacing())
            .build();
}
