package com.pengu.lostthaumaturgy.core.block.world;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;

import com.pengu.lostthaumaturgy.core.block.BlockPlant;
import com.pengu.lostthaumaturgy.core.items.ItemMultiMaterial.EnumMultiMaterialType;
import com.pengu.lostthaumaturgy.init.ItemsLT;

public class BlockCinderpearl extends BlockPlant
{
	public BlockCinderpearl()
	{
		super("cinderpearl", new AxisAlignedBB(.1, 0, .1, .9, .8, .9));
		setLightLevel(2.8F);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return ItemsLT.MULTI_MATERIAL;
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return EnumMultiMaterialType.CINDERPEARL_POD.getDamage();
	}
	
	@Override
	protected boolean canSustainBush(IBlockState state)
	{
		return state.getBlock() == Blocks.SAND;
	}
}