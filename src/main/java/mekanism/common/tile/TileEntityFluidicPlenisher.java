package mekanism.common.tile;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.api.Coord4D;
import mekanism.api.IConfigurable;
import mekanism.api.TileNetworkList;
import mekanism.api.sustained.ISustainedTank;
import mekanism.api.text.EnumColor;
import mekanism.common.Mekanism;
import mekanism.common.MekanismBlock;
import mekanism.common.Upgrade;
import mekanism.common.base.FluidHandlerWrapper;
import mekanism.common.base.IComparatorSupport;
import mekanism.common.base.IFluidHandlerWrapper;
import mekanism.common.base.IUpgradeTile;
import mekanism.common.base.LazyOptionalHelper;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.computer.IComputerIntegration;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.util.ChargeUtils;
import mekanism.common.util.FluidContainerUtils;
import mekanism.common.util.FluidContainerUtils.FluidChecker;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.PipeUtils;
import mekanism.common.util.TileUtils;
import mekanism.common.util.text.TextComponentUtil;
import mekanism.common.util.text.Translation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileEntityFluidicPlenisher extends TileEntityMekanism implements IComputerIntegration, IConfigurable, IFluidHandlerWrapper, ISustainedTank,
      IUpgradeTile, IComparatorSupport {

    private static final String[] methods = new String[]{"reset"};
    private static EnumSet<Direction> dirs = EnumSet.complementOf(EnumSet.of(Direction.UP));
    public Set<Coord4D> activeNodes = new LinkedHashSet<>();
    public Set<Coord4D> usedNodes = new HashSet<>();
    public boolean finishedCalc = false;
    public FluidTank fluidTank = new FluidTank(10000);
    /**
     * How many ticks it takes to run an operation.
     */
    public int BASE_TICKS_REQUIRED = 20;
    public int ticksRequired = BASE_TICKS_REQUIRED;
    /**
     * How many ticks this machine has been operating for.
     */
    public int operatingTicks;
    public TileComponentUpgrade<TileEntityFluidicPlenisher> upgradeComponent = new TileComponentUpgrade<>(this, 3);

    private int currentRedstoneLevel;

    public TileEntityFluidicPlenisher() {
        super(MekanismBlock.FLUIDIC_PLENISHER);
    }

    @Override
    public void onUpdate() {
        if (!world.isRemote) {
            ChargeUtils.discharge(2, this);
            if (FluidContainerUtils.isFluidContainer(getInventory().get(0))) {
                FluidContainerUtils.handleContainerItemEmpty(this, fluidTank, 0, 1, new FluidChecker() {
                    @Override
                    public boolean isValid(@Nonnull Fluid f) {
                        //TODO: Is there a better position to use
                        return f.getAttributes().canBePlacedInWorld(world, BlockPos.ZERO, f.getDefaultState());
                    }
                });
            }

            if (MekanismUtils.canFunction(this) && getEnergy() >= getEnergyPerTick() && !fluidTank.getFluid().isEmpty() &&
                fluidTank.getFluid().getFluid().getAttributes().canBePlacedInWorld(world, BlockPos.ZERO, fluidTank.getFluid())) {
                //TODO: Is there a better position to use
                if (!finishedCalc) {
                    setEnergy(getEnergy() - getEnergyPerTick());
                }
                if ((operatingTicks + 1) < ticksRequired) {
                    operatingTicks++;
                } else {
                    if (!finishedCalc) {
                        doPlenish();
                    } else {
                        Coord4D below = Coord4D.get(this).offset(Direction.DOWN);

                        if (canReplace(below, false, false) && fluidTank.getFluidAmount() >= FluidAttributes.BUCKET_VOLUME) {
                            if (fluidTank.getFluid().getFluid().getAttributes().canBePlacedInWorld(world, below.getPos(), fluidTank.getFluid())) {
                                world.setBlockState(below.getPos(), MekanismUtils.getFlowingBlockState(fluidTank.getFluid()));
                                setEnergy(getEnergy() - getEnergyPerTick());
                                fluidTank.drain(FluidAttributes.BUCKET_VOLUME, FluidAction.EXECUTE);
                            }
                        }
                    }
                    operatingTicks = 0;
                }
            }

            int newRedstoneLevel = getRedstoneLevel();
            if (newRedstoneLevel != currentRedstoneLevel) {
                world.updateComparatorOutputLevel(pos, getBlockType());
                currentRedstoneLevel = newRedstoneLevel;
            }
        }
    }

    private void doPlenish() {
        if (usedNodes.size() >= MekanismConfig.general.maxPlenisherNodes.get()) {
            finishedCalc = true;
            return;
        }
        if (activeNodes.isEmpty()) {
            if (usedNodes.isEmpty()) {
                Coord4D below = Coord4D.get(this).offset(Direction.DOWN);
                if (!canReplace(below, true, true)) {
                    finishedCalc = true;
                    return;
                }
                activeNodes.add(below);
            } else {
                finishedCalc = true;
                return;
            }
        }

        Set<Coord4D> toRemove = new HashSet<>();
        for (Coord4D coord : activeNodes) {
            if (coord.exists(world)) {
                FluidStack fluid = fluidTank.getFluid();
                if (canReplace(coord, true, false) && !fluid.isEmpty()) {
                    world.setBlockState(coord.getPos(), MekanismUtils.getFlowingBlockState(fluid));
                    fluidTank.drain(FluidAttributes.BUCKET_VOLUME, FluidAction.EXECUTE);
                }

                for (Direction dir : dirs) {
                    Coord4D sideCoord = coord.offset(dir);
                    if (sideCoord.exists(world) && canReplace(sideCoord, true, true)) {
                        activeNodes.add(sideCoord);
                    }
                }
                toRemove.add(coord);
                break;
            } else {
                toRemove.add(coord);
            }
        }
        usedNodes.addAll(toRemove);
        activeNodes.removeAll(toRemove);
    }

    public boolean canReplace(Coord4D coord, boolean checkNodes, boolean isPathfinding) {
        if (checkNodes && usedNodes.contains(coord)) {
            return false;
        }
        if (coord.isAirBlock(world) || MekanismUtils.isDeadFluid(world, coord)) {
            return true;
        }
        if (MekanismUtils.isFluid(world, coord)) {
            return isPathfinding;
        }
        return MekanismUtils.isValidReplaceableBlock(world, coord.getPos());
    }

    @Override
    public void handlePacketData(PacketBuffer dataStream) {
        super.handlePacketData(dataStream);
        if (world.isRemote) {
            finishedCalc = dataStream.readBoolean();
            TileUtils.readTankData(dataStream, fluidTank);
        }
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        data.add(finishedCalc);
        TileUtils.addTankData(data, fluidTank);
        return data;
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT nbtTags) {
        super.write(nbtTags);
        nbtTags.putInt("operatingTicks", operatingTicks);
        nbtTags.putBoolean("finishedCalc", finishedCalc);

        if (!fluidTank.getFluid().isEmpty()) {
            nbtTags.put("fluidTank", fluidTank.writeToNBT(new CompoundNBT()));
        }

        ListNBT activeList = new ListNBT();
        for (Coord4D wrapper : activeNodes) {
            CompoundNBT tagCompound = new CompoundNBT();
            wrapper.write(tagCompound);
            activeList.add(tagCompound);
        }
        if (!activeList.isEmpty()) {
            nbtTags.put("activeNodes", activeList);
        }

        ListNBT usedList = new ListNBT();
        for (Coord4D obj : usedNodes) {
            activeList.add(obj.write(new CompoundNBT()));
        }
        if (!activeList.isEmpty()) {
            nbtTags.put("usedNodes", usedList);
        }
        return nbtTags;
    }

    @Override
    public void read(CompoundNBT nbtTags) {
        super.read(nbtTags);
        operatingTicks = nbtTags.getInt("operatingTicks");
        finishedCalc = nbtTags.getBoolean("finishedCalc");

        if (nbtTags.contains("fluidTank")) {
            fluidTank.readFromNBT(nbtTags.getCompound("fluidTank"));
        }

        if (nbtTags.contains("activeNodes")) {
            ListNBT tagList = nbtTags.getList("activeNodes", NBT.TAG_COMPOUND);

            for (int i = 0; i < tagList.size(); i++) {
                activeNodes.add(Coord4D.read(tagList.getCompound(i)));
            }
        }
        if (nbtTags.contains("usedNodes")) {
            ListNBT tagList = nbtTags.getList("usedNodes", NBT.TAG_COMPOUND);

            for (int i = 0; i < tagList.size(); i++) {
                usedNodes.add(Coord4D.read(tagList.getCompound(i)));
            }
        }
    }

    @Override
    public boolean isItemValidForSlot(int slotID, @Nonnull ItemStack itemstack) {
        if (slotID == 1) {
            return false;
        } else if (slotID == 0) {
            //TODO: Is there a better position to use
            return new LazyOptionalHelper<>(FluidUtil.getFluidContained(itemstack)).matches(fluidStack ->
                  fluidStack.getFluid().getAttributes().canBePlacedInWorld(world, BlockPos.ZERO, fluidStack));
        } else if (slotID == 2) {
            return ChargeUtils.canBeDischarged(itemstack);
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull Direction side) {
        if (slotID == 2) {
            return ChargeUtils.canBeOutputted(itemstack, false);
        }
        return slotID == 1;
    }

    @Override
    public boolean canReceiveEnergy(Direction side) {
        return getOppositeDirection() == side;
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull Direction side) {
        if (side == Direction.UP) {
            return new int[]{0};
        } else if (side == Direction.DOWN) {
            return new int[]{1};
        }
        return new int[]{2};
    }

    @Override
    public IFluidTank[] getTankInfo(Direction direction) {
        if (direction == Direction.UP) {
            return new IFluidTank[]{fluidTank};
        }
        return PipeUtils.EMPTY;
    }

    @Override
    public IFluidTank[] getAllTanks() {
        return getTankInfo(Direction.UP);
    }

    @Override
    public void setFluidStack(@Nonnull FluidStack fluidStack, Object... data) {
        fluidTank.setFluid(fluidStack);
    }

    @Nonnull
    @Override
    public FluidStack getFluidStack(Object... data) {
        return fluidTank.getFluid();
    }

    @Override
    public boolean hasTank(Object... data) {
        return true;
    }

    @Override
    public int fill(Direction from, @Nonnull FluidStack resource, FluidAction fluidAction) {
        return fluidTank.fill(resource, fluidAction);
    }

    @Override
    public boolean canFill(Direction from, @Nonnull FluidStack fluid) {
        //TODO: Is there a better position to use
        return from == Direction.UP && fluid.getFluid().getAttributes().canBePlacedInWorld(world, BlockPos.ZERO, fluid);
    }

    @Override
    public ActionResultType onSneakRightClick(PlayerEntity player, Direction side) {
        activeNodes.clear();
        usedNodes.clear();
        finishedCalc = false;
        player.sendMessage(TextComponentUtil.build(EnumColor.DARK_BLUE, Mekanism.LOG_TAG + " ", EnumColor.GRAY,
              Translation.of("tooltip.mekanism.configurator.plenisherReset")));
        return ActionResultType.SUCCESS;
    }

    @Override
    public ActionResultType onRightClick(PlayerEntity player, Direction side) {
        return ActionResultType.PASS;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (capability == Capabilities.CONFIGURABLE_CAPABILITY) {
            return Capabilities.CONFIGURABLE_CAPABILITY.orEmpty(capability, LazyOptional.of(() -> this));
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.orEmpty(capability, LazyOptional.of(() -> new FluidHandlerWrapper(this, side)));
        }
        return super.getCapability(capability, side);
    }

    @Override
    public String[] getMethods() {
        return methods;
    }

    @Override
    public Object[] invoke(int method, Object[] arguments) throws NoSuchMethodException {
        if (method == 0) {
            activeNodes.clear();
            usedNodes.clear();
            finishedCalc = false;
            return new Object[]{"Plenisher calculation reset."};
        }
        throw new NoSuchMethodException();
    }

    @Override
    public TileComponentUpgrade getComponent() {
        return upgradeComponent;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        switch (upgrade) {
            case SPEED:
                ticksRequired = MekanismUtils.getTicks(this, BASE_TICKS_REQUIRED);
            case ENERGY:
                setEnergyPerTick(MekanismUtils.getEnergyPerTick(this, getBaseUsage()));
                setMaxEnergy(MekanismUtils.getMaxEnergy(this, getBaseStorage()));
                setEnergy(Math.min(getMaxEnergy(), getEnergy()));
            default:
                break;
        }
    }

    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(fluidTank.getFluidAmount(), fluidTank.getCapacity());
    }
}