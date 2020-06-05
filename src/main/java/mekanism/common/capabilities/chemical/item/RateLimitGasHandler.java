package mekanism.common.capabilities.chemical.item;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.LongSupplier;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import mekanism.api.Action;
import mekanism.api.IContentsListener;
import mekanism.api.annotations.NonNull;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.BasicGasTank;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.inventory.AutomationType;
import mekanism.common.capabilities.chemical.variable.RateLimitChemicalTank.RateLimitGasTank;
import mekanism.common.tier.ChemicalTankTier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RateLimitGasHandler extends ItemStackMekanismGasHandler {

    public static RateLimitGasHandler create(LongSupplier rate, LongSupplier capacity) {
        return create(rate, capacity, BasicGasTank.alwaysTrueBi, BasicGasTank.alwaysTrueBi, BasicGasTank.alwaysTrue, null);
    }

    public static RateLimitGasHandler create(ChemicalTankTier tier) {
        Objects.requireNonNull(tier, "Gas tank tier cannot be null");
        return new RateLimitGasHandler(handler -> new GasTankRateLimitGasTank(tier, handler));
    }

    public static RateLimitGasHandler create(LongSupplier rate, LongSupplier capacity, BiPredicate<@NonNull Gas, @NonNull AutomationType> canExtract,
          BiPredicate<@NonNull Gas, @NonNull AutomationType> canInsert, Predicate<@NonNull Gas> isValid) {
        return create(rate, capacity, canExtract, canInsert, isValid, null);
    }

    public static RateLimitGasHandler create(LongSupplier rate, LongSupplier capacity, BiPredicate<@NonNull Gas, @NonNull AutomationType> canExtract,
          BiPredicate<@NonNull Gas, @NonNull AutomationType> canInsert, Predicate<@NonNull Gas> isValid, @Nullable ChemicalAttributeValidator attributeValidator) {
        Objects.requireNonNull(rate, "Rate supplier cannot be null");
        Objects.requireNonNull(capacity, "Capacity supplier cannot be null");
        Objects.requireNonNull(canExtract, "Extraction validity check cannot be null");
        Objects.requireNonNull(canInsert, "Insertion validity check cannot be null");
        Objects.requireNonNull(isValid, "Gas validity check cannot be null");
        return new RateLimitGasHandler(listener -> new RateLimitGasTank(rate, capacity, canExtract, canInsert, isValid, attributeValidator, listener));
    }

    private final IGasTank tank;

    private RateLimitGasHandler(Function<IContentsListener, IGasTank> tankProvider) {
        this.tank = tankProvider.apply(this);
    }

    @Override
    protected List<IGasTank> getInitialTanks() {
        return Collections.singletonList(tank);
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    private static class GasTankRateLimitGasTank extends RateLimitGasTank {

        private final boolean isCreative;

        private GasTankRateLimitGasTank(ChemicalTankTier tier, IContentsListener listener) {
            super(tier::getOutput, tier::getStorage, BasicGasTank.alwaysTrueBi, BasicGasTank.alwaysTrueBi, BasicGasTank.alwaysTrue,
                  tier == ChemicalTankTier.CREATIVE ? ChemicalAttributeValidator.ALWAYS_ALLOW : null, listener);
            isCreative = tier == ChemicalTankTier.CREATIVE;
        }

        @Override
        public GasStack insert(GasStack stack, Action action, AutomationType automationType) {
            return super.insert(stack, action.combine(!isCreative), automationType);
        }

        @Override
        public GasStack extract(long amount, Action action, AutomationType automationType) {
            return super.extract(amount, action.combine(!isCreative), automationType);
        }

        /**
         * {@inheritDoc}
         *
         * Note: We are only patching {@link #setStackSize(long, Action)}, as both {@link #growStack(long, Action)} and {@link #shrinkStack(long, Action)} are wrapped
         * through this method.
         */
        @Override
        public long setStackSize(long amount, Action action) {
            return super.setStackSize(amount, action.combine(!isCreative));
        }
    }
}