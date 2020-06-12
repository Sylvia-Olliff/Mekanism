package mekanism.research.common.capabilities;

import mekanism.research.common.capabilities.interfaces.IResearchView;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ResearchViewCapabilityProvider implements ICapabilityProvider {

    @CapabilityInject(IResearchView.class)
    public static final Capability<IResearchView> RESEARCH_VIEW_CAPABILITY = null;

    private LazyOptional<IResearchView> instance = LazyOptional.of(RESEARCH_VIEW_CAPABILITY::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == RESEARCH_VIEW_CAPABILITY) {
            if (instance == null || !instance.isPresent()) {
                instance = LazyOptional.of(RESEARCH_VIEW_CAPABILITY::getDefaultInstance);
            }
            return instance.cast();
        }
        return LazyOptional.empty();
    }
}
