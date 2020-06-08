package mekanism.research.common.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ResearchCapabilityProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(ResearchPlayerCapability.class)
    public static final Capability<ResearchPlayerCapability> RESEARCH_PLAYER_CAPABILITY = null;

    private LazyOptional<ResearchPlayerCapability> instance = LazyOptional.of(RESEARCH_PLAYER_CAPABILITY::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == RESEARCH_PLAYER_CAPABILITY) {
            // Return the given instance if it has been set, else recreate it. Covers someone accidentally invalidating the capability.
            if (instance == null || !instance.isPresent()) {
                instance = LazyOptional.of(RESEARCH_PLAYER_CAPABILITY::getDefaultInstance);
            }
            return instance.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return RESEARCH_PLAYER_CAPABILITY.getStorage().writeNBT(RESEARCH_PLAYER_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        RESEARCH_PLAYER_CAPABILITY.getStorage().readNBT(RESEARCH_PLAYER_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}
