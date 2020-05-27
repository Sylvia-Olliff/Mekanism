package mekanism.research.common.registries;

import mekanism.common.block.interfaces.IHasDescription;
import mekanism.common.block.prefab.BlockBasicMultiblock;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.tile.accelerator.TileEntityParticleAcceleratorBlock;
import net.minecraft.block.Block;

import java.util.function.Supplier;

public class ResearchBlocks {
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(MekanismResearch.MODID);

    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityParticleAcceleratorBlock>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityParticleAcceleratorBlock>>> PARTICLE_ACCELERATOR_CASING = registerTooltipBlock("particle_accelerator_casing", () -> new BlockBasicMultiblock<>(ResearchBlockTypes.PARTICLE_ACCELERATOR_CASING));

    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockTooltip<BLOCK>> registerTooltipBlock(String name, Supplier<BLOCK> blockCreator) {
        return BLOCKS.registerDefaultProperties(name, blockCreator, ItemBlockTooltip::new);
    }
}
