package com.pengu.lostthaumaturgy.api.fuser.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.utils.IGetter;
import com.pengu.lostthaumaturgy.api.fuser.FuserInventory;
import com.pengu.lostthaumaturgy.api.fuser.IFuserRecipe;
import com.pengu.lostthaumaturgy.api.research.ResearchItem;
import com.pengu.lostthaumaturgy.api.research.ResearchSystem;
import com.pengu.lostthaumaturgy.core.items.ItemResearch;
import com.pengu.lostthaumaturgy.core.items.ItemWand;
import com.pengu.lostthaumaturgy.core.items.ItemResearch.EnumResearchItemType;

public class ShapedFuserRecipe implements IFuserRecipe
{
	@Nonnull
	protected ItemStack output = ItemStack.EMPTY;
	protected Object[] input = null;
	protected int width = 0;
	protected int height = 0;
	protected boolean mirrored = true;
	public float visUsage = 0, taintUsage = 0;
	public ResearchItem research;
	
	public ShapedFuserRecipe(Block result, Object... recipe)
	{
		this(new ItemStack(result), recipe);
	}
	
	public ShapedFuserRecipe(Item result, Object... recipe)
	{
		this(new ItemStack(result), recipe);
	}
	
	public ShapedFuserRecipe setVisUsage(float vis, float taint)
	{
		visUsage = vis;
		taintUsage = taint;
		return this;
	}
	
	public ShapedFuserRecipe setResearch(ResearchItem research)
	{
		this.research = research;
		return this;
	}
	
