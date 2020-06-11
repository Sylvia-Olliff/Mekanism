package mekanism.common.tile.transmitter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.api.Action;
import mekanism.api.NBTConstants;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.fluid.IMekanismFluidHandler;
import mekanism.api.inventory.AutomationType;
import mekanism.api.math.MathUtils;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.tier.AlloyTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.states.BlockStateHelper;
import mekanism.common.block.states.TransmitterType;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.proxy.ProxyFluidHandler;
import mekanism.common.capabilities.resolver.advanced.AdvancedCapabilityResolver;
import mekanism.common.content.transmitter.FluidNetwork;
import mekanism.common.lib.transmitter.ConnectionType;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tier.PipeTier;
import mekanism.common.upgrade.transmitter.MechanicalPipeUpgradeData;
import mekanism.common.upgrade.transmitter.TransmitterUpgradeData;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityMechanicalPipe extends TileEntityBufferedTransmitter<IFluidHandler, FluidNetwork, FluidStack, TileEntityMechanicalPipe> implements
      IMekanismFluidHandler {

    public final PipeTier tier;

    @Nonnull
    public FluidStack saveShare = FluidStack.EMPTY;
    private final List<IExtendedFluidTank> tanks;
    public final BasicFluidTank buffer;

    public TileEntityMechanicalPipe(IBlockProvider blockProvider) {
        super(blockProvider);
        this.tier = Attribute.getTier(blockProvider.getBlock(), PipeTier.class);
        //TODO: If we make fluids support longs then adjust this
        buffer = BasicFluidTank.create(MathUtils.clampToInt(getCapacity()), BasicFluidTank.alwaysFalse, BasicFluidTank.alwaysTrue, this);
        tanks = Collections.singletonList(buffer);
        addCapabilityResolver(AdvancedCapabilityResolver.readOnly(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, this, () -> new ProxyFluidHandler(this, null, null)));
    }

    @Override
    public void tick() {
        if (!isRemote()) {
            Set<Direction> connections = getConnections(ConnectionType.PULL);
            if (!connections.isEmpty()) {
                for (IFluidHandler connectedAcceptor : acceptorCache.getConnectedAcceptors(connections)) {
                    FluidStack received;
                    //Note: We recheck the buffer each time in case we ended up accepting fluid somewhere
                    // and our buffer changed and is no longer empty
                    FluidStack bufferWithFallback = getBufferWithFallback();
                    if (bufferWithFallback.isEmpty()) {
                        //If we don't have a fluid stored try pulling as much as we are able to
                        received = connectedAcceptor.drain(getAvailablePull(), FluidAction.SIMULATE);
                    } else {
                        //Otherwise try draining the same type of fluid we have stored requesting up to as much as we are able to pull
                        // We do this to better support multiple tanks in case the fluid we have stored we could pull out of a block's
                        // second tank but just asking to drain a specific amount
                        received = connectedAcceptor.drain(new FluidStack(bufferWithFallback, getAvailablePull()), FluidAction.SIMULATE);
                    }
                    if (!received.isEmpty() && takeFluid(received, Action.SIMULATE).isEmpty()) {
                        FluidStack remainder = takeFluid(received, Action.EXECUTE);
                        connectedAcceptor.drain(new FluidStack(received, received.getAmount() - remainder.getAmount()), FluidAction.EXECUTE);
                    }
                }
            }
        }
        super.tick();
    }

    private int getAvailablePull() {
        if (hasTransmitterNetwork()) {
            return Math.min(tier.getPipePullAmount(), getTransmitterNetwork().fluidTank.getNeeded());
        }
        return Math.min(tier.getPipePullAmount(), buffer.getNeeded());
    }

    @Nonnull
    @Override
    public FluidStack insertFluid(int tank, @Nonnull FluidStack stack, @Nullable Direction side, @Nonnull Action action) {
        IExtendedFluidTank fluidTank = getFluidTank(tank, side);
        if (fluidTank == null) {
            return stack;
        } else if (side == null) {
            return fluidTank.insert(stack, action, AutomationType.INTERNAL);
        }
        //If we have a side only allow inserting if our connection allows it
        ConnectionType connectionType = getConnectionType(side);
        if (connectionType == ConnectionType.NORMAL || connectionType == ConnectionType.PULL) {
            return fluidTank.insert(stack, action, AutomationType.EXTERNAL);
        }
        return stack;
    }

    @Override
    public void read(@Nonnull CompoundNBT nbtTags) {
        super.read(nbtTags);
        if (nbtTags.contains(NBTConstants.FLUID_STORED, NBT.TAG_COMPOUND)) {
            saveShare = FluidStack.loadFluidStackFromNBT(nbtTags.getCompound(NBTConstants.FLUID_STORED));
        } else {
            saveShare = FluidStack.EMPTY;
        }
        buffer.setStack(saveShare);
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT nbtTags) {
        super.write(nbtTags);
        if (hasTransmitterNetwork()) {
            getTransmitterNetwork().validateSaveShares(this);
        }
        if (saveShare.isEmpty()) {
            nbtTags.remove(NBTConstants.FLUID_STORED);
        } else {
            nbtTags.put(NBTConstants.FLUID_STORED, saveShare.writeToNBT(new CompoundNBT()));
        }
        return nbtTags;
    }

    @Override
    public TransmitterType getTransmitterType() {
        return TransmitterType.MECHANICAL_PIPE;
    }

    @Override
    public boolean isValidAcceptor(TileEntity tile, Direction side) {
        return super.isValidAcceptor(tile, side) && acceptorCache.isAcceptorAndListen(tile, side, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
    }

    @Override
    public boolean isValidTransmitter(TileEntityTransmitter<?, ?, ?> tile) {
        if (super.isValidTransmitter(tile) && tile instanceof TileEntityMechanicalPipe) {
            FluidStack buffer = getBufferWithFallback();
            if (buffer.isEmpty() && hasTransmitterNetwork() && getTransmitterNetwork().getPrevTransferAmount() > 0) {
                buffer = getTransmitterNetwork().lastFluid;
            }
            TileEntityMechanicalPipe other = (TileEntityMechanicalPipe) tile;
            FluidStack otherBuffer = other.getBufferWithFallback();
            if (otherBuffer.isEmpty() && other.hasTransmitterNetwork() && other.getTransmitterNetwork().getPrevTransferAmount() > 0) {
                otherBuffer = other.getTransmitterNetwork().lastFluid;
            }
            return buffer.isEmpty() || otherBuffer.isEmpty() || buffer.isFluidEqual(otherBuffer);
        }
        return false;
    }

    @Override
    public FluidNetwork createEmptyNetwork() {
        return new FluidNetwork();
    }

    @Override
    public FluidNetwork createEmptyNetworkWithID(UUID networkID) {
        return new FluidNetwork(networkID);
    }

    @Override
    public FluidNetwork createNetworkByMerging(Collection<FluidNetwork> networks) {
        return new FluidNetwork(networks);
    }

    @Override
    protected boolean canHaveIncompatibleNetworks() {
        return true;
    }

    @Override
    public long getCapacity() {
        return tier.getPipeCapacity();
    }

    @Nonnull
    @Override
    public FluidStack releaseShare() {
        FluidStack ret = buffer.getFluid();
        buffer.setEmpty();
        return ret;
    }

    @Override
    public boolean noBufferOrFallback() {
        return getBufferWithFallback().isEmpty();
    }

    @Nonnull
    @Override
    public FluidStack getBufferWithFallback() {
        FluidStack buffer = getShare();
        //If we don't have a buffer try falling back to the network's buffer
        if (buffer.isEmpty() && hasTransmitterNetwork()) {
            return getTransmitterNetwork().getBuffer();
        }
        return buffer;
    }

    @Nonnull
    @Override
    public FluidStack getShare() {
        return buffer.getFluid();
    }

    @Override
    public void takeShare() {
        if (hasTransmitterNetwork()) {
            FluidNetwork network = getTransmitterNetwork();
            if (!network.fluidTank.isEmpty() && !saveShare.isEmpty()) {
                int amount = saveShare.getAmount();
                MekanismUtils.logMismatchedStackSize(network.fluidTank.shrinkStack(amount, Action.EXECUTE), amount);
                buffer.setStack(saveShare);
            }
        }
    }

    @Nonnull
    @Override
    public List<IExtendedFluidTank> getFluidTanks(@Nullable Direction side) {
        if (hasTransmitterNetwork()) {
            return getTransmitterNetwork().getFluidTanks(side);
        }
        return tanks;
    }

    @Override
    public void onContentsChanged() {
        markDirty(false);
    }

    /**
     * @return remainder
     */
    @Nonnull
    private FluidStack takeFluid(@Nonnull FluidStack fluid, Action action) {
        if (hasTransmitterNetwork()) {
            return getTransmitterNetwork().fluidTank.insert(fluid, action, AutomationType.INTERNAL);
        }
        return buffer.insert(fluid, action, AutomationType.INTERNAL);
    }

    @Override
    protected boolean canUpgrade(AlloyTier alloyTier) {
        return alloyTier.getBaseTier().ordinal() == tier.getBaseTier().ordinal() + 1;
    }

    @Nonnull
    @Override
    protected BlockState upgradeResult(@Nonnull BlockState current, @Nonnull BaseTier tier) {
        switch (tier) {
            case BASIC:
                return BlockStateHelper.copyStateData(current, MekanismBlocks.BASIC_MECHANICAL_PIPE.getBlock().getDefaultState());
            case ADVANCED:
                return BlockStateHelper.copyStateData(current, MekanismBlocks.ADVANCED_MECHANICAL_PIPE.getBlock().getDefaultState());
            case ELITE:
                return BlockStateHelper.copyStateData(current, MekanismBlocks.ELITE_MECHANICAL_PIPE.getBlock().getDefaultState());
            case ULTIMATE:
                return BlockStateHelper.copyStateData(current, MekanismBlocks.ULTIMATE_MECHANICAL_PIPE.getBlock().getDefaultState());
        }
        return current;
    }

    @Nullable
    @Override
    protected MechanicalPipeUpgradeData getUpgradeData() {
        return new MechanicalPipeUpgradeData(redstoneReactive, connectionTypes, getShare());
    }

    @Override
    protected void parseUpgradeData(@Nonnull TransmitterUpgradeData upgradeData) {
        if (upgradeData instanceof MechanicalPipeUpgradeData) {
            MechanicalPipeUpgradeData data = (MechanicalPipeUpgradeData) upgradeData;
            redstoneReactive = data.redstoneReactive;
            connectionTypes = data.connectionTypes;
            takeFluid(data.contents, Action.EXECUTE);
        } else {
            super.parseUpgradeData(upgradeData);
        }
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        //Note: We add the stored information to the initial update tag and not to the one we sync on side changes which uses getReducedUpdateTag
        CompoundNBT updateTag = super.getUpdateTag();
        if (hasTransmitterNetwork()) {
            updateTag.put(NBTConstants.FLUID_STORED, getTransmitterNetwork().lastFluid.writeToNBT(new CompoundNBT()));
            updateTag.putFloat(NBTConstants.SCALE, getTransmitterNetwork().currentScale);
        }
        return updateTag;
    }

    @Override
    protected void handleContentsUpdateTag(@Nonnull FluidNetwork network, @Nonnull CompoundNBT tag) {
        super.handleContentsUpdateTag(network, tag);
        NBTUtils.setFluidStackIfPresent(tag, NBTConstants.FLUID_STORED, network::setLastFluid);
        NBTUtils.setFloatIfPresent(tag, NBTConstants.SCALE, scale -> network.currentScale = scale);
    }
}