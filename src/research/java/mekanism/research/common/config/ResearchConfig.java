package mekanism.research.common.config;

import com.sun.javaws.exceptions.InvalidArgumentException;
import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedFloatingLongValue;
import mekanism.common.config.value.CachedLongValue;
import mekanism.research.common.base.ResearchTracker;
import mekanism.research.common.tier.AcceleratorTier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class ResearchConfig extends BaseMekanismConfig {
    private static final String ACCELERATOR_CATEGORY = "accelerator";

    private final ForgeConfigSpec configSpec;

    public final CachedFloatingLongValue accelBasicPowerPerBlock;
    public final CachedLongValue accelBasicParticleProduction;
    public final CachedFloatingLongValue accelAdvPowerPerBlock;
    public final CachedLongValue accelAdvParticleProduction;
    public final CachedFloatingLongValue accelElitePowerPerBlock;
    public final CachedLongValue accelEliteParticleProduction;
    public final CachedFloatingLongValue accelUltimatePowerPerBlock;
    public final CachedLongValue accelUltimateParticleProduction;

    ResearchConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Mekanism Research Config. This config is synced between server and client.").push("research");

        // TODO: Values require testing!
        // Power consumption calculated to increase by 60% per tier
        // Particle production calculated to increase by 40% per tier
        builder.comment("Particle Accelerator Settings").push(ACCELERATOR_CATEGORY);
        accelBasicPowerPerBlock = CachedFloatingLongValue.define(this, builder, "Amount of power consumed per block",
                "accelBasicPowerPerBlock", FloatingLong.createConst(50L));
        accelBasicParticleProduction = CachedLongValue.wrap(this, builder.comment("Particle Production (controls both material production and research production rate) per block")
                .define("accelBasicParticleProduction", 10L));
        accelAdvPowerPerBlock = CachedFloatingLongValue.define(this, builder, "Amount of power consumed per block",
                "accelAdvPowerPerBlock", FloatingLong.createConst(80L));
        accelAdvParticleProduction = CachedLongValue.wrap(this, builder.comment("Particle Production (controls both material production and research production rate) per block")
                .define("accelAdvParticleProduction", 14L));
        accelElitePowerPerBlock = CachedFloatingLongValue.define(this, builder, "Amount of power consumed per block",
                "accelElitePowerPerBlock", FloatingLong.createConst(128L));
        accelEliteParticleProduction = CachedLongValue.wrap(this, builder.comment("Particle Production (controls both material production and research production rate) per block")
                .define("accelEliteParticleProduction", 20L));
        accelUltimatePowerPerBlock = CachedFloatingLongValue.define(this, builder, "Amount of power consumed per block",
                "accelUltimatePowerPerBlock", FloatingLong.createConst(204.8));
        accelUltimateParticleProduction = CachedLongValue.wrap(this, builder.comment("Particle Production (controls both material production and research production rate) per block")
                .define("accelUltimateParticleProduction", 28L));
        builder.pop();

        builder.pop();
        configSpec = builder.build();

        AcceleratorTier.BASIC.setConfigReference(accelBasicPowerPerBlock, accelBasicParticleProduction);
        AcceleratorTier.ADVANCED.setConfigReference(accelAdvPowerPerBlock, accelAdvParticleProduction);
        AcceleratorTier.ELITE.setConfigReference(accelElitePowerPerBlock, accelEliteParticleProduction);
        AcceleratorTier.ULTIMATE.setConfigReference(accelUltimatePowerPerBlock, accelUltimateParticleProduction);
    }

    @Override
    public String getFileName() {
        return "research";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public ModConfig.Type getConfigType() {
        return ModConfig.Type.SERVER;
    }
}
