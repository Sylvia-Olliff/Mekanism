package mekanism.common.block.prefab;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ILightReader;
import net.minecraftforge.common.ToolType;

public class BlockTileGlass<TILE extends TileEntityMekanism, TYPE extends BlockTypeTile<TILE>> extends BlockTile<TILE, TYPE> {

    public BlockTileGlass(TYPE type) {
        super(type, Block.Properties.create(Material.GLASS).hardnessAndResistance(3.5F, 16F).notSolid().harvestTool(ToolType.PICKAXE));
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState state, ILightReader world, BlockPos pos, IFluidState fluidState) {
        return true;
    }

    @Override
    @Deprecated
    public boolean isSideInvisible(@Nonnull BlockState state, @Nonnull BlockState adjacentBlockState, @Nonnull Direction side) {
        return adjacentBlockState.getBlock() instanceof BlockTileGlass;
    }

    @Override
    @Deprecated
    public float getAmbientOcclusionLightValue(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos) {
        return true;
    }

    @Override
    @Deprecated
    public boolean causesSuffocation(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isNormalCube(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos) {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, PlacementType type, @Nullable EntityType<?> entityType) {
        return false;
    }
}