package com.pengu.lostthaumaturgy.block.wood.greatwood;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.math.MathHelper;
import com.mrdimka.hammercore.proxy.ParticleProxy_Client;
import com.pengu.hammercore.color.Color;
import com.pengu.lostthaumaturgy.LTConfigs;
import com.pengu.lostthaumaturgy.block.BlockPlant;
import com.pengu.lostthaumaturgy.client.ClientSIAuraChunk;
import com.pengu.lostthaumaturgy.client.fx.FXWisp;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.worldgen.features.FeatureGreatwood;

public class BlockGreatwoodSapling extends BlockPlant
{
	public BlockGreatwoodSapling()
	{
		super("greatwood_sapling", new AxisAlignedBB(1 / 16D, 0, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D));
		setSoundType(SoundType.PLANT);
		setTickRandomly(true);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		worldIn.scheduleBlockUpdate(pos, state.getBlock(), tickRate(worldIn), 0);
	}
	
	@Override
	public int tickRate(World worldIn)
	{
		return 1000 + worldIn.rand.nextInt(2000);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		worldIn.setBlockToAir(pos);
		SIAuraChunk aura = AuraTicker.getAuraChunkFromBlockCoords(worldIn, pos);
		if(rand.nextInt(9) == 0 && aura != null && aura.vis >= LTConfigs.aura_max / 3 && !worldIn.isRemote && new FeatureGreatwood().generate(worldIn, rand, pos))
			aura.vis -= Math.sqrt(LTConfigs.aura_max / 3);
		else
		{
			worldIn.setBlockState(pos, state);
			worldIn.scheduleBlockUpdate(pos, state.getBlock(), tickRate(worldIn), 0);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		LTConfigs.updateAura();
		SIAuraChunk aura = ClientSIAuraChunk.getClientChunk();
		Chunk c = worldIn.getChunkFromBlockCoords(pos);
		if(aura != null && aura.x == c.x && aura.z == c.z && rand.nextInt(63) == 0)
		{
			FXWisp wisp = new FXWisp(Minecraft.getMinecraft().world, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), .4F, 5);
			if(aura.vis >= LTConfigs.sync_aura_max / 3)
			{
				int r = 222;
				int g = 61;
				int b = 255;
				
				r += rand.nextInt(25) - rand.nextInt(25);
				g += rand.nextInt(25) - rand.nextInt(25);
				b += rand.nextInt(25) - rand.nextInt(25);
				
				r = (int) MathHelper.clip(r, 0, 255);
				g = (int) MathHelper.clip(g, 0, 255);
				b = (int) MathHelper.clip(b, 0, 255);
				
				wisp.setColor(Color.packARGB(r, g, b, 255));
			}
			ParticleProxy_Client.queueParticleSpawn(wisp);
		}
	}
}