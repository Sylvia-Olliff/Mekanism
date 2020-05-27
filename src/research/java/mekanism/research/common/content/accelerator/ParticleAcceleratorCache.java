package mekanism.research.common.content.accelerator;

import mekanism.common.lib.multiblock.MultiblockCache;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.List;

public class ParticleAcceleratorCache extends MultiblockCache<ParticleAcceleratorMultiblockData> {

    @Override
    public void merge(MultiblockCache<ParticleAcceleratorMultiblockData> mergeCache, List<ItemStack> rejectedItems) {
        super.merge(mergeCache, rejectedItems);
    }

    @Override
    public void apply(ParticleAcceleratorMultiblockData data) {
        super.apply(data);
    }

    @Override
    public void sync(ParticleAcceleratorMultiblockData data) {
        super.sync(data);
    }

    @Override
    public void load(CompoundNBT nbtTags) {
        super.load(nbtTags);
    }

    @Override
    public void save(CompoundNBT nbtTags) {
        super.save(nbtTags);
    }
}
