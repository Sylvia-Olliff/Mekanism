package mekanism.generators.common.block.turbine;

import javax.annotation.Nonnull;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.base.WrenchResult;
import mekanism.common.util.MekanismUtils;
import mekanism.generators.common.item.ItemTurbineBlade;
import mekanism.generators.common.registries.GeneratorsBlockTypes;
import mekanism.generators.common.registries.GeneratorsItems;
import mekanism.generators.common.tile.turbine.TileEntityTurbineRotor;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockTurbineRotor extends BlockTileModel<TileEntityTurbineRotor, BlockTypeTile<TileEntityTurbineRotor>> {

    private static final VoxelShape bounds = makeCuboidShape(6, 0, 6, 10, 16, 10);

    public BlockTurbineRotor() {
        super(GeneratorsBlockTypes.TURBINE_ROTOR);
    }

    @Override
    @Deprecated
    public void onReplaced(BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (!world.isRemote && state.hasTileEntity() && state.getBlock() != newState.getBlock()) {
            TileEntityTurbineRotor tile = MekanismUtils.getTileEntity(TileEntityTurbineRotor.class, world, pos);
            if (tile != null) {
                int amount = tile.getHousedBlades();
                if (amount > 0) {
                    spawnAsEntity(world, pos, GeneratorsItems.TURBINE_BLADE.getItemStack(amount));
                }
            }
        }
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Nonnull
    @Override
    @Deprecated
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand,
          @Nonnull BlockRayTraceResult hit) {
        if (world.isRemote) {
            return ActionResultType.SUCCESS;
        }
        TileEntityMekanism tile = MekanismUtils.getTileEntity(TileEntityMekanism.class, world, pos);
        if (tile == null) {
            return ActionResultType.PASS;
        }
        if (tile.tryWrench(state, player, hand, hit) != WrenchResult.PASS) {
            return ActionResultType.SUCCESS;
        }
        ItemStack stack = player.getHeldItem(hand);
        TileEntityTurbineRotor rod = (TileEntityTurbineRotor) tile;
        if (!player.isSneaking()) {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemTurbineBlade) {
                if (rod.addBlade()) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                        if (stack.getCount() == 0) {
                            player.setHeldItem(hand, ItemStack.EMPTY);
                        }
                    }
                }
            }
        } else if (stack.isEmpty()) {
            if (rod.removeBlade()) {
                if (!player.isCreative()) {
                    player.setHeldItem(hand, GeneratorsItems.TURBINE_BLADE.getItemStack());
                    player.inventory.markDirty();
                }
            }
        } else if (stack.getItem() instanceof ItemTurbineBlade) {
            if (stack.getCount() < stack.getMaxStackSize()) {
                if (rod.removeBlade()) {
                    if (!player.isCreative()) {
                        stack.grow(1);
                        player.inventory.markDirty();
                    }
                }
            }
        }
        return ActionResultType.PASS;
    }

    @Nonnull
    @Override
    @Deprecated
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return bounds;
    }
}