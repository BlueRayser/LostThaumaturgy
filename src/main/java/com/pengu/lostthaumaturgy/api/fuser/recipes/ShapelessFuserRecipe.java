package com.pengu.lostthaumaturgy.api.fuser.recipes;

import java.util.ArrayList;
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
import com.pengu.lostthaumaturgy.custom.research.Research;
import com.pengu.lostthaumaturgy.custom.research.ResearchSystem;
import com.pengu.lostthaumaturgy.items.ItemResearch;
import com.pengu.lostthaumaturgy.items.ItemResearch.EnumResearchItemType;
import com.pengu.lostthaumaturgy.items.ItemWand;

public class ShapelessFuserRecipe implements IFuserRecipe
{
	@Nonnull
	protected ItemStack output = ItemStack.EMPTY;
	protected NonNullList<Object> input = NonNullList.create();
	public float visUsage = 0, taintUsage = 0;
	public Research research;
	
	public ShapelessFuserRecipe(Block result, Object... recipe)
	{
		this(new ItemStack(result), recipe);
	}
	
	public ShapelessFuserRecipe(Item result, Object... recipe)
	{
		this(new ItemStack(result), recipe);
	}
	
	public ShapelessFuserRecipe setVisUsage(float vis, float taint)
	{
		visUsage = vis;
		taintUsage = taint;
		return this;
	}
	
	public ShapelessFuserRecipe setResearch(Research research)
	{
		this.research = research;
		return this;
	}
	
	public ShapelessFuserRecipe(@Nonnull ItemStack result, Object... recipe)
	{
		output = result.copy();
		for(Object in : recipe)
		{
			if(in instanceof ItemStack)
			{
				input.add(((ItemStack) in).copy());
			} else if(in instanceof Item)
			{
				input.add(new ItemStack((Item) in));
			} else if(in instanceof Block)
			{
				input.add(new ItemStack((Block) in));
			} else if(in instanceof String)
			{
				input.add(OreDictionary.getOres((String) in));
			} else if(in instanceof IGetter && ((IGetter) in).get() instanceof ItemStack)
			{
				input.add(((IGetter<ItemStack>) in).get());
			} else
			{
				String ret = "Invalid shapeless fuser recipe: ";
				for(Object tmp : recipe)
				{
					ret += tmp + ", ";
				}
				ret += output;
				throw new RuntimeException(ret);
			}
		}
	}
	
	/**
	 * Returns the input for this recipe, any mod accessing this value should
	 * never manipulate the values in this array as it will effect the recipe
	 * itself.
	 * 
	 * @return The recipes input values.
	 */
	public NonNullList<Object> getInput()
	{
		return this.input;
	}
	
	@Override
	public boolean matches(FuserInventory inv, EntityPlayer player)
	{
		if(research != null && !ResearchSystem.isResearchCompleted(player, research))
			return false;
		
		NonNullList<Object> required = NonNullList.create();
		required.addAll(input);
		
		for(int x = 0; x < inv.craftingInv.getSizeInventory(); x++)
		{
			ItemStack slot = inv.craftingInv.getStackInSlot(x);
			
			if(!slot.isEmpty())
			{
				boolean inRecipe = false;
				Iterator<Object> req = required.iterator();
				
				while(req.hasNext())
				{
					boolean match = false;
					
					Object next = req.next();
					
					if(next instanceof ItemStack)
					{
						match = OreDictionary.itemMatches((ItemStack) next, slot, false);
					} else if(next instanceof List)
					{
						Iterator<ItemStack> itr = ((List<ItemStack>) next).iterator();
						while(itr.hasNext() && !match)
							match = OreDictionary.itemMatches(itr.next(), slot, false);
					}
					
					if(match)
					{
						inRecipe = true;
						required.remove(next);
						break;
					}
				}
				
				if(!inRecipe)
					return false;
			}
		}
		
		return required.isEmpty();
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
		for(int i = 0; i < input.size(); ++i)
		{
			List<ItemStack> s = new ArrayList<ItemStack>();
			Object item = input.get(i);
			if(item instanceof ItemStack)
				s.add((ItemStack) item);
			if(item instanceof List)
				s.addAll((List<ItemStack>) item);
			stacks.set(i, s);
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