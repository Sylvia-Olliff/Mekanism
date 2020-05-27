package mekanism.research.common;

import mekanism.common.Mekanism;
import mekanism.common.base.IModule;
import mekanism.common.config.MekanismModConfig;
import mekanism.common.lib.Version;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.research.common.config.MekanismResearchConfig;
import mekanism.research.common.content.accelerator.ParticleAcceleratorCache;
import mekanism.research.common.content.accelerator.ParticleAcceleratorMultiblockData;
import mekanism.research.common.content.accelerator.ParticleAcceleratorStructureValidator;
import mekanism.research.common.registries.ResearchBlocks;
import mekanism.research.common.registries.ResearchTileEntityTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MekanismResearch.MODID)
public class MekanismResearch implements IModule {

    public static final String MODID = "mekanismresearch";

    public static MekanismResearch instance;

    /**
     * MekanismResearch version number
     */
    public final Version versionNumber;

    public static MultiblockManager<ParticleAcceleratorMultiblockData> particleAcceleratorManager = new MultiblockManager<>("particleAccelerator", ParticleAcceleratorCache::new, ParticleAcceleratorStructureValidator::new);

    public MekanismResearch() {
        Mekanism.modulesLoaded.add(instance = this);
        MekanismResearchConfig.registerConfigs(ModLoadingContext.get());
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onConfigLoad);

        ResearchBlocks.BLOCKS.register(modEventBus);
        ResearchTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);


        //Set our version number to match the mods.toml file, which matches the one in our build.gradle
        versionNumber = new Version(ModLoadingContext.get().getActiveContainer().getModInfo().getVersion());
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MekanismResearch.MODID, path);
    }

    public void commonSetup(FMLCommonSetupEvent event) {

        MinecraftForge.EVENT_BUS.register(this);

        //Finalization
        Mekanism.logger.info("Loaded 'Mekanism Research' module.");
    }

    @Override
    public Version getVersion() {
        return versionNumber;
    }

    @Override
    public String getName() {
        return "Research";
    }

    @Override
    public void resetClient() {

    }

    private void onConfigLoad(ModConfig.ModConfigEvent configEvent) {
        //Note: We listen to both the initial load and the reload, so as to make sure that we fix any accidentally
        // cached values from calls before the initial loading
        ModConfig config = configEvent.getConfig();
        //Make sure it is for the same modid as us
        if (config.getModId().equals(MODID) && config instanceof MekanismModConfig) {
            ((MekanismModConfig) config).clearCache();
        }
    }
}