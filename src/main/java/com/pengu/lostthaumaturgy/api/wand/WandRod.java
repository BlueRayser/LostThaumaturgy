package com.pengu.lostthaumaturgy.api.wand;

import java.awt.Color;
import java.util.Random;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.math.MathHelper;
import com.mrdimka.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.items.ItemWand;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp2;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp3;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class WandRod
{
	public final String id;
	protected ItemStack rodItem = ItemStack.EMPTY;
	
	public WandRod(String id)
	{
		this.id = id;
	}
	
	public float getBaseCost()
	{
		return 10;
	}
	
	public float getRodCapacity()
	{
		return 10F;
	}
	
	public String getRodTexture()
	{
		return "minecraft:missing";
	}
	
	public int getColorMultiplierARGB(ItemStack wand)
	{
		return 0xFFFFFFFF;
	}
	
	public boolean isValid(ItemStack stack)
	{
		return rodItem.isItemEqual(stack);
	}
	
	public void onUpdate(ItemStack wand)
	{
		
	}
	
	public void onEntityItemUpdate(EntityItem wand)
	{
		wand.age = 0;
		if(!wand.world.isRemote && wand.ticksExisted % 4 == 0)
		{
			boolean flag1 = false;
			boolean flag = false;
			
			float vis = ItemWand.getVis(wand.getEntityItem());
			float taint = ItemWand.getTaint(wand.getEntityItem());
			SIAuraChunk si = AuraTicker.getAuraChunkFromBlockCoords(wand.getEntityWorld(), wand.getPosition());
			if(vis > taint && si.taint > 0 && taint < ItemWand.getMaxTaint(wand.getEntityItem()))
			{
				si.taint -= Math.ceil(ItemWand.addTaint(wand.getEntityItem(), .1F) * 10);
				flag = true;
				flag1 = true;
			} else if(si.vis > 0 && vis < ItemWand.getMaxVis(wand.getEntityItem()))
			{
				si.vis -= Math.ceil(ItemWand.addVis(wand.getEntityItem(), .1F) * 10);
				flag = true;
			}
			
			int color = 0xC548C9;
			
			Random rand = wand.world.rand;
			
			if(flag && rand.nextBoolean())
			{
				int r = (color >> 16) & 0xFF;
				int g = (color >> 8) & 0xFF;
				int b = (color >> 0) & 0xFF;
				
				r = (int) MathHelper.clip(r + rand.nextInt(25) - rand.nextInt(25), 0, 255);
				g = (int) MathHelper.clip(g + rand.nextInt(25) - rand.nextInt(25), 0, 255);
				b = (int) MathHelper.clip(b + rand.nextInt(25) - rand.nextInt(25), 0, 255);
				
				if(flag1)
				{
					r = (int) ((wand.world.rand.nextFloat() * .1F) * 255);
					g = (int) ((wand.world.rand.nextFloat() * .1F) * 255);
					b = (int) ((wand.world.rand.nextFloat() * .1F) * 255);
				}
				
				color = (r << 16) | (g << 8) | b;
				
				Vec3d pos = new Vec3d(wand.posX + (rand.nextGaussian() - rand.nextGaussian()) * 3D, wand.posY + (rand.nextGaussian() - rand.nextGaussian()) * 3D, wand.posZ + (rand.nextGaussian() - rand.nextGaussian()) * 3D);
				Vec3d tpos = wand.getPositionVector();
				
				if(!flag1)
					HCNetwork.manager.sendToAllAround(new PacketFXWisp3(pos.xCoord, pos.yCoord, pos.zCoord, tpos.xCoord, tpos.yCoord, tpos.zCoord, .9F + rand.nextFloat() * .6F, 2, color), new TargetPoint(wand.world.provider.getDimension(), wand.posX, wand.posY, wand.posZ, 48));
				else
					HCNetwork.manager.sendToAllAround(new PacketFXWisp2(pos.xCoord, pos.yCoord, pos.zCoord, tpos.xCoord, tpos.yCoord, tpos.zCoord, .9F + rand.nextFloat() * .6F, 5), new TargetPoint(wand.world.provider.getDimension(), wand.posX, wand.posY, wand.posZ, 48));
				
				if(wand.world.rand.nextInt(30) == 0)
					HammerCore.particleProxy.spawnZap(wand.world, pos, tpos, new Color(color));
			}
		}
	}
}