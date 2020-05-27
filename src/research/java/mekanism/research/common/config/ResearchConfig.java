package mekanism.research.common.config;

import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedFloatingLongValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class ResearchConfig extends BaseMekanismConfig {
    private static final String ACCELERATOR_CATEGORY = "accelerator";

    private final ForgeConfigSpec configSpec;

    public final CachedFloatingLongValue accelBasicPowerPerBlock;
    public final CachedFloatingLongValue accelBasicParticleProduction;
    public final CachedFloatingLongValue accelAdvPowerPerBlock;
    public final CachedFloatingLongValue accelAdvParticleProduction;
    public final CachedFloatingLongValue accelElitePowerPerBlock;
    public final CachedFloatingLongValue accelEliteParticleProduction;
    public final CachedFloatingLongValue accelUltimatePowerPerBlock;
    public final CachedFloatingLongValue accelUltimateParticleProduction;

    ResearchConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Mekanism Research Config. This config is synced between server and client.").push("research");

        // TODO: Values require testing!
        // Power consumption calculated to increase by 60% per tier
        // Particle production calculated to increase by 40% per tier
        builder.comment("Particle Accelerator Settings").push(ACCELERATOR_CATEGORY);
        accelBasicPowerPerBlock = CachedFloatingLongValue.define(this, builder, "Amount of power consumed per block",
                "accelBasicPowerPerBlock", FloatingLong.createConst(50));
        accelBasicParticleProduction = CachedFloatingLongValue.define(this, builder, "Particle Production (controls both material production and research production rate) per block",
                "accelBasicParticleProduction", FloatingLong.createConst(10));
        accelAdvPowerPerBlock = CachedFloatingLongValue.define(this, builder, "Amount of power consumed per block",
                "accelAdvPowerPerBlock", FloatingLong.createConst(80));
        accelAdvParticleProduction = CachedFloatingLongValue.define(this, builder, "Particle Production (controls both material production and research production rate) per block",
                "accelAdvParticleProduction", FloatingLong.createConst(14));
        accelElitePowerPerBlock = CachedFloatingLongValue.define(this, builder, "Amount of power consumed per block",
                "accelElitePowerPerBlock", FloatingLong.createConst(128));
        accelEliteParticleProduction = CachedFloatingLongValue.define(this, builder, "Particle Production (controls both material production and research production rate) per block",
                "accelEliteParticleProduction", FloatingLong.createConst(19.6));
        accelUltimatePowerPerBlock = CachedFloatingLongValue.define(this, builder, "Amount of power consumed per block",
                "accelUltimatePowerPerBlock", FloatingLong.createConst(204.8));
        accelUltimateParticleProduction = CachedFloatingLongValue.define(this, builder, "Particle Production (controls both material production and research production rate) per block",
                "accelUltimateParticleProduction", FloatingLong.createConst(27.44));
        builder.pop();

        builder.pop();
        configSpec = builder.build();

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
