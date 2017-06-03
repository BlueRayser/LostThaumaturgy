package com.pengu.lostthaumaturgy.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.mrdimka.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp2;

public class BlockTotem extends Block
{
	public final boolean good;
	
	public BlockTotem(boolean good)
	{
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		this.good = good;
		setUnlocalizedName("totem_" + (good ? "dawn" : "twilight"));
		setTickRandomly(true);
		setHardness(1.5F);
		setResistance(3F);
		setHarvestLevel("axe", -1);
	}
	
	@Override
	public int tickRate(World worldIn)
	{
		return 100;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		SIAuraChunk si = AuraTicker.getAuraChunkFromBlockCoords(worldIn, pos);
		
		if(si != null)
		{
			if(good)
			{
				if(si.badVibes > 0)
					si.badVibes--;
				else
					si.goodVibes++;
				HCNetwork.manager.sendToAllAround(new PacketFXWisp2(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, .8F, 6), new TargetPoint(worldIn.provider.getDimension(), pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, 48));
			} else
			{
				if(si.goodVibes > 0)
					si.goodVibes--;
				else
					si.badVibes++;
			}
		}
	}
}