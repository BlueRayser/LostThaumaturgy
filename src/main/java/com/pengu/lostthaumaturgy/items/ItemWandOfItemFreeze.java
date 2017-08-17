package com.pengu.lostthaumaturgy.items;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.IUpdatable;
import com.pengu.hammercore.raytracer.RayTracer;
import com.pengu.lostthaumaturgy.block.BlockLyingItem;
import com.pengu.lostthaumaturgy.tile.TileLyingItem;

public class ItemWandOfItemFreeze extends Item
{
	public ItemWandOfItemFreeze()
	{
		setUnlocalizedName("wand_item_freeze");
		setMaxStackSize(1);
		setMaxDamage(250);
	}
	
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		float damage = Math.max(0F, (float) (stack.getMaxDamage() - stack.getItemDamage()) / stack.getMaxDamage());
		int color = 0x0043FF;
		
		int target = (int) (damage * 255F);
		target = (target << 16) | (target << 8) | target;
		
		return MathHelper.multiplyColor(target, color);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		RayTraceResult rtl = RayTracer.retrace(playerIn);
		Vec3d point = RayTracer.getEndVec(playerIn);
		if(rtl != null && rtl.sideHit != null)
			point = new Vec3d(rtl.getBlockPos().offset(rtl.sideHit));
		
		Vec3d min = point.addVector(-1.5, -1.5, -1.5);
		Vec3d max = point.subtract(-1, -1, -1);
		List<EntityItem> items = worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(min.x, min.y, min.z, max.x, max.y, max.z));
		if(items.size() > 0)
		{
			if(!worldIn.isRemote)
			{
				EntityItem item = items.get(0);
				UpdatableProcess e = new UpdatableProcess();
				e.item = item;
				HammerCore.updatables.add(e);
			}
			
			playerIn.getHeldItem(handIn).damageItem(20, playerIn);
			playerIn.swingArm(handIn);
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
	}
	
	private static class UpdatableProcess implements IUpdatable
	{
		public int ticksExisted = 0;
		public EntityItem item;
		
		@Override
		public void update()
		{
			ticksExisted++;
			
			item.setPickupDelay(20 + 20 * ticksExisted);
			item.setNoDespawn();
			item.hoverStart += (float) Math.sqrt(ticksExisted);
			
			if(ticksExisted == 20 && item.world.isAirBlock(item.getPosition()))
			{
				TileLyingItem li = BlockLyingItem.place(item.getEntityWorld(), item.getPosition(), item.getItem().copy());
				if(li != null)
					li.placedByPlayer.set(true);
				item.setDead();
			}
		}
		
		@Override
		public boolean isAlive()
		{
			return item != null && !item.isDead && ticksExisted < 20;
		}
	}
}