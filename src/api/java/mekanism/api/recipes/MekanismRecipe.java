package mekanism.api.recipes;

import javax.annotation.Nonnull;
import mekanism.api.inventory.IgnoredIInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

//TODO: Make implementations override equals and hashcode?
public abstract class MekanismRecipe implements IRecipe<IgnoredIInventory> {

    private final ResourceLocation id;

    protected MekanismRecipe(ResourceLocation id) {
        this.id = id;
    }

    /**
     * Writes this recipe to a PacketBuffer.
     *
     * @param buffer The buffer to write to.
     */
    public abstract void write(PacketBuffer buffer);

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public boolean matches(@Nonnull IgnoredIInventory inv, @Nonnull World world) {
        //TODO: Check if this matches properly?
        return true;
    }

    @Override
    public boolean isDynamic() {
        //TODO: If we make this non dynamic, we can make it show in vanilla's crafting book
        // and also then obey the recipe locking.
        // For now none of that works/makes sense in our concept so don't lock it
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull IgnoredIInventory inv) {
        //TODO
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        //TODO: This is always true for cooking recipes, so I assume we probably also want it true
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        //TODO
        return ItemStack.EMPTY;
    }
}