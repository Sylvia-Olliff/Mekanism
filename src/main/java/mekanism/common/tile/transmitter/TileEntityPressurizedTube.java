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
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.BasicGasTank;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.api.chemical.gas.IGasHandler.IMekanismGasHandler;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.inventory.AutomationType;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.tier.AlloyTier;
import mekanism.api.tier.BaseTier;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.states.BlockStateHelper;
import mekanism.common.block.states.TransmitterType;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.proxy.ProxyChemicalHandler.ProxyGasHandler;
import mekanism.common.capabilities.resolver.advanced.AdvancedCapabilityResolver;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tier.TubeTier;
import mekanism.common.transmitters.TransmitterImpl;
import mekanism.common.transmitters.grid.GasNetwork;
import mekanism.common.upgrade.transmitter.PressurizedTubeUpgradeData;
import mekanism.common.upgrade.transmitter.TransmitterUpgradeData;
import mekanism.common.util.CapabilityUtils;
import mekanism.common.util.GasUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants.NBT;

//TODO - V10: Figure out how to make this work for multiple chemical types
public class TileEntityPressurizedTube extends TileEntityTransmitter<IGasHandler, GasNetwork, GasStack> implements IMekanismGasHandler {

    public final TubeTier tier;
    @Nonnull
    public GasStack saveShare = GasStack.EMPTY;
    private final List<IGasTank> tanks;
    public BasicGasTank buffer;

    public TileEntityPressurizedTube(IBlockProvider blockProvider) {
        super(blockProvider);
        this.tier = Attribute.getTier(blockProvider.getBlock(), TubeTier.class);
        buffer = BasicGasTank.create(getCapacity(), BasicGasTank.alwaysFalse, BasicGasTank.alwaysTrue, BasicGasTank.alwaysTrue, ChemicalAttributeValidator.ALWAYS_ALLOW, this);
        tanks = Collections.singletonList(buffer);
        addCapabilityResolver(AdvancedCapabilityResolver.readOnly(Capabilities.GAS_HANDLER_CAPABILITY, this, () -> new ProxyGasHandler(this, null, null)));
    }

    @Override
    public void tick() {
        if (!isRemote()) {
            Set<Direction> connections = getConnections(ConnectionType.PULL);
            if (!connections.isEmpty()) {
                for (IGasHandler connectedAcceptor : GasUtils.getConnectedAcceptors(getPos(), getWorld(), connections)) {
                    if (connectedAcceptor != null) {
                        GasStack received;
                        //Note: We recheck the buffer each time in case we ended up accepting gas somewhere
                        // and our buffer changed and is no longer empty
                        GasStack bufferWithFallback = getBufferWithFallback();
                        if (bufferWithFallback.isEmpty()) {
                            //If we don't have a gas stored try pulling as much as we are able to
                            received = connectedAcceptor.extractChemical(getAvailablePull(), Action.SIMULATE);
                        } else {
                            //Otherwise try draining the same type of gas we have stored requesting up to as much as we are able to pull
                            // We do this to better support multiple tanks in case the gas we have stored we could pull out of a block's
                            // second tank but just asking to drain a specific amount
                            received = connectedAcceptor.extractChemical(new GasStack(bufferWithFallback, getAvailablePull()), Action.SIMULATE);
                        }
                        if (!received.isEmpty() && takeGas(received, Action.SIMULATE).isEmpty()) {
                            //If we received some gas and are able to insert it all
                            GasStack remainder = takeGas(received, Action.EXECUTE);
                            connectedAcceptor.extractChemical(new GasStack(received, received.getAmount() - remainder.getAmount()), Action.EXECUTE);
                        }
                    }
                }
            }
        }
        super.tick();
    }

    private long getAvailablePull() {
        if (getTransmitter().hasTransmitterNetwork()) {
            return Math.min(tier.getTubePullAmount(), getTransmitter().getTransmitterNetwork().gasTank.getNeeded());
        }
        return Math.min(tier.getTubePullAmount(), buffer.getNeeded());
    }

