package mekanism.common.inventory.slot.chemical;

import com.mojang.datafixers.util.Pair;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import mekanism.api.IContentsListener;
import mekanism.api.annotations.FieldsAreNonnullByDefault;
import mekanism.api.annotations.NonNull;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.pigment.IPigmentHandler;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PigmentInventorySlot extends ChemicalInventorySlot<Pigment, PigmentStack> {

    @Nullable
    public static IPigmentHandler getCapability(ItemStack stack) {
        return getCapability(stack, Capabilities.PIGMENT_HANDLER_CAPABILITY);
    }

    //TODO: Implement creators as needed
    private PigmentInventorySlot(IPigmentTank pigmentTank, Supplier<World> worldSupplier, Predicate<@NonNull ItemStack> canExtract,
          Predicate<@NonNull ItemStack> canInsert, Predicate<@NonNull ItemStack> validator, @Nullable IContentsListener listener, int x, int y) {
        super(pigmentTank, worldSupplier, canExtract, canInsert, validator, listener, x, y);
    }

    @Nullable
    @Override
    protected IChemicalHandler<Pigment, PigmentStack> getCapability() {
        return getCapability(current);
    }

    @Nullable
    @Override
    protected Pair<ItemStack, PigmentStack> getConversion() {
        return null;
    }
}