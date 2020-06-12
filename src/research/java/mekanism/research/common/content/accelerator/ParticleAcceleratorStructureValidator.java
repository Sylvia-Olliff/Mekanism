package mekanism.research.common.content.accelerator;

import mekanism.common.Mekanism;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.lib.math.voxel.BlockPosBuilder;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.math.voxel.VoxelCuboid.CuboidSide;
import mekanism.common.lib.math.voxel.VoxelPlane;
import mekanism.common.lib.multiblock.*;
import mekanism.common.lib.multiblock.FormationProtocol.CasingType;
import mekanism.common.lib.multiblock.FormationProtocol.FormationResult;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.registries.ResearchBlockTypes;
import mekanism.research.common.tier.AcceleratorTier;
import mekanism.research.common.tile.accelerator.TileEntityParticleAcceleratorCasing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class ParticleAcceleratorStructureValidator extends CuboidStructureValidator<ParticleAcceleratorMultiblockData> {

    private static final VoxelCuboid MIN_BOUNDS = new VoxelCuboid(2, 1, 2);
    private static final VoxelCuboid MAX_BOUNDS = new VoxelCuboid(18, 1, 18);

    public ParticleAcceleratorStructureValidator() { super(MIN_BOUNDS, MAX_BOUNDS); }

    @Override
    protected CasingType getCasingType(BlockPos pos, BlockState state) {
        Block block = state.getBlock();

        //TODO: Add the Port/Valve block as valid casing
        if (BlockTypeTile.is(block, ResearchBlockTypes.PARTICLE_ACCELERATOR_CASING)) {
            return CasingType.FRAME;
        } else {
            return CasingType.INVALID;
        }
    }

    @Override
    public FormationResult validate(FormationProtocol<ParticleAcceleratorMultiblockData> ctx) {
        BlockPos min = cuboid.getMinPos(), max = cuboid.getMaxPos();
        int y = min.getY();
        int minX = min.getX();
        int minZ = min.getZ();
        int maxX = max.getX();
        int maxZ = max.getZ();
        AcceleratorTier firstTier = null;

        for (int x = min.getX(); x < max.getX(); x++) {
            for (int z = min.getZ(); z < max.getZ(); z++) {
                BlockPos pos = new BlockPos(x, y, z);
                IMultiblockBase tile = structure.getTile(pos);
                if (x > minX && z > minZ && x < maxX && z < maxZ) {
                    // Don't care what kind of multiblock tile this is, if it's inside the ring, don't form.
                    if (tile instanceof IMultiblock) {
                        return FormationResult.FAIL;
                    }
                }
                else if (MekanismResearch.particleAcceleratorManager.isCompatible((TileEntity) tile)) {
                    if (tile instanceof TileEntityParticleAcceleratorCasing) { // TODO: Update to include Port TE
                        AcceleratorTier currentTier = ((TileEntityParticleAcceleratorCasing) tile).getTier();

                        if (firstTier == null)
                            firstTier = currentTier;
                        else
                        {
                            if (currentTier != firstTier)
                                return FormationResult.FAIL; // TODO: Add Text message for multiblock failing if all components are not the same tier
                        }

                        IMultiblock<ParticleAcceleratorMultiblockData> multiblockTile = (IMultiblock<ParticleAcceleratorMultiblockData>) tile;
                        UUID uuid = multiblockTile.getCacheID();
                        if (uuid != null && multiblockTile.getManager() == manager && multiblockTile.hasCache()) {
                            manager.updateCache(multiblockTile);
                            ctx.idsFound.add(uuid);
                        }
                        ctx.locations.add(pos);
                        CasingType type = getCasingType(pos, world.getBlockState(pos));
                        if (type == FormationProtocol.CasingType.VALVE) {
                            IValveHandler.ValveData data = new IValveHandler.ValveData();
                            data.location = pos;
                            data.side = getSide(data.location);
                            ctx.valves.add(data);
                        }
                    }
                    else
                        return FormationResult.FAIL; // If the tile entity found isn't a particle accelerator component, fail
                }
                else {
                    return FormationResult.FAIL;
                }
            }
        }
        return FormationProtocol.FormationResult.SUCCESS;
    }

    @Override
    public boolean precheck() {

        VoxelCuboid ring = null;
        // Only check the Y axis as this should be a ring.
        TreeMap<Integer, VoxelPlane> map = structure.getMajorAxisMap(Structure.Axis.Y);

        if (map.isEmpty())
            return false;

        VoxelPlane yPlane = map.firstEntry().getValue();
        int y = map.firstEntry().getKey();

        // All valid formats are a multiple of 4
        if ((yPlane.size() % 4) != 0)
            return false;

        // If this plane is somehow full it's either too small or invalid
        if (yPlane.isFull())
            return false;

        // If it's not a square don't bother
        if (yPlane.height() != yPlane.length())
            return false;

        // Check bounds limits
        ring = VoxelCuboid.from(yPlane, yPlane, yPlane.getMinRow(), yPlane.getMaxRow() + yPlane.getMaxCol());
        if (!ring.greaterOrEqual(MIN_BOUNDS) || MAX_BOUNDS.greaterOrEqual(ring))
            return false;

        cuboid = ring;

        return true;
    }

    @Override
    public FormationResult postcheck(ParticleAcceleratorMultiblockData structureIn, Set<BlockPos> innerNodes) {
        Mekanism.logger.info("Successfully formed particle accelerator!");
        return super.postcheck(structureIn, innerNodes);
    }
}
