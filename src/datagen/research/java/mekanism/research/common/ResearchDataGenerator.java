package mekanism.research.common;

import mekanism.research.client.ResearchLangProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = MekanismResearch.MODID, bus = Bus.MOD)
public class ResearchDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if (event.includeClient()) {
            //Client side Data Generators
            gen.addProvider(new ResearchLangProvider(gen));
        }
        if (event.includeServer()) {
            //Server side Data Generators
        }
    }
}