    @Nonnull
    @Override
    public GasStack insertChemical(int tank, @Nonnull GasStack stack, @Nullable Direction side, @Nonnull Action action) {
        IGasTank gasTank = getChemicalTank(tank, side);
        if (gasTank == null) {
            return stack;
        } else if (side == null) {
            return gasTank.insert(stack, action, AutomationType.INTERNAL);
        }
        //If we have a side only allow inserting if our connection allows it
        ConnectionType connectionType = getConnectionType(side);
        if (connectionType == ConnectionType.NORMAL || connectionType == ConnectionType.PULL) {
            return gasTank.insert(stack, action, AutomationType.EXTERNAL);
        }
        return stack;
    }

    @Override
    public void read(CompoundNBT nbtTags) {
        super.read(nbtTags);
        if (nbtTags.contains(NBTConstants.GAS_STORED, NBT.TAG_COMPOUND)) {
            saveShare = GasStack.readFromNBT(nbtTags.getCompound(NBTConstants.GAS_STORED));
        } else {
            saveShare = GasStack.EMPTY;
        }
        buffer.setStack(saveShare);
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT nbtTags) {
        super.write(nbtTags);
        if (getTransmitter().hasTransmitterNetwork()) {
            getTransmitter().getTransmitterNetwork().validateSaveShares(getTransmitter());
        }
        if (saveShare.isEmpty()) {
            nbtTags.remove(NBTConstants.GAS_STORED);
        } else {
            nbtTags.put(NBTConstants.GAS_STORED, saveShare.write(new CompoundNBT()));
        }
        return nbtTags;
    }

    @Override
    public TransmissionType getTransmissionType() {
        return TransmissionType.GAS;
    }

    @Override
    public TransmitterType getTransmitterType() {
        return TransmitterType.PRESSURIZED_TUBE;
    }

    @Override
    public boolean isValidAcceptor(TileEntity tile, Direction side) {
        if (CapabilityUtils.getCapability(tile, Capabilities.GRID_TRANSMITTER_CAPABILITY, null).filter(transmitter ->
              TransmissionType.checkTransmissionType(transmitter, TransmissionType.GAS)).isPresent()) {
            return false;
        }
        return isAcceptorAndListen(tile, side, Capabilities.GAS_HANDLER_CAPABILITY);
    }

    @Override
    public boolean isValidTransmitter(TileEntity tile) {
        if (!super.isValidTransmitter(tile)) {
            return false;
        }
        if (!(tile instanceof TileEntityPressurizedTube)) {
            return true;
        }
        Gas buffer = getBufferWithFallback().getType();
        if (buffer.isEmptyType() && getTransmitter().hasTransmitterNetwork() && getTransmitter().getTransmitterNetwork().getPrevTransferAmount() > 0) {
            buffer = getTransmitter().getTransmitterNetwork().lastGas;
        }
        TileEntityPressurizedTube other = (TileEntityPressurizedTube) tile;
        Gas otherBuffer = other.getBufferWithFallback().getType();
        if (otherBuffer.isEmptyType() && other.getTransmitter().hasTransmitterNetwork() && other.getTransmitter().getTransmitterNetwork().getPrevTransferAmount() > 0) {
            otherBuffer = other.getTransmitter().getTransmitterNetwork().lastGas;
        }
        return buffer.isEmptyType() || otherBuffer.isEmptyType() || buffer == otherBuffer;
    }

    @Override
    public GasNetwork createNewNetwork() {
        return new GasNetwork();
    }

    @Override
    public GasNetwork createNewNetworkWithID(UUID networkID) {
        return new GasNetwork(networkID);
    }

    @Override
    public GasNetwork createNetworkByMerging(Collection<GasNetwork> networks) {
        return new GasNetwork(networks);
    }

    @Override
    protected boolean canHaveIncompatibleNetworks() {
        return true;
    }

    @Override
    public long getCapacity() {
        return tier.getTubeCapacity();
    }

    @Nonnull
    @Override
    public GasStack releaseShare() {
        GasStack ret = buffer.getStack();
        buffer.setEmpty();
        return ret;
    }

    @Override
    public GasStack getShare() {
        return buffer.getStack();
    }

    @Override
    public boolean noBufferOrFallback() {
        return getBufferWithFallback().isEmpty();
    }

