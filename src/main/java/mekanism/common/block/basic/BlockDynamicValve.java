package mekanism.common.block.basic;

import javax.annotation.Nonnull;
import mekanism.common.base.IComparatorSupport;
import mekanism.common.block.BlockBasic;
import mekanism.common.tile.TileEntityDynamicTank;
import mekanism.common.tile.TileEntityDynamicValve;
import mekanism.common.util.MekanismUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class BlockDynamicValve extends BlockBasic {

    public BlockDynamicValve() {
        super("dynamic_valve");
    }

    @Override
    public boolean hasDescription() {
        return true;
    }

    @Override
    public boolean canCreatureSpawn(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, SpawnPlacementType type) {
        TileEntityDynamicTank tileEntity = (TileEntityDynamicTank) MekanismUtils.getTileEntitySafe(world, pos);
        if (tileEntity != null) {
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
                if (tileEntity.structure != null) {
                    return false;
                }
            } else if (tileEntity.clientHasStructure) {
                return false;
            }
        }
        return super.canCreatureSpawn(state, world, pos, type);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityDynamicValve();
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState blockState) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IComparatorSupport) {
            return ((IComparatorSupport) tile).getRedstoneLevel();
        }
        return 0;
    }
}