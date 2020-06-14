package mekanism.research.common.registries;

import mekanism.common.block.interfaces.IHasDescription;
import mekanism.common.block.prefab.BlockBasicMultiblock;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.tier.AcceleratorTier;
import mekanism.research.common.tile.TileEntityResearchTerminal;
import mekanism.research.common.tile.accelerator.TileEntityParticleAcceleratorCasing;
import net.minecraft.block.Block;

import java.util.function.Supplier;

public class ResearchBlocks {
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(MekanismResearch.MODID);

    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityParticleAcceleratorCasing>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityParticleAcceleratorCasing>>> BASIC_PARTICLE_ACCELERATOR_CASING = registerAcceleratorCasing(() -> new BlockBasicMultiblock<>(ResearchBlockTypes.BASIC_PARTICLE_ACCELERATOR_CASING), AcceleratorTier.BASIC);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityParticleAcceleratorCasing>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityParticleAcceleratorCasing>>> ADVANCED_PARTICLE_ACCELERATOR_CASING = registerAcceleratorCasing(() -> new BlockBasicMultiblock<>(ResearchBlockTypes.ADVANCED_PARTICLE_ACCELERATOR_CASING), AcceleratorTier.ADVANCED);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityParticleAcceleratorCasing>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityParticleAcceleratorCasing>>> ELITE_PARTICLE_ACCELERATOR_CASING = registerAcceleratorCasing(() -> new BlockBasicMultiblock<>(ResearchBlockTypes.ELITE_PARTICLE_ACCELERATOR_CASING), AcceleratorTier.ELITE);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityParticleAcceleratorCasing>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityParticleAcceleratorCasing>>> ULTIMATE_PARTICLE_ACCELERATOR_CASING = registerAcceleratorCasing(() -> new BlockBasicMultiblock<>(ResearchBlockTypes.ULTIMATE_PARTICLE_ACCELERATOR_CASING), AcceleratorTier.ULTIMATE);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityParticleAcceleratorCasing>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityParticleAcceleratorCasing>>> CREATIVE_PARTICLE_ACCELERATOR_CASING = registerAcceleratorCasing(() -> new BlockBasicMultiblock<>(ResearchBlockTypes.CREATIVE_PARTICLE_ACCELERATOR_CASING), AcceleratorTier.CREATIVE);

    public static final BlockRegistryObject<BlockTile<TileEntityResearchTerminal, BlockTypeTile<TileEntityResearchTerminal>>, ItemBlockTooltip<BlockTile<TileEntityResearchTerminal, BlockTypeTile<TileEntityResearchTerminal>>>> RESEARCH_TERMINAL = registerTooltipBlock("research_terminal", () -> new BlockTile<>(ResearchBlockTypes.RESEARCH_TERMINAL));

    private static BlockRegistryObject<BlockBasicMultiblock<TileEntityParticleAcceleratorCasing>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityParticleAcceleratorCasing>>> registerAcceleratorCasing(Supplier<BlockBasicMultiblock<TileEntityParticleAcceleratorCasing>> acceleratorSupplier, AcceleratorTier tier) {
        return registerTooltipBlock( tier.name().toLowerCase() + "_particle_accelerator_casing",  acceleratorSupplier);
    }

    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockTooltip<BLOCK>> registerTooltipBlock(String name, Supplier<BLOCK> blockCreator) {
        return BLOCKS.registerDefaultProperties(name, blockCreator, ItemBlockTooltip::new);
    }
}
