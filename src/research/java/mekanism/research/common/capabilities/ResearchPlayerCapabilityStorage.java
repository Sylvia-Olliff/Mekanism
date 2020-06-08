package mekanism.research.common.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

import javax.annotation.Nullable;

public class ResearchPlayerCapabilityStorage implements IStorage<ResearchPlayerCapability> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<ResearchPlayerCapability> capability, ResearchPlayerCapability instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<ResearchPlayerCapability> capability, ResearchPlayerCapability instance, Direction side, INBT nbt) {
        instance.deserializeNBT((CompoundNBT) nbt);
    }
}
