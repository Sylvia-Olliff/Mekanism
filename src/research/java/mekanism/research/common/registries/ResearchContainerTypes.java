package mekanism.research.common.registries;

import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.tile.TileEntityResearchTerminal;

public class ResearchContainerTypes {

    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(MekanismResearch.MODID);

    // Blocks
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityResearchTerminal>> RESEARCH_TERMINAL = CONTAINER_TYPES.register(ResearchBlocks.RESEARCH_TERMINAL, TileEntityResearchTerminal.class);
}
