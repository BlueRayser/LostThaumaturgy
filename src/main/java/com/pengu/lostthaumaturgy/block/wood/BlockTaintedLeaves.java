package com.pengu.lostthaumaturgy.block.wood;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;

public class BlockTaintedLeaves extends BlockLeaves
{
	public BlockTaintedLeaves()
	{
		setUnlocalizedName("tainted_leaves");
		Blocks.FIRE.setFireInfo(this, 30, 60);
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(this);
	}
	
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
	}
	
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(DECAYABLE, (meta & 2) == 0).withProperty(CHECK_DECAY, (meta & 4) > 0);
	}
	
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		if(!state.getValue(DECAYABLE).booleanValue())
			i |= 2;
		if(state.getValue(CHECK_DECAY).booleanValue())
			i |= 4;
		return i;
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		return random.nextInt(40) == 0 ? 1 : 0;
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		return Arrays.asList(new ItemStack(this));
	}
	
	@Override
	public EnumType getWoodType(int meta)
	{
		return EnumType.OAK;
	}
	
	@Override
	protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance)
	{
		if(RANDOM.nextInt(8) == 0)
			spawnAsEntity(worldIn, pos, EnumMultiMaterialType.TAINTED_FRUIT.stack());
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return ItemsLT.MULTI_MATERIAL;
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return EnumMultiMaterialType.TAINTED_BRANCH.getDamage();
	}
}