package com.pengu.lostthaumaturgy.block;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;

import com.pengu.hammercore.api.ITileBlock;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.LTConfigs;
import com.pengu.lostthaumaturgy.api.blocks.ITaintedBlock;
import com.pengu.lostthaumaturgy.api.event.TaintedSoilEvent;
import com.pengu.lostthaumaturgy.api.event.TaintedSoilEvent.GetDrops;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereTicker;
import com.pengu.lostthaumaturgy.custom.aura.taint.TaintRegistry;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.tile.TileTaintedSoil;

public class BlockTaintedSoil extends Block implements ITileEntityProvider, ITileBlock<TileTaintedSoil>, ITaintedBlock
{
	public BlockTaintedSoil()
	{
		super(Material.GROUND);
		setUnlocalizedName("tainted_soil");
		setHardness(5F);
		setResistance(100F);
		setTickRandomly(true);
	}
	
	public static void placeSoil(World world, BlockPos pos)
	{
		BlockSnapshot snapshot = new BlockSnapshot(world, pos, world.getBlockState(pos));
		TileTaintedSoil soil = new TileTaintedSoil();
		world.setBlockState(pos, BlocksLT.TAINTED_SOIL.getDefaultState());
		world.setTileEntity(pos, soil);
		soil.setSnapshot(snapshot);
		
		if(world.rand.nextInt(25) == 0 && world.isAirBlock(pos.up()))
			world.setBlockState(pos.up(), BlocksLT.TAINTED_PLANT.getDefaultState());
	}
	
	public static void cleanSoil(World world, BlockPos pos)
	{
		TileTaintedSoil soil = WorldUtil.cast(world.getTileEntity(pos), TileTaintedSoil.class);
		if(soil != null)
		{
			BlockSnapshot snapshot = soil.getSnapshot();
			if(snapshot != null && snapshot.restore(true))
			{
				AtmosphereChunk si = AtmosphereTicker.getAuraChunkFromBlockCoords(world, pos);
				si.taint += 3;
				si.badVibes++;
			}
			
			if(world.getBlockState(pos.up()).getBlock() == BlocksLT.TAINTED_PLANT)
			{
				world.setBlockToAir(pos.up());
				AtmosphereChunk si = AtmosphereTicker.getAuraChunkFromBlockCoords(world, pos);
				si.taint += 5 + world.rand.nextInt(20);
				si.badVibes += 4 + world.rand.nextInt(10);
			}
		}
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		if(worldIn.getBlockState(pos).getBlock() != this)
			return;
		
		if(random.nextInt(25) == 0 && worldIn.isAirBlock(pos.up()))
			worldIn.setBlockState(pos.up(), BlocksLT.TAINTED_PLANT.getDefaultState());
		
		IBlockState up = worldIn.getBlockState(pos.up());
		
		if(up.getBlock() == BlocksLT.TAINTED_PLANT && BlocksLT.TAINTED_PLANT.getMetaFromState(up) == 1)
		{
			worldIn.setBlockState(pos.up(), BlocksLT.TAINTED_PLANT.getDefaultState());
			AtmosphereChunk si = AtmosphereTicker.getAuraChunkFromBlockCoords(worldIn, pos);
			si.taint += 5 + random.nextInt(20);
			si.badVibes += 4 + random.nextInt(10);
		}
		
		AtmosphereChunk a = AtmosphereTicker.getAuraChunkFromBlockCoords(worldIn, pos);
		if(!a.isTainted() || random.nextInt(75) == 0)
		{
			cleanSoil(worldIn, pos);
			for(EnumFacing f : EnumFacing.VALUES)
			{
				BlockPos p = pos.offset(f);
				if(worldIn.getBlockState(p).getBlock() == this && random.nextInt(20) < 3)
				{
					randomTick(worldIn, p, state, random);
					cleanSoil(worldIn, p);
				}
			}
		}
		
		int c = a.isTainted() ? 2 : 5;
		if(random.nextInt(c) == 0)
			for(EnumFacing f : EnumFacing.VALUES)
			{
				BlockPos p = pos.offset(f);
				if(worldIn.isBlockLoaded(p) && random.nextBoolean())
					if(TaintRegistry.taintBlock(worldIn, p) && random.nextBoolean())
						randomTick(worldIn, p, worldIn.getBlockState(p), random);
			}
	}
	
	public static boolean isTaintable(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		Block b = state.getBlock();
		ResourceLocation reg = b.getRegistryName();
		
		if(LTConfigs.taint_taintableBlocks != null)
			for(String v : LTConfigs.taint_taintableBlocks)
			{
				ResourceLocation vr = new ResourceLocation(v);
				if(vr.equals(reg))
					return true;
			}
		
		return false;
	}
	
	@Override
	public Class<TileTaintedSoil> getTileClass()
	{
		return TileTaintedSoil.class;
	}
	
	@Override
	public TileTaintedSoil createNewTileEntity(World worldIn, int meta)
	{
		return new TileTaintedSoil();
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		WorldLocation loc = new WorldLocation(worldIn, pos);
		TileTaintedSoil tile = loc.getTileOfType(TileTaintedSoil.class);
		if(tile == null)
			worldIn.setTileEntity(pos, tile = createNewTileEntity(worldIn, loc.getMeta()));
		NBTTagCompound nbt = new NBTTagCompound();
		ResourceLocation stone = Blocks.STONE.getRegistryName();
		nbt.setString("blockMod", stone.getResourceDomain());
		nbt.setString("blockName", stone.getResourcePath());
		nbt.setInteger("posX", pos.getX());
		nbt.setInteger("posY", pos.getY());
		nbt.setInteger("posZ", pos.getZ());
		nbt.setInteger("flag", 3);
		nbt.setInteger("dimension", worldIn.provider.getDimension());
		nbt.setInteger("metadata", 0);
		nbt.setBoolean("hasTE", false);
		tile.BLOCK_SNAPSHOT.set(nbt);
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		if(plantable == BlocksLT.SHIMMERLEAF || plantable == BlocksLT.TAINTEDLEAF || plantable == BlocksLT.TAINTED_PLANT)
			return true;
		return false;
	}
	
	private ThreadLocal<TileTaintedSoil> tts = new ThreadLocal();
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		TileTaintedSoil soil = tts.get();
		tts.set(null);
		
		if(soil != null)
		{
			GetDrops e = new TaintedSoilEvent.GetDrops(soil.getWorld(), pos, soil);
			MinecraftForge.EVENT_BUS.post(e);
			return e.drops;
		}
		return Arrays.asList();
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		tts.set(WorldUtil.cast(worldIn.getTileEntity(pos), TileTaintedSoil.class));
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		TileTaintedSoil ttl = WorldUtil.cast(worldIn.getTileEntity(pos), TileTaintedSoil.class);
		if(ttl != null)
			try
			{
				return ttl.getSnapshot().getReplacedBlock().getBlockHardness(worldIn, pos) * 3F;
			} catch(Throwable er)
			{
			}
		return 2F;
	}
	
	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity)
	{
		TileTaintedSoil ttl = WorldUtil.cast(world.getTileEntity(pos), TileTaintedSoil.class);
		if(ttl != null)
			try
			{
				BlockSnapshot s = ttl.getSnapshot();
				return s.getReplacedBlock().getBlock().getSoundType(s.getCurrentBlock(), world, pos, entity);
			} catch(Throwable er)
			{
			}
		return super.getSoundType(state, world, pos, entity);
	}
}