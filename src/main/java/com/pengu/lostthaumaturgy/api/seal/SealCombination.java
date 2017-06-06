package com.pengu.lostthaumaturgy.api.seal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.lostthaumaturgy.tile.TileSeal;

public class SealCombination
{
	public final ItemSealSymbol[] slots = new ItemSealSymbol[3];
	public final String name;
	
	public SealCombination(ItemSealSymbol i, ItemSealSymbol j, ItemSealSymbol k, String name)
    {
		this.name = name;
		slots[0] = i;
		slots[1] = j;
		slots[2] = k;
    }
	
	/**
	 * Used to invoke a static method via reflection.
	 * <br>Format: com.package.RenderClass.methodName
	 */
	public String getRender(TileSeal seal, int index)
	{
		return "com.pengu.lostthaumaturgy.client.render.seal.LTSealRenders.renderNone";
	}
	
	public void update(TileSeal seal)
	{
		
	}
	
	public boolean onSealActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return false;
	}
	
	public boolean isValid(TileSeal seal)
	{
		if(slots[0] != seal.getSymbol(0))
			return false;
		if(slots[1] != seal.getSymbol(1))
			return false;
		if(slots[2] != seal.getSymbol(2))
			return false;
		return true;
	}
}