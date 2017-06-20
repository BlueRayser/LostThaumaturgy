package com.pengu.lostthaumaturgy.items.tools.swords;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.net.HCNetwork;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.init.ItemMaterialsLT;
import com.pengu.lostthaumaturgy.init.SoundEventsLT;

public class ItemSwordElemental extends ItemSword
{
	public Color zapColor = new Color(0x6D00F1);
	
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
	
	private void hitEntity(Vec3d zapStart, Entity ent, List<Entity> hit, int maxHit, EntityPlayer player, Color color)
	{
		if(hit.size() < maxHit)
		{
			Vec3d pos = ent.getPositionVector().addVector(0, ent.height, 0);
			
			if(ent instanceof EntityLivingBase && ((EntityLivingBase) ent).attackable() && ((EntityLivingBase) ent).hurtTime <= 0)
			{
				HammerCore.particleProxy.spawnZap(player.world, zapStart, pos, color);
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
		if(!player.isSneaking())
		{
			player.getHeldItem(hand).damageItem(1, player);
			Vec3d target = player.getLookVec().add(player.getPositionVector());
			
			player.motionX = (player.posX - target.x) * 2F;
			player.motionY = (player.posY - target.y) * 2F;
			player.motionZ = (player.posY - target.y) * 2F;
			
			SoundEventsLT.SWING.playAt(new WorldLocation(world, player.getPosition()), 1F, 1F, SoundCategory.PLAYERS);
		}
		return super.onItemRightClick(world, player, hand);
	}
}