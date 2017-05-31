package com.pengu.lostthaumaturgy.items;

import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.items.ITooltipInjector;
import com.mrdimka.hammercore.common.items.MultiVariantItem;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.custom.research.Research;
import com.pengu.lostthaumaturgy.custom.research.ResearchRegistry;
import com.pengu.lostthaumaturgy.custom.research.ResearchSystem;
import com.pengu.lostthaumaturgy.init.ItemsLT;

public class ItemResearch extends MultiVariantItem implements ITooltipInjector
{
	public ItemResearch()
	{
		super("discovery", "fragment", "theory", "discovery");
		insertPrefix(LTInfo.MOD_ID + ":");
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> l)
	{
		ResearchRegistry.addAllResearchItems(EnumResearchItemType.DISCOVERY, l);
		ResearchRegistry.addAllResearchItems(EnumResearchItemType.THEORY, l);
		ResearchRegistry.addAllResearchItems(EnumResearchItemType.FRAGMENT, l);
	}
	
	@Override
	public Item setCreativeTab(CreativeTabs tab)
	{
		return super.setCreativeTab(LostThaumaturgy.tab_researches);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		Research r = getFromStack(playerIn.getHeldItem(handIn));
		if(!r.isCompleted(playerIn) && getType(playerIn.getHeldItem(handIn)) == EnumResearchItemType.DISCOVERY)
		{
			if(!worldIn.isRemote)
			{
				ResearchSystem.setResearchCompleted(playerIn, r, true);
				HammerCore.audioProxy.playSoundAt(worldIn, LTInfo.MOD_ID + ":discover", playerIn.getPosition(), .5F, .8F + playerIn.getRNG().nextFloat() * .4F, SoundCategory.PLAYERS);
				if(!playerIn.capabilities.isCreativeMode)
					playerIn.getHeldItem(handIn).shrink(1);
			}
		}
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		Research res = getFromStack(stack);
		return super.getItemStackDisplayName(stack) + (res != null ? ": " + res.getTitle() : "");
	}
	
	@Override
	public void injectVariables(ItemStack stack, Map<String, String> vars)
	{
		Research r = getFromStack(stack);
		vars.put("difficulty", r != null ? r.sucessToString() : "Unknown");
	}
	
	public static Research getFromStack(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt != null && nbt.hasKey("Research"))
			return ResearchRegistry.getById(nbt.getString("Research"));
		return null;
	}
	
	public static ItemStack create(Research res, EnumResearchItemType type)
	{
		ItemStack stack = new ItemStack(ItemsLT.DISCOVERY);
		if(res != null)
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("Research", res.uid);
		}
		stack.setItemDamage(type.ordinal());
		return stack;
	}
	
	public static EnumResearchItemType getType(ItemStack stack)
	{
		return EnumResearchItemType.values()[stack.getItemDamage() % EnumResearchItemType.values().length];
	}
	
	public enum EnumResearchItemType
	{
		FRAGMENT, THEORY, DISCOVERY;
		
		public EnumResearchItemType progress()
		{
			if(ordinal() == values().length - 1)
				return this;
			return values()[ordinal() % values().length];
		}
	}
}