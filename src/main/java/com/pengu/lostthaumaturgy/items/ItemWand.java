package com.pengu.lostthaumaturgy.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.api.wand.EnumCapLocation;
import com.pengu.lostthaumaturgy.api.wand.WandCap;
import com.pengu.lostthaumaturgy.api.wand.WandRegistry;
import com.pengu.lostthaumaturgy.api.wand.WandRod;

public class ItemWand extends Item
{
	public static final ItemWand WAND = new ItemWand();
	
	private ItemWand()
	{
		setUnlocalizedName("wand");
		setMaxStackSize(1);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if(isInCreativeTab(tab))
			WandRegistry.addSubitems(subItems);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced)
	{
		String stored = "";
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("PureVis", NBT.TAG_FLOAT))
			stored += (stored.isEmpty() ? "" : " | ") + TextFormatting.LIGHT_PURPLE + "" + LostThaumaturgy.standartDecimalFormat.format(getVis(stack)) + " Vis" + TextFormatting.RESET;
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("TaintedVis", NBT.TAG_FLOAT))
			stored += (stored.isEmpty() ? "" : " | ") + TextFormatting.DARK_PURPLE + "" + LostThaumaturgy.standartDecimalFormat.format(getTaint(stack)) + " Taint" + TextFormatting.RESET;
		if(!stored.isEmpty())
			stored = "Stored: " + stored;
		
		String cap = "";
		if(stack.hasTagCompound())
			cap += (cap.isEmpty() ? "" : " | ") + TextFormatting.LIGHT_PURPLE + "" + LostThaumaturgy.standartDecimalFormat.format(getMaxVis(stack)) + " Vis" + TextFormatting.RESET;
		if(stack.hasTagCompound())
			cap += (cap.isEmpty() ? "" : " | ") + TextFormatting.DARK_PURPLE + "" + LostThaumaturgy.standartDecimalFormat.format(getMaxTaint(stack)) + " Taint" + TextFormatting.RESET;
		cap = "Capacity: " + cap;
		
		WandRod rod = getRod(stack);
		WandCap c1 = getCap(stack, EnumCapLocation.UP);
		WandCap c2 = getCap(stack, EnumCapLocation.DOWN);
		
		if(rod != null)
			tooltip.add("Rod: " + I18n.translateToLocal("wand.rod." + rod.id));
		if(c1 != null && c1 == c2)
			tooltip.add("Capping: " + I18n.translateToLocal("wand.cap." + c1.id));
		else
		{
			if(c1 != null)
				tooltip.add("Upper Cap: " + I18n.translateToLocal("wand.cap." + c1.id));
			if(c2 != null)
				tooltip.add("Lower Cap: " + I18n.translateToLocal("wand.cap." + c2.id));
		}
		
		if(!stored.isEmpty())
			tooltip.add(stored);
		tooltip.add(cap);
		tooltip.add("Avg Vis Cost: " + Math.round(getVisUsage(stack) * 100) + "%");
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		WandRod rod = getRod(stack);
		WandCap c1 = getCap(stack, EnumCapLocation.UP);
		WandCap c2 = getCap(stack, EnumCapLocation.DOWN);
		
		if(rod != null)
			rod.onUpdate(stack);
		if(c1 != null)
			c1.onUpdate(stack, EnumCapLocation.UP, c1 == c2);
		if(c2 != null)
			c2.onUpdate(stack, EnumCapLocation.DOWN, c1 == c2);
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		entityItem.setNoDespawn();
		
		ItemStack stack = entityItem.getItem();
		
		WandRod rod = getRod(stack);
		WandCap c1 = getCap(stack, EnumCapLocation.UP);
		WandCap c2 = getCap(stack, EnumCapLocation.DOWN);
		
		if(rod != null)
			rod.onEntityItemUpdate(entityItem);
		if(c1 != null)
			c1.onEntityItemUpdate(entityItem, EnumCapLocation.UP, c1 == c2);
		if(c2 != null)
			c2.onEntityItemUpdate(entityItem, EnumCapLocation.DOWN, c1 == c2);
		
		return false;
	}
	
	public static ItemStack makeWand(WandRod rod, WandCap up, WandCap down)
	{
		ItemStack stack = new ItemStack(WAND);
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setString("WandRod", rod.id);
		stack.getTagCompound().setString(EnumCapLocation.UP.nbtName(), up.id);
		stack.getTagCompound().setString(EnumCapLocation.DOWN.nbtName(), down.id);
		return stack;
	}
	
	public static WandCap getCap(ItemStack wand, EnumCapLocation loc)
	{
		if(wand.isEmpty() || !wand.hasTagCompound() || !wand.getTagCompound().hasKey(loc.nbtName(), NBT.TAG_STRING))
			return null;
		return WandRegistry.getCap(wand.getTagCompound().getString(loc.nbtName()));
	}
	
	public static WandRod getRod(ItemStack wand)
	{
		if(wand.isEmpty() || !wand.hasTagCompound() || !wand.getTagCompound().hasKey("WandRod", NBT.TAG_STRING))
			return null;
		return WandRegistry.getRod(wand.getTagCompound().getString("WandRod"));
	}
	
	public static float getVis(ItemStack wand)
	{
		if(!wand.isEmpty() && wand.hasTagCompound())
			return wand.getTagCompound().getFloat("PureVis");
		return 0F;
	}
	
	public static float getTaint(ItemStack wand)
	{
		if(!wand.isEmpty() && wand.hasTagCompound())
			return wand.getTagCompound().getFloat("TaintedVis");
		return 0F;
	}
	
	public static void setVis(ItemStack wand, float vis)
	{
		if(wand.isEmpty())
			return;
		if(!wand.hasTagCompound())
			wand.setTagCompound(new NBTTagCompound());
		wand.getTagCompound().setFloat("PureVis", vis);
	}
	
	public static void setTaint(ItemStack wand, float vis)
	{
		if(wand.isEmpty())
			return;
		if(!wand.hasTagCompound())
			wand.setTagCompound(new NBTTagCompound());
		wand.getTagCompound().setFloat("TaintedVis", vis);
	}
	
	/**
	 * Warning: it is a float from 0 to 1 (higher if usage bigger than normal)
	 */
	public static float getVisUsage(ItemStack wand)
	{
		WandRod rod = getRod(wand);
		
		float base = 100;
		
		if(rod != null)
			base = rod.getBaseCost();
		
		for(EnumCapLocation cl : EnumCapLocation.values())
		{
			WandCap c = getCap(wand, cl);
			if(c != null)
				base += c.getUseCost();
		}
		
		return Math.max(base / 100, .1F);
	}
	
	public static float getMaxVis(ItemStack wand)
	{
		WandRod rod = getRod(wand);
		if(rod == null)
			return 0;
		return rod.getRodCapacity();
	}
	
	public static float getMaxTaint(ItemStack wand)
	{
		float vis = getMaxVis(wand);
		float cost = getVisUsage(wand);
		return (vis / cost) / 2;
	}
	
	public static float addVis(ItemStack wand, float vis)
	{
		float v = getVis(wand);
		float maxAccept = Math.min(vis, getMaxVis(wand) - v);
		setVis(wand, v + maxAccept);
		return maxAccept;
	}
	
	public static float addTaint(ItemStack wand, float taint)
	{
		float t = getTaint(wand);
		float maxAccept = Math.min(taint, getMaxTaint(wand) - t);
		setTaint(wand, t + maxAccept);
		return maxAccept;
	}
	
	public static float removeVis(ItemStack wand, float vis)
	{
		float v = getVis(wand);
		float maxRemove = Math.min(vis, v);
		setVis(wand, v - maxRemove);
		return maxRemove;
	}
	
	public static float removeTaint(ItemStack wand, float taint)
	{
		float t = getTaint(wand);
		float maxRemove = Math.min(taint, t);
		setTaint(wand, t - maxRemove);
		return maxRemove;
	}
}