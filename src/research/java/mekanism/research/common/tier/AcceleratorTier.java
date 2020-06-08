package mekanism.research.common.tier;

import mekanism.api.math.FloatingLong;
import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;
import mekanism.common.config.value.CachedFloatingLongValue;
import mekanism.common.config.value.CachedLongValue;

public enum AcceleratorTier implements ITier {
    BASIC(BaseTier.BASIC, FloatingLong.create(50), 10L),
    ADVANCED(BaseTier.ADVANCED, FloatingLong.create(80), 14L),
    ELITE(BaseTier.ELITE, FloatingLong.create(128), 20L),
    ULTIMATE(BaseTier.ULTIMATE, FloatingLong.create(204.8), 28L),
    CREATIVE(BaseTier.CREATIVE, FloatingLong.ZERO, 250_000_000L);

    private final FloatingLong basePowerConsumption;
    private CachedFloatingLongValue powerReference;
    private final long baseResearchGeneration;
    private CachedLongValue researchReference;

    private final BaseTier baseTier;

    AcceleratorTier(BaseTier tier, FloatingLong power, long research) {
        this.baseTier = tier;
        this.basePowerConsumption = power;
        this.baseResearchGeneration = research;
    }

    @Override
    public BaseTier getBaseTier() {
        return baseTier;
    }

    public FloatingLong getPowerConsumption() { return powerReference == null ? getBasePowerConsumption() : powerReference.get(); }

    private FloatingLong getBasePowerConsumption() { return basePowerConsumption; }

    public long getResearchGeneration() { return researchReference == null ? getBaseResearchGeneration() : researchReference.get(); }

    private long getBaseResearchGeneration() { return baseResearchGeneration; }

    /**
     * ONLY CALL This from ResearchConfig! This is to give a reference to the actual config value
     * @param power
     * @param research
     */
    public void setConfigReference(CachedFloatingLongValue power, CachedLongValue research) {
        this.powerReference = power;
        this.researchReference = research;
    }
}
