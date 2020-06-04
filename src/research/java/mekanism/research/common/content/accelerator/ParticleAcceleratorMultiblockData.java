package mekanism.research.common.content.accelerator;

import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.inventory.container.sync.dynamic.ContainerSync;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.network.PacketResearchUpdate;
import mekanism.research.common.tier.AcceleratorTier;
import mekanism.research.common.tile.accelerator.TileEntityParticleAcceleratorCasing;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLServerLaunchProvider;
import net.minecraftforge.fml.loading.FMLServiceProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerMain;

import java.util.UUID;

public class ParticleAcceleratorMultiblockData extends MultiblockData {
    private static final FloatingLong MAX_ENERGY = FloatingLong.createConst(500_000_000);
    private static final int TICK_FREQUENCY = 50;
    private long ticks;
    private AcceleratorTier tier;
    private UUID ownerId;

    @ContainerSync
    public BasicEnergyContainer energyContainer;

    public ParticleAcceleratorMultiblockData(TileEntityParticleAcceleratorCasing tile) {
        super(tile);

        energyContainers.add(energyContainer = BasicEnergyContainer.output(MAX_ENERGY, this));
        ticks = 0L;
        ownerId = tile.getSecurity().getOwnerUUID();
    }

    @Override
    public boolean tick(World world) {
        ++ticks;

        if (ticks % TICK_FREQUENCY == 0) {
            PlayerEntity player = 
            MekanismResearch.packetHandler.sendToAllTracking(new PacketResearchUpdate(ownerId, tier.getResearchGeneration()), );
        }

        return super.tick(world);
    }
}
