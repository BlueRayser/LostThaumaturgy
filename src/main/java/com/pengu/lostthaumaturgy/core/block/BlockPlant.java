package com.pengu.lostthaumaturgy.core.block;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import com.pengu.lostthaumaturgy.core.items.ItemMultiMaterial.EnumMultiMaterialType;

public class BlockPlant extends BlockBush
{
	private final AxisAlignedBB aabb;
	
	public BlockPlant(String name, AxisAlignedBB aabb)
	{
		this.aabb = aabb;
		setSoundType(SoundType.PLANT);
		setUnlocalizedName(name);
		setHarvestLevel(null, 0);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		if(this instanceof BlockTaintedPlant)
		{
			if(RANDOM.nextInt(100) < 40)
			{
				List<ItemStack> drops = NonNullList.<ItemStack> create();
				if(RANDOM.nextBoolean())
					drops.add(EnumMultiMaterialType.TAINT_SPORES.stack());
				else if(RANDOM.nextInt(16) == 0)
					drops.add(EnumMultiMaterialType.INTACT_TAINTSPORE_POD.stack());
				return drops;
			}
			return Arrays.asList();
		}
		return super.getDrops(world, pos, state, fortune);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return aabb;
	}
}