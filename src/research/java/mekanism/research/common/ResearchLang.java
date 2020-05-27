package mekanism.research.common;

import mekanism.api.text.ILangEntry;
import net.minecraft.util.Util;

public enum ResearchLang implements ILangEntry {
    // GUI
    POWER("gui", "power"),
    DESCRIPTION_PARTICLE_ACCELERATOR_CASING("description", "particle_accelerator_casing");

    private final String key;

    ResearchLang(String type, String path) {
        this(Util.makeTranslationKey(type, MekanismResearch.rl(path)));
    }

    ResearchLang(String key) {
        this.key = key;
    }

    @Override
    public String getTranslationKey() {
        return key;
    }
}
