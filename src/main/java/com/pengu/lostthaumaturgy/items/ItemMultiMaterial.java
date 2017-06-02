package com.pengu.lostthaumaturgy.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.mrdimka.hammercore.common.items.MultiVariantItem;
import com.pengu.hammercore.utils.IGetter;
import com.pengu.hammercore.utils.IRegisterListener;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.entity.EntityTravelingTrunk;
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
		
		EnumMultiMaterialType.EXTRACT_COOLEST_WATER.setHasEffect(true);
		EnumMultiMaterialType.EXTRACT_DEEPEST_EARTH.setHasEffect(true);
		EnumMultiMaterialType.EXTRACT_FOULEST_TAINT.setHasEffect(true);
		EnumMultiMaterialType.EXTRACT_LIGHTEST_AIR.setHasEffect(true);
		EnumMultiMaterialType.EXTRACT_PUREST_MAGIC.setHasEffect(true);
		EnumMultiMaterialType.EXTRACT_WARMEST_FIRE.setHasEffect(true);
		
		EnumMultiMaterialType.REZULI_CRYSTAL.setHasEffect(true);
		
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
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack held = player.getHeldItem(hand);
		if(held.getItemDamage() == EnumMultiMaterialType.TRAVELING_TRUNK.getDamage())
		{
			BlockPos sp = pos.offset(facing);
			if(!worldIn.isRemote)
				worldIn.spawnEntity(new EntityTravelingTrunk(worldIn, player, sp.getX() + .5, sp.getY(), sp.getZ() + .5));
			held.shrink(1);
			player.swingArm(hand);
			return EnumActionResult.SUCCESS;
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
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
		CRACKED_WISP_SHELL, //
		DISTORTED_SKULL, //
		INHUMAN_SKULL, //
		DARKENED_CRYSTAL_EYE, //
		KNOTTED_SPIKE, //
		TOME_FORBIDDEN_KNOWLEDGE, //
		TAINT_SPORES, //
		TAINTED_ORGAN, //
		TAINTED_FRUIT, //
		TAINTED_BRANCH, //
		INTACT_TAINTSPORE_POD, //
		WRITHING_TAINT_TENDRILS, //
		SHARD_STRANGE_METAL, //
		ELDRITCH_MECHANISM, //
		OPALESCENT_EYE, //
		DISTURBING_MIRROR, //
		GLOWING_ELDRITCH_DEVICE, //
		ELDRITCH_REPOSITORY, //
		DARKNESS_SEED, //
		VOID_INGOT("ingotVoid"), //
		TOPAZ("gemLTTopaz"), //
		EXTRACT_PUREST_MAGIC, //
		EXTRACT_FOULEST_TAINT, //
		EXTRACT_WARMEST_FIRE, //
		EXTRACT_DEEPEST_EARTH, //
		EXTRACT_LIGHTEST_AIR, //
		EXTRACT_COOLEST_WATER, //
		SOUL_FRAGMENT, //
		CONGEALED_TAINT, //
		REZULI_CRYSTAL("gemRezuli"), //
		ELDRITCH_KEYSTONE_INERT, //
		ELDRITCH_KEYSTONE_TLHUTLH, //
		TRAVELING_TRUNK, //
		INERT_CARPET;
		
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
		
		public static boolean isNormalCrystal(ItemStack src)
		{
			return VAPOROUS_CRYSTAL.isThisItem(src) || AQUEOUS_CRYSTAL.isThisItem(src) || FIERY_CRYSTAL.isThisItem(src) || EARTHEN_CRYSTAL.isThisItem(src) || VIS_CRYSTAL.isThisItem(src) || TAINTED_CRYSTAL.isThisItem(src);
		}
	}
}