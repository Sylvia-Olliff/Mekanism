package mekanism.common.capabilities.resolver.manager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.gas.ISidedGasHandler;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.proxy.ProxyGasHandler;

/**
 * Helper class to make reading instead of having as messy generics
 */
public class GasHandlerManager extends CapabilityHandlerManager<IChemicalTankHolder<Gas, GasStack, IGasTank>, IGasTank, IGasHandler, ISidedGasHandler> {

    public GasHandlerManager(@Nullable IChemicalTankHolder<Gas, GasStack, IGasTank> holder, @Nonnull ISidedGasHandler baseHandler) {
        super(holder, baseHandler, Capabilities.GAS_HANDLER_CAPABILITY, ProxyGasHandler::new, IChemicalTankHolder::getTanks);
    }
}