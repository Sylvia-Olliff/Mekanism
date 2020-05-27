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

    // NOTE: This is nearly identical to the Super, but allows for height of only 1
    @Override
    public boolean setShape(IShape shape) {
        if (shape instanceof VoxelCuboid) {
            VoxelCuboid cuboid = (VoxelCuboid) shape;
            minLocation = cuboid.getMinPos();
            maxLocation = cuboid.getMaxPos();
            renderLocation = minLocation.offset(Direction.UP);
            length = Math.abs(maxLocation.getX() - minLocation.getX()) + 1;
            height = Math.abs(maxLocation.getY() - minLocation.getY()) + 1;
            width = Math.abs(maxLocation.getZ() - minLocation.getZ()) + 1;
            setVolume(length * width * height);
            return length >= 3 && length <= FormationProtocol.MAX_SIZE && height >= 1 && height <= FormationProtocol.MAX_SIZE && width >= 3 && width <= FormationProtocol.MAX_SIZE;
        }
        return false;
    }

    @Override
    public boolean tick(World world) {
        return super.tick(world);
    }
}
