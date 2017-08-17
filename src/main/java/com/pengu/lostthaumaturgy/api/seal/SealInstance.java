package com.pengu.lostthaumaturgy.api.seal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.lostthaumaturgy.tile.TileSeal;

public class SealInstance
{
	public final TileSeal seal;
	
	public SealInstance(TileSeal seal)
	{
		this.seal = seal;
	}
	
	public void tick()
	{
		
	}
	
	public boolean onSealActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return false;
	}
	
	public void onEntityCollidedWithSeal(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		
	}
	
	public String getUnlocalizedDescription()
	{
		return "lt_seal.unknown";
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		return nbt;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		
	}
}