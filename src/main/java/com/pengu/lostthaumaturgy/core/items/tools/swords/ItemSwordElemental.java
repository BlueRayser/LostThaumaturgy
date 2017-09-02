package com.pengu.lostthaumaturgy.core.items.tools.swords;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.raytracer.RayTracer;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.init.ItemMaterialsLT;
import com.pengu.lostthaumaturgy.init.SoundEventsLT;

public class ItemSwordElemental extends ItemSword
{
	public int zapColor = 0x6D00F1;
	
	public ItemSwordElemental()
	{
		super(ItemMaterialsLT.tool_elemental);
		setUnlocalizedName("elemental_sword");
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if(!player.world.isRemote && !player.isSneaking())
		{
			List<Entity> damaged = new ArrayList<>();
			hitEntity(player.getPositionVector().addVector(0, player.height / 2, 0), entity, damaged, 5, player, zapColor);
			stack.damageItem(damaged.size(), player);
			if(!damaged.isEmpty())
				SoundEventsLT.ZAP.playAt(new WorldLocation(player.world, player.getPosition()), .2F, 1F, SoundCategory.PLAYERS);
			damaged.clear();
		}
		return false;
	}
	
	private void hitEntity(Vec3d zapStart, Entity ent, List<Entity> hit, int maxHit, EntityPlayer player, int color)
	{
		if(hit.size() < maxHit)
		{
			Vec3d pos = ent.getPositionVector().addVector(0, ent.height, 0);
			
			if(ent instanceof EntityLivingBase && ((EntityLivingBase) ent).attackable() && ((EntityLivingBase) ent).hurtTime <= 0)
			{
				HammerCore.particleProxy.spawnSlowZap(player.world, zapStart, pos, color, 10, .25F);
				ent.attackEntityFrom(DamageSource.causePlayerDamage(player), 6 + player.getRNG().nextFloat() * 2F);
				hit.add(ent);
			}
			
			/** Filters out all entities that do not match lambda predicate. */
			List<Entity> ents = ent.world.getEntitiesInAABBexcluding(ent, new AxisAlignedBB(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z).grow(5), t -> t != player && !hit.contains(t) && t instanceof EntityLivingBase && ((EntityLivingBase) t).canEntityBeSeen(ent) && player.getRNG().nextBoolean());
			
			for(Entity e : ents)
				hitEntity(pos, e, hit, maxHit - 1, player, color);
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		
		if(stack.hasTagCompound())
		{
			long used = stack.getTagCompound().getLong("LastUsed");
			if(world.getTotalWorldTime() > used && world.getTotalWorldTime() - 30L < used && !player.onGround)
				return super.onItemRightClick(world, player, hand);
		}
		
		{
			if(!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			
			Vec3d begin = RayTracer.getStartVec(player);
			Vec3d lookVec = player.getLook(1);
			Vec3d end = begin.addVector(lookVec.x * 32, lookVec.y * 32, lookVec.z * 32);
			List<Entity> tracedList = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(player.getPosition()).grow(32), t -> t != player && player.canEntityBeSeen(t) && t.canBeCollidedWith() && t.getEntityBoundingBox().calculateIntercept(begin, end) != null);
			Entity traced = null;
			for(int i = 0; i < tracedList.size(); ++i)
				if(traced == null || player.getDistanceSqToEntity(tracedList.get(i)) < player.getDistanceSqToEntity(traced))
					traced = tracedList.get(i);
			
			boolean flag = false;
			
			if(traced != null && player.isSneaking())
			{
				traced.motionX = ((player.posX - traced.posX) / 32D) * 10F;
				traced.motionY = ((player.posY - traced.posY) / 32D) * 10F;
				traced.motionZ = ((player.posZ - traced.posZ) / 32D) * 10F;
				flag = true;
			} else if(!player.isSneaking())
			{
				Vec3d target = player.getLookVec().add(player.getPositionVector());
				
				player.motionX = (target.x - player.posX) * 3F;
				player.motionY = (target.y - player.posY) * 2F;
				player.motionZ = (target.z - player.posZ) * 3F;
				flag = true;
			}
			
			if(flag)
			{
				player.getHeldItem(hand).damageItem(1, player);
				
				if(!world.isRemote)
					SoundEventsLT.SWING.playAt(new WorldLocation(world, player.getPosition()), 1F, 1F, SoundCategory.PLAYERS);
				
				stack.getTagCompound().setLong("LastUsed", world.getTotalWorldTime());
			}
		}
		
		return super.onItemRightClick(world, player, hand);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(isSelected)
			entityIn.fallDistance = 0;
	}
}