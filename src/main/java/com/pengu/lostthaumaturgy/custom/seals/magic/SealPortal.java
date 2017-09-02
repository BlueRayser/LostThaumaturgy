package com.pengu.lostthaumaturgy.custom.seals.magic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.lostthaumaturgy.api.seal.ItemSealSymbol;
import com.pengu.lostthaumaturgy.api.seal.SealCombination;
import com.pengu.lostthaumaturgy.api.seal.SealInstance;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.tile.TileSeal;

public class SealPortal extends SealInstance
{
	public static final Map<ItemSealSymbol, Map<Integer, List<Long>>> NETWORKS = new HashMap<>();
	
	public static List<Long> getNetwork(ItemSealSymbol item, World world)
	{
		Map<Integer, List<Long>> nets = NETWORKS.get(item);
		if(nets == null)
			NETWORKS.put(item, nets = new HashMap<>());
		List<Long> parts = nets.get(world.provider.getDimension());
		if(parts == null)
			nets.put(world.provider.getDimension(), parts = new ArrayList<>());
		return parts;
	}
	
	public int cooldown;
	
	public SealPortal(TileSeal seal)
	{
		super(seal);
	}
	
	@Override
	public void tick()
	{
		if(cooldown > 0)
			--cooldown;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Cooldown", cooldown);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		cooldown = nbt.getInteger("Cooldown");
	}
	
	@Override
	public void onEntityCollidedWithSeal(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if(entityIn.getEntityBoundingBox().intersects(state.getBoundingBox(worldIn, pos).grow(.4)) && cooldown <= 0)
		{
			
			
			cooldown += 40;
		}
	}
	
	public static class PortalSealCombination extends SealCombination
	{
		public PortalSealCombination()
		{
			super(null, null, null);
		}
		
		@Override
		public String getRender(TileSeal seal, int index)
		{
			return super.getRender(seal, index);
		}
		
		@Override
		public boolean isValid(TileSeal seal)
		{
			return seal.getSymbol(0) == ItemsLT.RUNICESSENCE_MAGIC && seal.getSymbol(1) == ItemsLT.RUNICESSENCE_AIR;
		}
	}
}