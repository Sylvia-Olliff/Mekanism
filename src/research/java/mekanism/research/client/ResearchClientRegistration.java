package mekanism.research.client;

import mekanism.client.ClientRegistrationUtil;
import mekanism.research.client.gui.GuiResearchTerminal;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.registries.ResearchContainerTypes;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MekanismResearch.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        ClientRegistrationUtil.registerScreen(ResearchContainerTypes.RESEARCH_TERMINAL, GuiResearchTerminal::new);
    }
}
