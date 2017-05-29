package com.pengu.lostthaumaturgy.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import com.mrdimka.hammercore.common.items.MultiVariantItem;
import com.pengu.hammercore.utils.IGetter;
import com.pengu.hammercore.utils.IRegisterListener;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.init.ItemsLT;

public class ItemMultiMaterial extends MultiVariantItem implements IRegisterListener
{
	private static ItemMultiMaterial instance;
	
	public ItemMultiMaterial()
	{
		super("multi_material", gen());
		instance = this;
	}
	
	public static String[] gen()
	{
		String[] names = new String[EnumMultiMaterialType.values().length];
		for(int i = 0; i < names.length; ++i)
			names[i] = EnumMultiMaterialType.values()[i].mod + ":" + EnumMultiMaterialType.values()[i];
		return names;
	}
	
	@Override
	public void onRegistered()
	{
		EnumMultiMaterialType.VAPOROUS_CRYSTAL.setHasEffect(true);
		EnumMultiMaterialType.AQUEOUS_CRYSTAL.setHasEffect(true);
		EnumMultiMaterialType.EARTHEN_CRYSTAL.setHasEffect(true);
		EnumMultiMaterialType.FIERY_CRYSTAL.setHasEffect(true);
		EnumMultiMaterialType.VIS_CRYSTAL.setHasEffect(true);
		EnumMultiMaterialType.TAINTED_CRYSTAL.setHasEffect(true);
		
		EnumMultiMaterialType.ENCHANTED_WOOD.setHasEffect(true);
		EnumMultiMaterialType.ENCHANTED_SILVERWOOD.setHasEffect(true);
		EnumMultiMaterialType.ENCHANTED_FABRIC.setHasEffect(true);
		
		for(EnumMultiMaterialType type : EnumMultiMaterialType.values())
			if(type.oredict != null)
				for(String name : type.oredict)
					if(!name.isEmpty())
						OreDictionary.registerOre(name, type.stack());
		
		OreDictionary.registerOre("logWood", BlocksLT.SILVERWOOD_LOG);
		OreDictionary.registerOre("treeLeaves", BlocksLT.SILVERWOOD_LEAVES);
		OreDictionary.registerOre("plankWood", BlocksLT.SILVERWOOD_PLANKS);
		OreDictionary.registerOre("stairWood", BlocksLT.SILVERWOOD_STAIRS);
		OreDictionary.registerOre("oreQuicksilver", BlocksLT.CINNABAR_ORE);
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> l)
	{
		for(int i = 0; i < this.names.length; ++i)
			if(EnumMultiMaterialType.values()[i].tab == tab || tab == LostThaumaturgy.tab)
			{
				l.add(new ItemStack(item, 1, i));
				if(i == EnumMultiMaterialType.ALUMENTUM.ordinal())
					l.add(new ItemStack(BlocksLT.NITOR));
			}
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		int dmg = stack.getItemDamage();
		return EnumMultiMaterialType.values()[dmg % EnumMultiMaterialType.values().length].hasEffect;
	}
	
	public enum EnumMultiMaterialType implements IGetter<ItemStack>
	{
		VAPOROUS_CRYSTAL("crystalVis"), //
		AQUEOUS_CRYSTAL("crystalVis"), //
		EARTHEN_CRYSTAL("crystalVis"), //
		FIERY_CRYSTAL("crystalVis"), //
		VIS_CRYSTAL("crystalVis"), //
		TAINTED_CRYSTAL("crystalVis"), //
		DEPLETED_CRYSTAL("crystalDepleted"), //
		ENCHANTED_WOOD, //
		ENCHANTED_SILVERWOOD, //
		CINDERPEARL_POD, //
		ZOMBIE_BRAINS, //
		QUICKSILVER("gemQuicksilver"), //
		THAUMIUM_INGOT("ingotThaumium"), //
		ENCHANTED_FABRIC, //
		ANIMATED_PISTON, //
		ALUMENTUM, //
		ANCIENT_POTTERY, //
		TARNISHED_CHALICE, //
		WORN_STATUETTE, //
		ANCIENT_WEAPON, //
		ANCIENT_SEAL, //
		ANCIENT_STONE_TABLET, //
		TOPAZ("gemTopaz"), //
		EXTRACT_PUREST_MAGIC, //
		EXTRACT_FOULEST_TAINT, //
		EXTRACT_WARMEST_FIRE, //
		EXTRACT_DEEPEST_EARTH, //
		EXTRACT_LIGHTEST_AIR, //
		EXTRACT_COOLEST_WATER, //
		SOUL_FRAGMENT, //
		CONGEALED_TAINT;
		
		private final String oredict[];
		public final String mod;
		public final CreativeTabs tab;
		public boolean hasEffect = false;
		
		EnumMultiMaterialType()
		{
			this.oredict = null;
			this.tab = LostThaumaturgy.tab;
			this.mod = LTInfo.MOD_ID;
		}
		
		EnumMultiMaterialType(String... oredict)
		{
			this.oredict = oredict;
			this.tab = LostThaumaturgy.tab;
			this.mod = LTInfo.MOD_ID;
		}
		
		EnumMultiMaterialType(String mod, CreativeTabs tab, String... oredict)
		{
			this.oredict = oredict;
			this.mod = mod;
			this.tab = tab;
		}
		
		public void setHasEffect(boolean hasEffect)
		{
			this.hasEffect = hasEffect;
		}
		
		public boolean isEqualByOredict(ItemStack stack)
		{
			if(oredict == null || stack == null)
				return false;
			if(stack.isItemEqual(stack(stack.getCount())))
				return true;
			int[] ids = OreDictionary.getOreIDs(stack);
			for(int id : ids)
			{
				String dict = OreDictionary.getOreName(id);
				if(dict.equals(oredict))
					return true;
			}
			return false;
		}
		
		public int getDamage()
		{
			return ordinal();
		}
		
		public ItemStack stack()
		{
			return stack(1);
		}
		
		public ItemStack stack(int count)
		{
			return new ItemStack(instance, count, getDamage());
		}
		
		@Override
		public String toString()
		{
			return name().toLowerCase();
		}
		
		@Override
		public ItemStack get()
		{
			return stack();
		}
		
		public boolean isThisItem(ItemStack src)
		{
			return src.getItem() == ItemsLT.MULTI_MATERIAL && src.getItemDamage() == getDamage();
		}
		
		public static boolean isCrystal(ItemStack src)
		{
			return DEPLETED_CRYSTAL.isThisItem(src) || VAPOROUS_CRYSTAL.isThisItem(src) || AQUEOUS_CRYSTAL.isThisItem(src) || FIERY_CRYSTAL.isThisItem(src) || EARTHEN_CRYSTAL.isThisItem(src) || VIS_CRYSTAL.isThisItem(src) || TAINTED_CRYSTAL.isThisItem(src);
		}
	}
}