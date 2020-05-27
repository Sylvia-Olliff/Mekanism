package mekanism.research.common.content.accelerator;

import mekanism.common.Mekanism;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.lib.math.voxel.BlockPosBuilder;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.math.voxel.VoxelCuboid.CuboidSide;
import mekanism.common.lib.math.voxel.VoxelPlane;
import mekanism.common.lib.multiblock.*;
import mekanism.research.common.MekanismResearch;
import mekanism.research.common.registries.ResearchBlockTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ParticleAcceleratorStructureValidator extends CuboidStructureValidator<ParticleAcceleratorMultiblockData> {

    private static final VoxelCuboid MIN_BOUNDS = new VoxelCuboid(2, 1, 2);
    private static final VoxelCuboid MAX_BOUNDS = new VoxelCuboid(18, 1, 18);

    public ParticleAcceleratorStructureValidator() { super(MIN_BOUNDS, MAX_BOUNDS); }

    @Override
    protected FormationProtocol.CasingType getCasingType(BlockPos pos, BlockState state) {
        Block block = state.getBlock();

        //TODO: Add the Port/Valve block as valid casing
        if (BlockTypeTile.is(block, ResearchBlockTypes.PARTICLE_ACCELERATOR_CASING)) {
            return FormationProtocol.CasingType.FRAME;
        } else {
            return FormationProtocol.CasingType.INVALID;
        }
    }

    @Override
    protected boolean validateInner(BlockPos pos) {
        // We don't care what's inside the ring, so long as it isn't another accelerator block.
        return !BlockType.is(world.getBlockState(pos).getBlock(), ResearchBlockTypes.PARTICLE_ACCELERATOR_CASING);
    }

    @Override
    public FormationProtocol.FormationResult validate(FormationProtocol<ParticleAcceleratorMultiblockData> ctx) {
        return super.validate(ctx);
    }

    @Override
    protected FormationProtocol.FormationResult validateFrame(FormationProtocol<ParticleAcceleratorMultiblockData> ctx, BlockPos pos, BlockState state, FormationProtocol.CasingType type, boolean needsFrame) {
        return super.validateFrame(ctx, pos, state, type, needsFrame);
    }

    @Override
    protected FormationProtocol.FormationResult validateNode(FormationProtocol<ParticleAcceleratorMultiblockData> ctx, BlockPos pos) {
        return super.validateNode(ctx, pos);
    }

    @Override
    protected FormationProtocol.StructureRequirement getStructureRequirement(BlockPos pos) {
        
        return FormationProtocol.StructureRequirement.IGNORED;
    }

    @Override
    public boolean precheck() {
        // Only check the X and Z Axis as this structure has no top or bottom.
        VoxelCuboid ring = null;
        TreeMap<Integer, VoxelPlane> xMap = structure.getAxisMap(Structure.Axis.X);
        TreeMap<Integer, VoxelPlane> zMap = structure.getAxisMap(Structure.Axis.Z);

        Map.Entry<Integer, VoxelPlane> firstX = xMap.firstEntry(), lastX = xMap.lastEntry();

        if (firstX == null || !firstX.getValue().equals(lastX.getValue()) || !firstX.getValue().isFull()) {
            return false;
        }

        VoxelCuboid pass1 = VoxelCuboid.from(firstX.getValue(), lastX.getValue(), firstX.getKey(), lastX.getKey());

        if (!pass1.greaterOrEqual(MIN_BOUNDS) && MAX_BOUNDS.greaterOrEqual(pass1))
            return false;

        Map.Entry<Integer, VoxelPlane> firstZ = zMap.firstEntry(), lastZ = zMap.lastEntry();

        if (firstZ == null || !firstZ.getValue().equals(lastZ.getValue()) || !firstZ.getValue().isFull()) {
            return false;
        }

        ring = VoxelCuboid.from(firstZ.getValue(), lastZ.getValue(), firstZ.getKey(), lastZ.getKey());

        if (!pass1.equals(ring))
            return false;

        cuboid = ring;

        return true;
    }

    @Override
    public FormationProtocol.FormationResult postcheck(ParticleAcceleratorMultiblockData structureIn, Set<BlockPos> innerNodes) {
        TreeMap<Integer, VoxelPlane> xMap = structure.getAxisMap(Structure.Axis.X);
        TreeMap<Integer, VoxelPlane> zMap = structure.getAxisMap(Structure.Axis.Z);

        for (Map.Entry<Integer, VoxelPlane>
                entry : xMap.entrySet()) {
            Mekanism.logger.info("Key: " + entry.getKey() + " Value: " + entry.getValue().toString());
        }

        for (Map.Entry<Integer, VoxelPlane>
                entry : zMap.entrySet()) {
            Mekanism.logger.info("Key: " + entry.getKey() + " Value: " + entry.getValue().toString());
        }
        return super.postcheck(structureIn, innerNodes);
    }
}
