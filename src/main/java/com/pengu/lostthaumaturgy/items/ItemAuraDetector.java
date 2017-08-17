package com.pengu.lostthaumaturgy.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.utils.ChatUtil;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;

public class ItemAuraDetector extends Item
{
	public static int type = -1;
	public final String[] names = { "vis_detector", "taint_detector", "radiation_detector", "thaumometer" };
	
	public ItemAuraDetector()
	{
		setUnlocalizedName("aura_detector");
		setHasSubtypes(true);
		setMaxStackSize(1);
		addPropertyOverride(new ResourceLocation("type"), (stack, world, entity) -> stack.getItemDamage());
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
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(isInCreativeTab(tab))
			for(int i = 0; i < names.length; ++i)
				items.add(new ItemStack(this, 1, i));
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.UNCOMMON;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(player.getHeldItem(hand).getItemDamage() == 2)
			return EnumActionResult.FAIL;
		
		IConnection conn = WorldUtil.cast(worldIn.getTileEntity(pos), IConnection.class);
		if(conn != null)
		{
			player.swingArm(hand);
			
			int type = player.getHeldItem(hand).getItemDamage();
			
			int suction = conn.getSuction(null);
			int v = Math.round(conn.getPureVis());
			int t = Math.round(conn.getTaintedVis());
			
			if(!worldIn.isRemote)
			{
				String[] args = new String[] { "" + (type == 0 ? v : t) };
				if(type == 3)
					args = new String[] { "" + t, "" + v };
				ChatUtil.sendNoSpam(player, new TextComponentTranslation("chat." + LTInfo.MOD_ID + ":" + names[type], (Object[]) args));
				HammerCore.audioProxy.playSoundAt(worldIn, "block.note.pling", pos, .5F, 2F, SoundCategory.PLAYERS);
			}
		}
		return EnumActionResult.FAIL;
	}
}