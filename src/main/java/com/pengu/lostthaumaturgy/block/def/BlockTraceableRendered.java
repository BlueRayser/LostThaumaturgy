package com.pengu.lostthaumaturgy.block.def;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.api.mhb.BlockTraceable;
import com.pengu.lostthaumaturgy.client.fx.FXTextureAtlasSpriteDigging;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;

public abstract class BlockTraceableRendered extends BlockTraceable
{
	public BlockTraceableRendered(Material material)
	{
		super(material);
	}
	
	public BlockTraceableRendered(Material material, MapColor color)
	{
		super(material, color);
	}
	
	public abstract String getParticleSprite(World world, BlockPos pos);
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
	{
		IBlockState state = world.getBlockState(pos).getActualState(world, pos);
		int i = 4;
		
		for(int j = 0; j < 4; ++j)
		{
			for(int k = 0; k < 4; ++k)
			{
				for(int l = 0; l < 4; ++l)
				{
					double d0 = ((double) j + 0.5D) / 4.0D;
					double d1 = ((double) k + 0.5D) / 4.0D;
					double d2 = ((double) l + 0.5D) / 4.0D;
					Minecraft.getMinecraft().effectRenderer.addEffect(new FXTextureAtlasSpriteDigging(world, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, d0 - .5, d1 - .5, d2 - .5, state, ClientProxy.getSprite(getParticleSprite(world, pos))).setBlockPos(pos));
				}
			}
		}
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager)
	{
		BlockPos pos = target.getBlockPos();
		EnumFacing side = target.sideHit;
		
		if(state.getRenderType() != EnumBlockRenderType.INVISIBLE)
		{
			int i = pos.getX();
			int j = pos.getY();
			int k = pos.getZ();
			float f = 0.1F;
			AxisAlignedBB axisalignedbb = state.getBoundingBox(world, pos);
			double d0 = (double) i + world.rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minX;
			double d1 = (double) j + world.rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minY;
			double d2 = (double) k + world.rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minZ;
			
			double consta = 0.10000000149011612D;
			if(side == EnumFacing.DOWN)
				d1 = (double) j + axisalignedbb.minY - consta;
			if(side == EnumFacing.UP)
				d1 = (double) j + axisalignedbb.maxY + consta;
			if(side == EnumFacing.NORTH)
				d2 = (double) k + axisalignedbb.minZ - consta;
			if(side == EnumFacing.SOUTH)
				d2 = (double) k + axisalignedbb.maxZ + consta;
			if(side == EnumFacing.WEST)
				d0 = (double) i + axisalignedbb.minX - consta;
			if(side == EnumFacing.EAST)
				d0 = (double) i + axisalignedbb.maxX + consta;
			Minecraft.getMinecraft().effectRenderer.addEffect((new FXTextureAtlasSpriteDigging(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, state, ClientProxy.getSprite(getParticleSprite(world, pos)))).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
		}
		
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addLandingEffects(IBlockState state, WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles)
	{
		return true;
	}
}