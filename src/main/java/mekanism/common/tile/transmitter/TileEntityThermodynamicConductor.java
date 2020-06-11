package mekanism.common.tile.transmitter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.api.DataHandlerUtils;
import mekanism.api.NBTConstants;
import mekanism.api.heat.HeatAPI;
import mekanism.api.heat.IHeatCapacitor;
import mekanism.api.heat.IHeatHandler;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.tier.AlloyTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.states.BlockStateHelper;
import mekanism.common.block.states.TransmitterType;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.ITileHeatHandler;
import mekanism.common.capabilities.proxy.ProxyHeatHandler;
import mekanism.common.capabilities.resolver.advanced.AdvancedCapabilityResolver;
import mekanism.common.content.transmitter.HeatNetwork;
import mekanism.common.lib.Color;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tier.ConductorTier;
import mekanism.common.upgrade.transmitter.ThermodynamicConductorUpgradeData;
import mekanism.common.upgrade.transmitter.TransmitterUpgradeData;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants.NBT;

public class TileEntityThermodynamicConductor extends TileEntityTransmitter<IHeatHandler, HeatNetwork, TileEntityThermodynamicConductor> implements ITileHeatHandler {

    public final ConductorTier tier;

    private double clientTemperature = HeatAPI.AMBIENT_TEMP;

    private final List<IHeatCapacitor> capacitors;
    public final BasicHeatCapacitor buffer;

    public TileEntityThermodynamicConductor(IBlockProvider blockProvider) {
        super(blockProvider);
        this.tier = Attribute.getTier(blockProvider.getBlock(), ConductorTier.class);
        buffer = BasicHeatCapacitor.create(tier.getHeatCapacity(), tier.getInverseConduction(), tier.getInverseConductionInsulation(), this);
        capacitors = Collections.singletonList(buffer);
        addCapabilityResolver(AdvancedCapabilityResolver.readOnly(Capabilities.HEAT_HANDLER_CAPABILITY, this, () -> new ProxyHeatHandler(this, null, null)));
    }

    @Override
    public HeatNetwork createEmptyNetwork() {
        return new HeatNetwork();
    }

    @Override
    public HeatNetwork createEmptyNetworkWithID(UUID networkID) {
        return new HeatNetwork(networkID);
    }

    @Override
    public HeatNetwork createNetworkByMerging(Collection<HeatNetwork> networks) {
        return new HeatNetwork(networks);
    }

    @Override
    public void takeShare() {
    }

    @Override
    public TransmitterType getTransmitterType() {
        return TransmitterType.THERMODYNAMIC_CONDUCTOR;
    }

    @Override
    public boolean isValidAcceptor(TileEntity tile, Direction side) {
        return acceptorCache.isAcceptorAndListen(tile, side, Capabilities.HEAT_HANDLER_CAPABILITY);
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT tag) {
        super.write(tag);
        tag.put(NBTConstants.HEAT_CAPACITORS, DataHandlerUtils.writeContainers(getHeatCapacitors(null)));
        return tag;
    }

    @Override
    public void read(@Nonnull CompoundNBT tag) {
        super.read(tag);
        DataHandlerUtils.readContainers(getHeatCapacitors(null), tag.getList(NBTConstants.HEAT_CAPACITORS, NBT.TAG_COMPOUND));
    }

    @Nonnull
    @Override
    public CompoundNBT getReducedUpdateTag() {
        CompoundNBT updateTag = super.getReducedUpdateTag();
        updateTag.putDouble(NBTConstants.TEMPERATURE, buffer.getHeat());
        return updateTag;
    }

    @Override
    public void handleUpdateTag(@Nonnull CompoundNBT tag) {
        super.handleUpdateTag(tag);
        NBTUtils.setDoubleIfPresent(tag, NBTConstants.TEMPERATURE, buffer::setHeat);
    }

    public Color getBaseColor() {
        return tier.getBaseColor();
    }

    @Nonnull
    @Override
    public List<IHeatCapacitor> getHeatCapacitors(Direction side) {
        return capacitors;
    }

    @Override
    public void onContentsChanged() {
        if (!isRemote()) {
            if (Math.abs(buffer.getTemperature() - clientTemperature) > (buffer.getTemperature() / 20)) {
                clientTemperature = buffer.getTemperature();
                sendUpdatePacket();
            }
        }
        markDirty(false);
    }

    @Nullable
    @Override
    public IHeatHandler getAdjacent(Direction side) {
        if (connectionMapContainsSide(getAllCurrentConnections(), side)) {
            //Note: We use the acceptor cache as the heat network is different and the transmitters count the other transmitters in the
            // network as valid acceptors
            return MekanismUtils.toOptional(acceptorCache.getConnectedAcceptor(side)).orElse(null);
        }
        return null;
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
                return BlockStateHelper.copyStateData(current, MekanismBlocks.BASIC_THERMODYNAMIC_CONDUCTOR.getBlock().getDefaultState());
            case ADVANCED:
                return BlockStateHelper.copyStateData(current, MekanismBlocks.ADVANCED_THERMODYNAMIC_CONDUCTOR.getBlock().getDefaultState());
            case ELITE:
                return BlockStateHelper.copyStateData(current, MekanismBlocks.ELITE_THERMODYNAMIC_CONDUCTOR.getBlock().getDefaultState());
            case ULTIMATE:
                return BlockStateHelper.copyStateData(current, MekanismBlocks.ULTIMATE_THERMODYNAMIC_CONDUCTOR.getBlock().getDefaultState());
        }
        return current;
    }

    @Nullable
    @Override
    protected ThermodynamicConductorUpgradeData getUpgradeData() {
        return new ThermodynamicConductorUpgradeData(redstoneReactive, connectionTypes, buffer.getHeat());
    }

    @Override
    protected void parseUpgradeData(@Nonnull TransmitterUpgradeData upgradeData) {
        if (upgradeData instanceof ThermodynamicConductorUpgradeData) {
            ThermodynamicConductorUpgradeData data = (ThermodynamicConductorUpgradeData) upgradeData;
            redstoneReactive = data.redstoneReactive;
            connectionTypes = data.connectionTypes;
            buffer.setHeat(data.heat);
        } else {
            super.parseUpgradeData(upgradeData);
        }
    }
}