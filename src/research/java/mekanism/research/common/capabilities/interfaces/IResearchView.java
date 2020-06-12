package mekanism.research.common.capabilities.interfaces;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IResearchView {
    long getPlayerResearch(PlayerEntity player);
}
