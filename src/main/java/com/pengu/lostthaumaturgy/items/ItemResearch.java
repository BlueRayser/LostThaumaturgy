package com.pengu.lostthaumaturgy.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.items.ITooltipInjector;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.api.items.INotCloneable;
import com.pengu.lostthaumaturgy.api.research.ResearchCategories;
import com.pengu.lostthaumaturgy.api.research.ResearchItem;
import com.pengu.lostthaumaturgy.api.research.ResearchManager;
import com.pengu.lostthaumaturgy.api.research.ResearchSystem;
import com.pengu.lostthaumaturgy.init.ItemsLT;

public class ItemResearch extends Item implements ITooltipInjector, INotCloneable
{
	public final String[] names = { "fragment", "theory", "discovery" };
	
	public ItemResearch()
	{
		setUnlocalizedName("discovery");
		addPropertyOverride(new ResourceLocation("type"), (stack, world, entity) -> stack.getItemDamage());
		setHasSubtypes(true);
		setMaxStackSize(1);
		setMaxDamage(names.length);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return "item." + LTInfo.MOD_ID + ":" + (stack.getItemDamage() < 0 || stack.getItemDamage() >= names.length ? "unnamed" : names[stack.getItemDamage()]);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public boolean isRepairable()
	{
		return false;
	}
	
	@Override
	public boolean isDamageable()
	{
		return false;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> l)
	{
		if(isInCreativeTab(tab))
		{
			Collection<ResearchItem> items = new ArrayList<ResearchItem>();
			ResearchCategories.researchCategories.values().forEach(cl -> items.addAll(cl.research.values()));
			
			items.forEach(r ->
			{
				l.add(create(r, EnumResearchItemType.DISCOVERY));
				l.add(create(r, EnumResearchItemType.FRAGMENT));
				l.add(create(r, EnumResearchItemType.THEORY));
			});
		}
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		EnumResearchItemType type = getType(stack);
		return type == EnumResearchItemType.FRAGMENT ? EnumRarity.UNCOMMON : type == EnumResearchItemType.THEORY ? EnumRarity.RARE : EnumRarity.EPIC;
	}
	
	@Override
	public Item setCreativeTab(CreativeTabs tab)
	{
		return super.setCreativeTab(LostThaumaturgy.tab_researches);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		try
		{
			ResearchItem r = getFromStack(playerIn.getHeldItem(handIn));
			if(!worldIn.isRemote && r != null && playerIn != null && !ResearchManager.isResearchComplete(playerIn.getName(), r.key) && getType(playerIn.getHeldItem(handIn)) == EnumResearchItemType.DISCOVERY)
			{
				ResearchSystem.setResearchCompleted(playerIn, r, true);
				HammerCore.audioProxy.playSoundAt(worldIn, LTInfo.MOD_ID + ":discover", playerIn.getPosition(), .5F, .8F + playerIn.getRNG().nextFloat() * .4F, SoundCategory.PLAYERS);
				if(!playerIn.capabilities.isCreativeMode)
					playerIn.getHeldItem(handIn).shrink(1);
			}
		} catch(Throwable err)
		{ /* fuck */
		}
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		ResearchItem r = getFromStack(stack);
		return super.getItemStackDisplayName(stack) + (r != null ? ": " + r.getName() : "");
	}
	
	@Override
	public void injectVariables(ItemStack stack, Map<String, String> vars)
	{
		ResearchItem r = getFromStack(stack);
		vars.put("difficulty", r != null ? r.getComplexityLabel() : "Unknown");
	}
	
	public static ResearchItem getFromStack(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt != null && nbt.hasKey("Research"))
			return ResearchManager.getById(nbt.getString("Research"));
		return null;
	}
	
	public static ItemStack create(ResearchItem res, EnumResearchItemType type)
	{
		ItemStack stack = new ItemStack(ItemsLT.DISCOVERY);
		if(res != null)
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("Research", res.key);
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