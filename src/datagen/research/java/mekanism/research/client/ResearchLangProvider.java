package mekanism.research.client;

import mekanism.client.lang.BaseLanguageProvider;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.ResearchLang;
import mekanism.research.common.registries.ResearchBlocks;
import net.minecraft.data.DataGenerator;

public class ResearchLangProvider extends BaseLanguageProvider {

    public ResearchLangProvider(DataGenerator gen) { super(gen, MekanismResearch.MODID); }

    @Override
    protected void addTranslations() {
        addItems();
        addBlocks();
        addMisc();
    }

    private void addItems() {

    }

    private void addBlocks() {
        add(ResearchBlocks.PARTICLE_ACCELERATOR_CASING, "Particle Accelerator Casing");
        add(ResearchBlocks.RESEARCH_TERMINAL, "Research Terminal");
    }

    private void addMisc() {
        add(ResearchLang.DESCRIPTION_PARTICLE_ACCELERATOR_CASING, "Reinforced Casing that can be used in the Particle Accelerator multiblock");
        add(ResearchLang.DESCRIPTION_RESEARCH_TERMINAL, "Powered Terminal for viewing research data");
    }
}