    @Nonnull
    @Override
    public GasStack getBufferWithFallback() {
        GasStack buffer = getShare();
        //If we don't have a buffer try falling back to the network's buffer
        if (buffer.isEmpty() && getTransmitter().hasTransmitterNetwork()) {
            return getTransmitter().getTransmitterNetwork().getBuffer();
        }
        return buffer;
    }

    @Override
    public void takeShare() {
        if (getTransmitter().hasTransmitterNetwork()) {
            GasNetwork transmitterNetwork = getTransmitter().getTransmitterNetwork();
            if (!transmitterNetwork.gasTank.isEmpty() && !saveShare.isEmpty()) {
                long amount = saveShare.getAmount();
                MekanismUtils.logMismatchedStackSize(transmitterNetwork.gasTank.shrinkStack(amount, Action.EXECUTE), amount);
                buffer.setStack(saveShare);
            }
        }
    }

    /**
     * @return remainder
     */
    @Nonnull
    private GasStack takeGas(GasStack gasStack, Action action) {
        if (getTransmitter().hasTransmitterNetwork()) {
            return getTransmitter().getTransmitterNetwork().gasTank.insert(gasStack, action, AutomationType.INTERNAL);
        }
        return buffer.insert(gasStack, action, AutomationType.INTERNAL);
    }

    @Nonnull
    @Override
    public List<IGasTank> getChemicalTanks(@Nullable Direction side) {
        if (getTransmitter().hasTransmitterNetwork()) {
            return getTransmitter().getTransmitterNetwork().getChemicalTanks(side);
        }
        return tanks;
    }

    @Override
    public void onContentsChanged() {
        markDirty(false);
    }

    @Override
    public IGasHandler getCachedAcceptor(Direction side) {
        return MekanismUtils.toOptional(CapabilityUtils.getCapability(getCachedTile(side), Capabilities.GAS_HANDLER_CAPABILITY, side.getOpposite())).orElse(null);
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
                return BlockStateHelper.copyStateData(current, MekanismBlocks.BASIC_PRESSURIZED_TUBE.getBlock().getDefaultState());
            case ADVANCED:
                return BlockStateHelper.copyStateData(current, MekanismBlocks.ADVANCED_PRESSURIZED_TUBE.getBlock().getDefaultState());
            case ELITE:
                return BlockStateHelper.copyStateData(current, MekanismBlocks.ELITE_PRESSURIZED_TUBE.getBlock().getDefaultState());
            case ULTIMATE:
                return BlockStateHelper.copyStateData(current, MekanismBlocks.ULTIMATE_PRESSURIZED_TUBE.getBlock().getDefaultState());
        }
        return current;
    }

    @Nullable
    @Override
    protected PressurizedTubeUpgradeData getUpgradeData() {
        return new PressurizedTubeUpgradeData(redstoneReactive, connectionTypes, getShare());
    }

    @Override
    protected void parseUpgradeData(@Nonnull TransmitterUpgradeData upgradeData) {
        if (upgradeData instanceof PressurizedTubeUpgradeData) {
            PressurizedTubeUpgradeData data = (PressurizedTubeUpgradeData) upgradeData;
            redstoneReactive = data.redstoneReactive;
            connectionTypes = data.connectionTypes;
            takeGas(data.contents, Action.EXECUTE);
        } else {
            super.parseUpgradeData(upgradeData);
        }
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        //Note: We add the stored information to the initial update tag and not to the one we sync on side changes which uses getReducedUpdateTag
        CompoundNBT updateTag = super.getUpdateTag();
        TransmitterImpl<IGasHandler, GasNetwork, GasStack> transmitter = getTransmitter();
        if (transmitter.hasTransmitterNetwork()) {
            updateTag.put(NBTConstants.GAS_STORED, transmitter.getTransmitterNetwork().lastGas.write(new CompoundNBT()));
            updateTag.putFloat(NBTConstants.SCALE, transmitter.getTransmitterNetwork().gasScale);
        }
        return updateTag;
    }

    @Override
    protected void handleContentsUpdateTag(@Nonnull GasNetwork network, @Nonnull CompoundNBT tag) {
        NBTUtils.setGasIfPresent(tag, NBTConstants.GAS_STORED, network::setLastGas);
        NBTUtils.setFloatIfPresent(tag, NBTConstants.SCALE, scale -> network.gasScale = scale);
    }
}