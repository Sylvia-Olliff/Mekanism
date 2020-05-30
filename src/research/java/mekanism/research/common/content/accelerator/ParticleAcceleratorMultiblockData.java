package mekanism.research.common.content.accelerator;

import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.inventory.container.sync.dynamic.ContainerSync;
import mekanism.common.lib.math.voxel.IShape;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.research.common.tile.accelerator.TileEntityParticleAcceleratorCasing;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public class ParticleAcceleratorMultiblockData extends MultiblockData {
    private static final FloatingLong MAX_ENERGY = FloatingLong.createConst(500_000_000);
    // TODO: Change Model for PAccelerator to use Attribute BlockShape rather than values in model.json
    @ContainerSync
    public BasicEnergyContainer energyContainer;

    public ParticleAcceleratorMultiblockData(TileEntityParticleAcceleratorCasing tile) {
        super(tile);

        energyContainers.add(energyContainer = BasicEnergyContainer.output(MAX_ENERGY, this));
    }

    @Override
    public boolean tick(World world) {
        return super.tick(world);
    }
}
