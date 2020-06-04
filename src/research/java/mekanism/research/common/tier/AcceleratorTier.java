package mekanism.research.common.tier;

import mekanism.api.math.FloatingLong;
import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;
import mekanism.common.config.value.CachedFloatingLongValue;

public enum AcceleratorTier implements ITier {
    BASIC(BaseTier.BASIC, FloatingLong.create(50), FloatingLong.create(10)),
    ADVANCED(BaseTier.ADVANCED, FloatingLong.create(80), FloatingLong.create(14)),
    ELITE(BaseTier.ELITE, FloatingLong.create(128), FloatingLong.create(19.6)),
    ULTIMATE(BaseTier.ULTIMATE, FloatingLong.create(204.8), FloatingLong.create(27.44)),
    CREATIVE(BaseTier.CREATIVE, FloatingLong.ZERO, FloatingLong.MAX_VALUE);

    private final FloatingLong basePowerConsumption;
    private CachedFloatingLongValue powerReference;
    private final FloatingLong baseResearchGeneration;
    private CachedFloatingLongValue researchReference;

    private final BaseTier baseTier;

    AcceleratorTier(BaseTier tier, FloatingLong power, FloatingLong research) {
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

    public FloatingLong getResearchGeneration() { return researchReference == null ? getBaseResearchGeneration() : researchReference.get(); }

    private FloatingLong getBaseResearchGeneration() { return baseResearchGeneration; }

    /**
     * ONLY CALL This from ResearchConfig! This is to give a reference to the actual config value
     * @param power
     * @param research
     */
    public void setConfigReference(CachedFloatingLongValue power, CachedFloatingLongValue research) {
        this.powerReference = power;
        this.researchReference = research;
    }
}
