package mekanism.generators.common.content.turbine;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import mekanism.api.Action;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.inventory.AutomationType;
import mekanism.common.capabilities.chemical.multiblock.MultiblockGasTank;
import mekanism.common.registries.MekanismGases;
import mekanism.generators.common.tile.turbine.TileEntityTurbineCasing;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TurbineGasTank extends MultiblockGasTank<TurbineMultiblockData> {

    public TurbineGasTank(TurbineMultiblockData multiblock, TileEntityTurbineCasing tile) {
        super(multiblock, tile, multiblock::getSteamCapacity, gas -> gas == MekanismGases.STEAM.getChemical());
    }

    @Override
    public GasStack insert(@Nonnull GasStack stack, Action action, AutomationType automationType) {
        GasStack returned = super.insert(stack, action, automationType);
        if (action == Action.EXECUTE && multiblock.isFormed()) {
            multiblock.newSteamInput += stack.getAmount() - returned.getAmount();
        }
        return returned;
    }
}