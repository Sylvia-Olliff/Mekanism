package mekanism.research.common.content.accelerator;

import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.math.voxel.VoxelCuboid.CuboidSide;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.lib.multiblock.Structure;
import mekanism.common.lib.multiblock.StructureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class ParticleAcceleratorStructureValidator extends CuboidStructureValidator {

    private static final VoxelCuboid MIN_BOUNDS = new VoxelCuboid(2, 1, 2);
    private static final VoxelCuboid MAX_BOUNDS = new VoxelCuboid(18, 1, 18);


    @Override
    protected FormationProtocol.CasingType getCasingType(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public boolean precheck() {
        cuboid = StructureHelper.fetchCuboid(structure, MIN_BOUNDS, MAX_BOUNDS, EnumSet.of(CuboidSide.BOTTOM, CuboidSide.TOP), 2);
        return cuboid != null;
    }

    private enum CasingType {
        IGNORED,
        CASING,
        OTHER;

        private static final CasingType[] CASINGS = values();

        boolean needsCasing() { return this == CASING || this == IGNORED; }
    }
}
