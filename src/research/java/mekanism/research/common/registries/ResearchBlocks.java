package mekanism.research.common.registries;

import mekanism.api.tier.ITier;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.interfaces.IHasDescription;
import mekanism.common.block.prefab.BlockBasicMultiblock;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.block.BlockParticleAcceleratorCasing;
import mekanism.research.common.item.block.ItemBlockParticleAcceleratorCasing;
import mekanism.research.common.tile.TileEntityResearchTerminal;
import mekanism.research.common.tile.accelerator.TileEntityParticleAcceleratorCasing;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

import java.util.function.Function;
import java.util.function.Supplier;

public class ResearchBlocks {
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(MekanismResearch.MODID);

    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityParticleAcceleratorCasing>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityParticleAcceleratorCasing>>> PARTICLE_ACCELERATOR_CASING = registerTooltipBlock("particle_accelerator_casing", () -> new BlockBasicMultiblock<>(ResearchBlockTypes.PARTICLE_ACCELERATOR_CASING));

    public static final BlockRegistryObject<BlockTile<TileEntityResearchTerminal, BlockTypeTile<TileEntityResearchTerminal>>, ItemBlockTooltip<BlockTile<TileEntityResearchTerminal, BlockTypeTile<TileEntityResearchTerminal>>>> RESEARCH_TERMINAL = registerTooltipBlock("research_terminal", () -> new BlockTile<>(ResearchBlockTypes.RESEARCH_TERMINAL));

    private static BlockRegistryObject<BlockParticleAcceleratorCasing, ItemBlockParticleAcceleratorCasing> registerAcceleratorCasing(BlockTypeTile<TileEntityParticleAcceleratorCasing> type) {
        return registerTieredMultiBlock(type.get(AttributeTier.class).getTier(), "_particle_accelerator_casing",  () -> new BlockParticleAcceleratorCasing(type), ItemBlockParticleAcceleratorCasing::new);
    }

    private static <BLOCK extends BlockBasicMultiblock, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredMultiBlock(ITier tier, String suffix, Supplier<? extends BLOCK> blockSupplier, Function<BLOCK, ITEM> itemCreator) {
        return BLOCKS.register(tier.getBaseTier().getLowerName() + suffix, blockSupplier, itemCreator);
    }

    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockTooltip<BLOCK>> registerTooltipBlock(String name, Supplier<BLOCK> blockCreator) {
        return BLOCKS.registerDefaultProperties(name, blockCreator, ItemBlockTooltip::new);
    }
}
