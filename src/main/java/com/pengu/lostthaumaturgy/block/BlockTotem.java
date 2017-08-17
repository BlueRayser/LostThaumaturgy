package com.pengu.lostthaumaturgy.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.pengu.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
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
		AtmosphereChunk si = AuraTicker.getAuraChunkFromBlockCoords(worldIn, pos);
		
		if(si != null)
		{
			if(good)
			{
				if(si.badVibes > 0)
					si.badVibes--;
				else
					si.goodVibes++;
				si.vis += random.nextInt(4);
				HCNetwork.manager.sendToAllAround(new PacketFXWisp2(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, .8F, 6), new TargetPoint(worldIn.provider.getDimension(), pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, 48));
			} else
			{
				if(si.goodVibes > 0)
					si.goodVibes--;
				else
					si.badVibes++;
				si.taint += random.nextInt(4);
			}
			
			for(int x = -1; x < 2; ++x)
				for(int y = -1; y < 2; ++y)
					for(int z = -1; z < 2; ++z)
					{
						int sq = (int) new Vec3d(x, y, z).squareDistanceTo(0, 0, 0);
						if(sq >= 1 && random.nextInt(sq + 1) == 0)
						{
							if(good)
								AuraTicker.decreaseTaintedPlants(worldIn, x + pos.getX(), y + pos.getY(), z + pos.getZ());
							else
								AuraTicker.increaseTaintedPlants(worldIn, x + pos.getX(), y + pos.getY(), z + pos.getZ());
						}
					}
		}
	}
}