	public ShapedFuserRecipe(@Nonnull ItemStack result, Object... recipe)
	{
		output = result.copy();
		
		String shape = "";
		int idx = 0;
		
		if(recipe[idx] instanceof Boolean)
		{
			mirrored = (Boolean) recipe[idx];
			if(recipe[idx + 1] instanceof Object[])
			{
				recipe = (Object[]) recipe[idx + 1];
			} else
			{
				idx = 1;
			}
		}
		
		if(recipe[idx] instanceof String[])
		{
			String[] parts = ((String[]) recipe[idx++]);
			
			for(String s : parts)
			{
				width = s.length();
				shape += s;
			}
			
			height = parts.length;
		} else
		{
			while(recipe[idx] instanceof String)
			{
				String s = (String) recipe[idx++];
				shape += s;
				width = s.length();
				height++;
			}
		}
		
		if(width * height != shape.length())
		{
			String ret = "Invalid shaped ore recipe: ";
			for(Object tmp : recipe)
			{
				ret += tmp + ", ";
			}
			ret += output;
			throw new RuntimeException(ret);
		}
		
		HashMap<Character, Object> itemMap = new HashMap<Character, Object>();
		
		for(; idx < recipe.length; idx += 2)
		{
			Character chr = (Character) recipe[idx];
			Object in = recipe[idx + 1];
			
			if(in instanceof ItemStack)
			{
				itemMap.put(chr, ((ItemStack) in).copy());
			} else if(in instanceof Item)
			{
				itemMap.put(chr, new ItemStack((Item) in));
			} else if(in instanceof Block)
			{
				itemMap.put(chr, new ItemStack((Block) in, 1, OreDictionary.WILDCARD_VALUE));
			} else if(in instanceof String)
			{
				itemMap.put(chr, OreDictionary.getOres((String) in));
			} else if(in instanceof IGetter && ((IGetter) in).get() instanceof ItemStack)
			{
				itemMap.put(chr, ((IGetter<ItemStack>) in).get());
			} else
			{
				String ret = "Invalid shaped fuser recipe: ";
				for(Object tmp : recipe)
				{
					ret += tmp + ", ";
				}
				ret += output;
				throw new RuntimeException(ret);
			}
		}
		
		input = new Object[width * height];
		int x = 0;
		for(char chr : shape.toCharArray())
		{
			input[x++] = itemMap.get(chr);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected boolean checkMatch(FuserInventory inv, int startX, int startY, boolean mirror)
	{
		for(int x = 0; x < 3; x++)
		{
			for(int y = 0; y < 3; y++)
			{
				int subX = x - startX;
				int subY = y - startY;
				Object target = null;
				
				if(subX >= 0 && subY >= 0 && subX < width && subY < height)
				{
					if(mirror)
					{
						target = input[width - subX - 1 + subY * width];
					} else
					{
						target = input[subX + subY * width];
					}
				}
				
				ItemStack slot = inv.getStackInRowAndColumn(x, y);
				
				if(target instanceof ItemStack)
				{
					if(!OreDictionary.itemMatches((ItemStack) target, slot, false))
					{
						return false;
					}
				} else if(target instanceof List)
				{
					boolean matched = false;
					
					Iterator<ItemStack> itr = ((List<ItemStack>) target).iterator();
					while(itr.hasNext() && !matched)
					{
						matched = OreDictionary.itemMatches(itr.next(), slot, false);
					}
					
					if(!matched)
					{
						return false;
					}
				} else if(target == null && !slot.isEmpty())
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public ShapedFuserRecipe setMirrored(boolean mirror)
	{
		mirrored = mirror;
		return this;
	}
	
	/**
	 * Returns the input for this recipe, any mod accessing this value should
	 * never manipulate the values in this array as it will effect the recipe
	 * itself.
	 * 
	 * @return The recipes input values.
	 */
	public Object[] getInput()
	{
		return this.input;
	}
	
	@Override
	public boolean matches(FuserInventory inv, EntityPlayer player)
	{
		if(research != null && !ResearchSystem.isResearchCompleted(player, research))
			return false;
		for(int x = 0; x <= 3 - width; x++)
			for(int y = 0; y <= 3 - height; ++y)
			{
				if(checkMatch(inv, x, y, false))
					return true;
				
				if(mirrored && checkMatch(inv, x, y, true))
					return true;
			}
		return false;
	}
	
	@Override
	public ItemStack getCraftResult(FuserInventory inv, EntityPlayer player)
	{
		return output.copy();
	}
	
	@Override
	public void consumeWandVis(FuserInventory inv, EntityPlayer player, ItemStack wandStack)
	{
		if(visUsage > 0 || taintUsage > 0)
		{
			if(wandStack.isEmpty() || !(wandStack.getItem() instanceof ItemWand))
				return;
			float eff = ItemWand.getVisUsage(wandStack);
			if(visUsage > 0 && ItemWand.getVis(wandStack) < visUsage * eff)
				return;
			if(taintUsage > 0 && ItemWand.getTaint(wandStack) < taintUsage * eff)
				return;
			ItemWand.removeVis(wandStack, visUsage * eff);
			ItemWand.removeTaint(wandStack, taintUsage * eff);
		}
	}
	
	@Override
	public ItemStack getOutput()
	{
		return output.copy();
	}
	
	@SideOnly(Side.CLIENT)
	public ItemStack getJEIOutput()
	{
		return research == null || ResearchSystem.isResearchCompleted(HammerCore.renderProxy.getClientPlayer(), research) ? getOutput() : ItemResearch.create(research, EnumResearchItemType.FRAGMENT);
	}
	
	@Override
	public NonNullList<List<ItemStack>> bakeInputItems()
	{
		NonNullList<List<ItemStack>> stacks = NonNullList.<List<ItemStack>> withSize(9, new ArrayList<>());
		for(int x = 0; x < width; ++x)
			for(int y = 0; y < height; ++y)
			{
				List<ItemStack> s = new ArrayList<ItemStack>();
				Object item = input[x + y * width];
				if(item instanceof ItemStack)
					s.add((ItemStack) item);
				if(item instanceof List)
					s.addAll((List<ItemStack>) item);
				stacks.set(x + y * 3, s);
			}
		return stacks;
	}
	
	@Override
	public float getVisUsage()
	{
		return visUsage;
	}
	
	@Override
	public float getTaintUsage()
	{
		return taintUsage;
	}
}