package com.pengu.lostthaumaturgy.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.items.MultiVariantItem;
import com.mrdimka.hammercore.common.utils.ChatUtil;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;

public class ItemAuraDetector extends MultiVariantItem
{
	public static int type = -1;
	
	public ItemAuraDetector()
	{
		super("aura_detector", "vis_detector", "taint_detector", "radiation_detector", "thaumometer");
		insertPrefix(LTInfo.MOD_ID + ":");
		setMaxStackSize(1);
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
				ChatUtil.sendNoSpam(player, new TextComponentTranslation("chat." + names[type], (Object[]) args));
				HammerCore.audioProxy.playSoundAt(worldIn, "block.note.pling", pos, .5F, 2F, SoundCategory.PLAYERS);
			}
		}
		return EnumActionResult.FAIL;
	}
}