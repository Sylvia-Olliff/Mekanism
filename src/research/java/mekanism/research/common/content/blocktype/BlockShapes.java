package mekanism.research.common.content.blocktype;

import static mekanism.common.util.VoxelShapeUtils.setShape;
import static net.minecraft.block.Block.makeCuboidShape;
import mekanism.common.util.EnumUtils;
import mekanism.common.util.VoxelShapeUtils;
import net.minecraft.util.math.shapes.VoxelShape;

public final class BlockShapes {

    public static final VoxelShape[] RESEARCH_TERMINAL = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];

    static {
        setShape(VoxelShapeUtils.combine(
                makeCuboidShape(0, 0, 0, 16, 4, 16), // base
                makeCuboidShape(0, 10, 0, 16, 12, 16), // display
                makeCuboidShape(1, 4, 2, 5, 9, 4), // strut1
                makeCuboidShape(11, 4, 2, 15, 9, 4), // strut2
                makeCuboidShape(11, 4, 12, 15, 13, 15), // strut3
                makeCuboidShape(1, 4, 12, 5, 13, 15) // strut4
        ), RESEARCH_TERMINAL);
    }
}
