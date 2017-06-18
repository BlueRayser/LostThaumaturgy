package com.pengu.lostthaumaturgy.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.proxy.ParticleProxy_Client;
import com.pengu.hammercore.color.Color;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.client.fx.FXWisp;

public class BlockNitor extends BlockRendered
{
	public BlockNitor()
	{
		super(Material.ROCK);
		setSoundType(SoundType.CLOTH);
		setUnlocalizedName("nitor");
		setHarvestLevel(null, 0);
		setHardness(0);
		lightValue = 15;
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
	}
	
	public static final List<Integer> PARTICLE_COLORS = new ArrayList<>();
	
	static
	{
		PARTICLE_COLORS.addAll(Arrays.asList(0xE36F08, 0xEE8208, 0xFFB708, 0xFFDB12, 0xFF8E13, 0xFFFE88, 0xDF6F08, 0xE19B0D));
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		double x1 = pos.getX() + .5 + (rand.nextDouble() - rand.nextDouble()) * .125;
		double y1 = pos.getY() + .5 + (rand.nextDouble() - rand.nextDouble()) * .14;
		double z1 = pos.getZ() + .5 + (rand.nextDouble() - rand.nextDouble()) * .125;
		
		double x2 = pos.getX() + .5 + (rand.nextDouble() - rand.nextDouble()) * .5;
		double y2 = pos.getY() + .5 + rand.nextDouble() * .5;
		double z2 = pos.getZ() + .5 + (rand.nextDouble() - rand.nextDouble()) * .5;
		
		FXWisp wisp = new FXWisp(worldIn, x1, y1, z1, x2, y2, z2, 1F, 5);
		wisp.tinkle = false;
		
		int rgb = PARTICLE_COLORS.get(rand.nextInt(PARTICLE_COLORS.size()));
		
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = (rgb >> 0) & 0xFF;
		
		r = (int) (((r / 255F) * 1.2F) * 255F);
		g = (int) (((g / 255F) * 1.2F) * 255F);
		b = (int) (((b / 255F) * 1.2F) * 255F);
		
		r = Math.min(255, r);
		g = Math.min(255, g);
		b = Math.min(255, b);
		
		wisp.setColor(Color.packARGB(r, g, b, 255));
		ParticleProxy_Client.queueParticleSpawn(wisp);
	}
	
	private static final AxisAlignedBB NITOR_AABB = new AxisAlignedBB(6 / 16D, 6 / 16D, 6 / 16D, 10 / 16D, 10 / 16D, 10 / 16D);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return NITOR_AABB;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_)
	{
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		return NITOR_AABB.offset(pos);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return false;
	}
	
	@Override
	public String getParticleSprite(World world, BlockPos pos)
	{
		return LTInfo.MOD_ID + ":blocks/nitor";
	}
}