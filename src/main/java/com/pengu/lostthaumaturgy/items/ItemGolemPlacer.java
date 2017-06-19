package com.pengu.lostthaumaturgy.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.mrdimka.hammercore.common.items.MultiVariantItem;
import com.pengu.hammercore.utils.IGetter;
import com.pengu.hammercore.utils.IRegisterListener;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.entity.EntityGolemBase;
import com.pengu.lostthaumaturgy.init.ItemsLT;

public class ItemGolemPlacer extends MultiVariantItem implements IRegisterListener
{
	public ItemGolemPlacer()
	{
		super("golem", gen());
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if(player.isSneaking())
			return EnumActionResult.PASS;
		if(!world.isRemote)
		{
			boolean created = spawnCreature(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, side, player.getHeldItem(hand), player);
			if(created)
				player.getHeldItem(hand).shrink(1);
			return created ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		}
		player.swingArm(hand);
		return EnumActionResult.PASS;
	}
	
	public static String[] gen()
	{
		String[] names = new String[EnumGolemType.values().length];
		for(int i = 0; i < names.length; ++i)
			names[i] = EnumGolemType.values()[i].mod + ":" + EnumGolemType.values()[i];
		return names;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		int dmg = stack.getItemDamage();
		return EnumGolemType.values()[dmg % EnumGolemType.values().length].hasEffect;
	}
	
	@Override
	public void onRegistered()
	{
		for(EnumGolemType type : EnumGolemType.values())
			if(type.oredict != null)
				for(String name : type.oredict)
					if(!name.isEmpty())
						OreDictionary.registerOre(name, type.stack());
	}
	
	public boolean spawnCreature(World world, double x, double y, double z, EnumFacing side, ItemStack stack, EntityPlayer player)
	{
//		EntityGolemBase golem = new EntityGolemBase(world);
//		golem.setPositionAndUpdate(x + side.getFrontOffsetX(), y + side.getFrontOffsetY(), z + side.getFrontOffsetZ());
//		golem.setHomePosAndDistance(new BlockPos(x, y, z), 32);
//		golem.homeFacing = side;
//		EnumGolemType type = EnumGolemType.values()[stack.getItemDamage()];
//		golem.setTexture(LTInfo.MOD_ID + ":textures/models/golem_" + type.name().substring(0, type.name().indexOf("_")).toLowerCase() + ".png");
//		golem.setOwner(player.getGameProfile().getName());
//		if(!world.isRemote)
//			world.spawnEntity(golem);
//		return true;
		return false;
	}
	
	public enum EnumGolemType implements IGetter<ItemStack>
	{
		STRAW_GOLEM, //
		WOOD_GOLEM, //
		FLESH_GOLEM, //
		CLAY_GOLEM, //
		TALLOW_GOLEM, //
		STONE_GOLEM, //
		IRON_GOLEM, //
		THAUMIUM_GOLEM, //
		VOID_GOLEM;
		
		private final String oredict[];
		public final String mod;
		public final CreativeTabs tab;
		public boolean hasEffect = false;
		
		EnumGolemType()
		{
			this.oredict = null;
			this.tab = LostThaumaturgy.tab;
			this.mod = LTInfo.MOD_ID;
		}
		
		EnumGolemType(String... oredict)
		{
			this.oredict = oredict;
			this.tab = LostThaumaturgy.tab;
			this.mod = LTInfo.MOD_ID;
		}
		
		EnumGolemType(String mod, CreativeTabs tab, String... oredict)
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
			return new ItemStack(ItemsLT.GOLEM_PLACER, count, getDamage());
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
	}
}