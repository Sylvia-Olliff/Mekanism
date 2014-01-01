package mekanism.api.gas;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Gas - a class used to set specific properties of gasses when used or seen in-game.
 * @author aidancbrady
 *
 */
public class Gas
{
	private String name;
	
	private String unlocalizedName;
	
	private Fluid fluid;
	
	private Icon icon;
	
	/**
	 * Creates a new Gas object with a defined name or key value.
	 * @param s - name or key to associate this Gas with
	 */
	public Gas(String s)
	{
		unlocalizedName = name = s;
	}
	
	/**
	 * Gets the name (key) of this Gas. This is NOT a translated or localized display name.
	 * @return this Gas's name or key
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets the unlocalized name of this Gas.
	 * @return this Gas's unlocalized name
	 */
	public String getUnlocalizedName()
	{
		return "gas." + unlocalizedName;
	}
	
	/**
	 * Translates this Gas's unlocalized name and returns it as localized.
	 * @return this Gas's localized name
	 */
	public String getLocalizedName()
	{
		return StatCollector.translateToLocal(getUnlocalizedName());
	}
	
	/**
	 * Sets the unlocalized name of this Gas.
	 * @param s - unlocalized name to set
	 * @return this Gas object
	 */
	public Gas setUnlocalizedName(String s)
	{
		unlocalizedName = s;
		
		return this;
	}
	
	/**
	 * Gets the Icon associated with this Gas.
	 * @return associated Icon
	 */
	public Icon getIcon()
	{
		return icon;
	}
	
	/**
	 * Sets this gas's icon.
	 * @param i - Icon to associate with this Gas
	 * @return this Gas object
	 */
	public Gas setIcon(Icon i)
	{
		icon = i;
		
		if(hasFluid())
		{
			fluid.setIcons(getIcon());
		}
		
		return this;
	}
	
	/**
	 * Gets the ID associated with this gas.
	 * @return the associated gas ID
	 */
	public int getID()
	{
		return GasRegistry.getGasID(this);
	}
	
	/**
	 * Writes this Gas to a defined tag compound.
	 * @param nbtTags - tag compound to write this Gas to
	 * @return the tag compound this gas was written to
	 */
	public NBTTagCompound write(NBTTagCompound nbtTags)
	{
		nbtTags.setString("gasName", getName());
		
		return nbtTags;
	}
	
	/**
	 * Returns the Gas stored in the defined tag compound.
	 * @param nbtTags - tag compound to get the Gas from
	 * @return Gas stored in the tag compound
	 */
	public static Gas readFromNBT(NBTTagCompound nbtTags)
	{
		if(nbtTags == null || nbtTags.hasNoTags())
		{
			return null;
		}
		
		return GasRegistry.getGas(nbtTags.getString("gasName"));
	}
	
	/**
	 * Whether or not this Gas has an associated fluid.
	 * @return if this gas has a fluid
	 */
	public boolean hasFluid()
	{
		return fluid != null;
	}
	
	/**
	 * Gets the fluid associated with this Gas.
	 * @return fluid associated with this gas
	 */
	public Fluid getFluid()
	{
		return fluid;
	}
	
	/**
	 * Registers a new fluid out of this Gas or gets one from the FluidRegistry.
	 * @return this Gas object
	 */
	public Gas registerFluid()
	{
		if(fluid == null)
		{
			if(FluidRegistry.getFluid(getName()) == null)
			{
				fluid = new Fluid(getName()).setGaseous(true);
				FluidRegistry.registerFluid(fluid);
			}
			else
			{
				fluid = FluidRegistry.getFluid(getName());
			}
		}
		
		return this;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
