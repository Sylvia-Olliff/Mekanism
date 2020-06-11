package mekanism.common.item;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import mekanism.api.Action;
import mekanism.api.MekanismAPI;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.heat.IHeatHandler;
import mekanism.api.inventory.AutomationType;
import mekanism.api.math.FloatingLong;
import mekanism.api.text.EnumColor;
import mekanism.api.text.TextComponentUtil;
import mekanism.common.MekanismLang;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.transmitter.EnergyNetwork;
import mekanism.common.lib.transmitter.DynamicNetwork;
import mekanism.common.lib.transmitter.TransmitterNetworkRegistry;
import mekanism.common.tile.transmitter.TileEntityBufferedTransmitter;
import mekanism.common.tile.transmitter.TileEntityTransmitter;
import mekanism.common.util.CapabilityUtils;
import mekanism.common.util.EnumUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.StorageUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class ItemNetworkReader extends ItemEnergized {

    public ItemNetworkReader(Properties properties) {
        super(MekanismConfig.gear.networkReaderChargeRate, MekanismConfig.gear.networkReaderMaxEnergy, properties.rarity(Rarity.UNCOMMON));
    }

    @Nonnull
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        if (!world.isRemote && player != null) {
            ItemStack stack = player.getHeldItem(context.getHand());
            BlockPos pos = context.getPos();
            TileEntity tileEntity = MekanismUtils.getTileEntity(world, pos);
            if (tileEntity != null) {
                if (!player.isCreative()) {
                    FloatingLong energyPerUse = MekanismConfig.gear.networkReaderEnergyUsage.get();
                    IEnergyContainer energyContainer = StorageUtils.getEnergyContainer(stack, 0);
                    if (energyContainer == null || energyContainer.extract(energyPerUse, Action.SIMULATE, AutomationType.MANUAL).smallerThan(energyPerUse)) {
                        return ActionResultType.FAIL;
                    }
                    energyContainer.extract(energyPerUse, Action.EXECUTE, AutomationType.MANUAL);
                }
                Direction opposite = context.getFace().getOpposite();
                if (tileEntity instanceof TileEntityTransmitter) {
                    TileEntityTransmitter<?, ?, ?> transmitter = (TileEntityTransmitter<?, ?, ?>) tileEntity;
                    player.sendMessage(MekanismLang.NETWORK_READER_BORDER.translateColored(EnumColor.GRAY, "-------------",
                          MekanismLang.GENERIC_SQUARE_BRACKET.translateColored(EnumColor.DARK_BLUE, MekanismLang.MEKANISM)));
                    player.sendMessage(MekanismLang.NETWORK_READER_TRANSMITTERS.translateColored(EnumColor.GRAY, EnumColor.DARK_GRAY, transmitter.getTransmitterNetworkSize()));
                    player.sendMessage(MekanismLang.NETWORK_READER_ACCEPTORS.translateColored(EnumColor.GRAY, EnumColor.DARK_GRAY, transmitter.getTransmitterNetworkAcceptorSize()));
                    player.sendMessage(MekanismLang.NETWORK_READER_NEEDED.translateColored(EnumColor.GRAY, EnumColor.DARK_GRAY, transmitter.getTransmitterNetworkNeeded()));
                    player.sendMessage(MekanismLang.NETWORK_READER_BUFFER.translateColored(EnumColor.GRAY, EnumColor.DARK_GRAY, transmitter.getTransmitterNetworkBuffer()));
                    player.sendMessage(MekanismLang.NETWORK_READER_THROUGHPUT.translateColored(EnumColor.GRAY, EnumColor.DARK_GRAY, transmitter.getTransmitterNetworkFlow()));
                    DynamicNetwork<?, ?, ?> transmitterNetwork = transmitter.getTransmitterNetwork();
                    if (transmitterNetwork instanceof EnergyNetwork) {
                        player.sendMessage(MekanismLang.NETWORK_READER_CAPACITY.translateColored(EnumColor.GRAY, EnumColor.DARK_GRAY, ((EnergyNetwork) transmitterNetwork).getCapacityAsFloatingLong()));
                    } else if (transmitter instanceof TileEntityBufferedTransmitter) {
                        player.sendMessage(MekanismLang.NETWORK_READER_CAPACITY.translateColored(EnumColor.GRAY, EnumColor.DARK_GRAY,
                              ((TileEntityBufferedTransmitter<?, ?, ?, ?>) transmitter).getTransmitterNetworkCapacity()));
                    }
                    CapabilityUtils.getCapability(tileEntity, Capabilities.HEAT_HANDLER_CAPABILITY, opposite).ifPresent(heatHandler ->
                          player.sendMessage(MekanismLang.NETWORK_READER_TEMPERATURE.translateColored(EnumColor.GRAY, EnumColor.DARK_GRAY,
                                MekanismUtils.getTemperatureDisplay(heatHandler.getTotalTemperature(), TemperatureUnit.KELVIN, true))));
                    player.sendMessage(MekanismLang.NETWORK_READER_BORDER.translateColored(EnumColor.GRAY, "-------------", EnumColor.DARK_BLUE, "[=======]"));
                } else {
                    Optional<IHeatHandler> heatHandler = MekanismUtils.toOptional(CapabilityUtils.getCapability(tileEntity, Capabilities.HEAT_HANDLER_CAPABILITY, opposite));
                    if (heatHandler.isPresent()) {
                        IHeatHandler transfer = heatHandler.get();
                        MekanismLang.NETWORK_READER_BORDER.translateColored(EnumColor.GRAY, "-------------",
                              MekanismLang.GENERIC_SQUARE_BRACKET.translateColored(EnumColor.DARK_BLUE, MekanismLang.MEKANISM));
                        ITextComponent temp = MekanismUtils.getTemperatureDisplay(transfer.getTotalTemperature(), TemperatureUnit.KELVIN, true);
                        player.sendMessage(MekanismLang.NETWORK_READER_TEMPERATURE.translateColored(EnumColor.GRAY, EnumColor.DARK_GRAY, temp));
                        player.sendMessage(MekanismLang.NETWORK_READER_BORDER.translateColored(EnumColor.GRAY, "-------------", EnumColor.DARK_BLUE, "[=======]"));
                    } else {
                        Set<DynamicNetwork<?, ?, ?>> iteratedNetworks = new ObjectOpenHashSet<>();
                        for (Direction side : EnumUtils.DIRECTIONS) {
                            TileEntity tile = MekanismUtils.getTileEntity(world, pos.offset(side));
                            if (tile instanceof TileEntityTransmitter) {
                                TileEntityTransmitter<?, ?, ?> transmitter = (TileEntityTransmitter<?, ?, ?>) tile;
                                DynamicNetwork<?, ?, ?> transmitterNetwork = transmitter.getTransmitterNetwork();
                                if (transmitterNetwork.hasAcceptor(pos) && !iteratedNetworks.contains(transmitterNetwork)) {
                                    player.sendMessage(MekanismLang.NETWORK_READER_BORDER.translateColored(EnumColor.GRAY, "-------------",
                                          MekanismLang.GENERIC_SQUARE_BRACKET.translateColored(EnumColor.DARK_BLUE, transmitter.getTransmissionType())));
                                    player.sendMessage(MekanismLang.NETWORK_READER_CONNECTED_SIDES.translateColored(EnumColor.GRAY, EnumColor.DARK_GRAY,
                                          getDirections(transmitterNetwork.getAcceptorDirections(pos))));
                                    player.sendMessage(MekanismLang.NETWORK_READER_BORDER.translateColored(EnumColor.GRAY, "-------------", EnumColor.DARK_BLUE, "[=======]"));
                                    iteratedNetworks.add(transmitterNetwork);
                                }
                            }
                        }
                    }
                }
                return ActionResultType.SUCCESS;
            }
            if (player.isSneaking() && MekanismAPI.debug) {
                MekanismLang.NETWORK_READER_BORDER.translateColored(EnumColor.GRAY, "----------",
                      MekanismLang.GENERIC_SQUARE_BRACKET.translateColored(EnumColor.DARK_BLUE, MekanismLang.DEBUG_TITLE));
                for (ITextComponent component : TransmitterNetworkRegistry.getInstance().toComponents()) {
                    player.sendMessage(TextComponentUtil.build(EnumColor.DARK_GRAY, component));
                }
                player.sendMessage(MekanismLang.NETWORK_READER_BORDER.translateColored(EnumColor.GRAY, "-------------", EnumColor.DARK_BLUE, "[=======]"));
            }
        }
        return ActionResultType.PASS;
    }

    private ITextComponent getDirections(Set<Direction> directions) {
        if (directions.isEmpty()) {
            return MekanismLang.GENERIC_SQUARE_BRACKET.translate("");
        }
        ITextComponent component = null;
        for (Direction direction : directions) {
            if (component == null) {
                component = TextComponentUtil.build(direction);
            } else {
                component = MekanismLang.GENERIC_WITH_COMMA.translate(component, direction);
            }
        }
        return MekanismLang.GENERIC_SQUARE_BRACKET.translate(component);
    }
}