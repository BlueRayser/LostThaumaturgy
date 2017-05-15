package com.pengu.lostthaumaturgy.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.api.ITileBlock;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.LTConfigs;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.client.fx.FXWisp;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileCrystalOre;

public class BlockOreCrystal extends BlockRendered implements ITileBlock<TileCrystalOre>, ITileEntityProvider
{
	protected IGetter<ItemStack> crystal;
	protected boolean goodVibesOnGrowth = false;
	protected int crystalColor = 0;
	
	public static interface IGetter<T>
	{
		T get();
	}
	
	public static class Getter<T> implements IGetter<T>
	{
		public T inst;
		
		public Getter(T i)
        {
			inst = i;
        }
		
		@Override
		public T get()
		{
		    return inst;
		}
	}
	
	/** Used to register and perform rendering. DO NOT MODIFY THIS SET! */
	public static final Set<BlockOreCrystal> crystals = new HashSet<>();
	
	public BlockOreCrystal(IGetter<ItemStack> crystalStack, String crystalName, boolean goodVibesOnGrowth, int crystalColor)
	{
		super(Material.GLASS);
		crystals.add(this);
		this.crystalColor = crystalColor;
		this.goodVibesOnGrowth = goodVibesOnGrowth;
		this.crystal = crystalStack;
		setSoundType(SoundType.GLASS);
		setHarvestLevel("pickaxe", 1);
		setUnlocalizedName("crystal_ore_" + crystalName);
		setTickRandomly(true);
		setLightLevel(2.5F);
		setHardness(.75F);
	}
	
	public ItemStack getCrystal()
	{
		return crystal.get();
	}
	
	public boolean areVibesGood()
	{
		return goodVibesOnGrowth;
	}
	
	public int getCrystalColor()
	{
		return crystalColor;
	}
	
	@Override
	public TileCrystalOre createNewTileEntity(World worldIn, int meta)
	{
		return new TileCrystalOre();
	}
	
	@Override
	public Class<TileCrystalOre> getTileClass()
	{
		return TileCrystalOre.class;
	}
	
	@Override
	public int tickRate(World worldIn)
	{
		return 400;
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
	{
		if(!super.canPlaceBlockOnSide(worldIn, pos, side))
			return false;
		if(!isSafe(worldIn, pos, side.getOpposite()))
			return false;
		return true;
	}
	
	public boolean isSafe(World world, BlockPos pos, EnumFacing facing)
	{
		BlockPos tpos = pos.offset(facing);
		if(!world.isBlockLoaded(tpos))
			return true;
		return world.getBlockState(tpos).isSideSolid(world, tpos, facing);
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		TileCrystalOre ore = WorldUtil.cast(world.getTileEntity(pos), TileCrystalOre.class);
		if(ore == null)
			return;
		EnumFacing orientation = EnumFacing.VALUES[ore.orientation % EnumFacing.VALUES.length].getOpposite();
		if(!isSafe(ore.getWorld(), pos, orientation))
			ore.getWorld().destroyBlock(pos, true);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		onNeighborChange(worldIn, pos, fromPos);
	}
	
	@Override
	public void randomTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		if(world.isRemote)
			return;
		TileCrystalOre ore = WorldUtil.cast(world.getTileEntity(pos), TileCrystalOre.class);
		SIAuraChunk ac = AuraTicker.getAuraChunkFromBlockCoords(world, pos);
		if(ac != null && ore != null)
		{
			short q2 = ore.crystals;
			if(!goodVibesOnGrowth)
			{
				if(q2 < 5 && ac.badVibes > 0 && random.nextInt(q2 * 75) == 0)
				{
					ore.crystals++;
				} else if(q2 < 3 && random.nextInt(q2 * 150) == 0)
				{
					ore.crystals++;
				}
				if(ac.taint < LTConfigs.auraMax / 10)
				{
					ac.badVibes += random.nextInt(q2);
				}
				ore.sync();
				world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 3);
			} else
			{
				if(q2 < 5 && ac.goodVibes > 0 && random.nextInt(q2 * 75) == 0)
				{
					ore.crystals++;
				} else if(q2 < 3 && random.nextInt(q2 * 150) == 0)
				{
					ore.crystals++;
				}
				if(ac.vis < LTConfigs.auraMax / 10)
				{
					ac.goodVibes += random.nextInt(q2);
				}
				ore.sync();
				world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 3);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(rand.nextInt(9) != 0)
			return;
		
		double x1 = pos.getX() + .5 + (rand.nextDouble() - rand.nextDouble()) * .3;
		double y1 = pos.getY() + .5 + (rand.nextDouble() - rand.nextDouble()) * .3;
		double z1 = pos.getZ() + .5 + (rand.nextDouble() - rand.nextDouble()) * .3;
		
		double x2 = pos.getX() + .5 + (rand.nextDouble() - rand.nextDouble()) * 2;
		double y2 = pos.getY() + .5 + (rand.nextDouble() - rand.nextDouble()) * 2;
		double z2 = pos.getZ() + .5 + (rand.nextDouble() - rand.nextDouble()) * 2;
		
		if(!goodVibesOnGrowth)
		{
			x2 = pos.getX() + .5 + (rand.nextDouble() - rand.nextDouble()) * .3;
			y2 = pos.getY() + .5 + (rand.nextDouble() - rand.nextDouble()) * .3;
			z2 = pos.getZ() + .5 + (rand.nextDouble() - rand.nextDouble()) * .3;
			x1 = pos.getX() + .5 + (rand.nextDouble() - rand.nextDouble()) * 5;
			y1 = pos.getY() + .5 + (rand.nextDouble() - rand.nextDouble()) * 5;
			z1 = pos.getZ() + .5 + (rand.nextDouble() - rand.nextDouble()) * 5;
		}
		
		FXWisp wisp = new FXWisp(worldIn, x1, y1, z1, x2, y2, z2, .5F, 5);
		wisp.tinkle = true;
		wisp.setColor(getCrystalColor());
		ClientProxy.queueParticle(wisp);
	}
	
	private final HashMap<String, Short> crystalAmts = new HashMap<>();
	private final HashMap<String, Short> placementMap = new HashMap<>();
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		short crystals = 0;
		
		TileCrystalOre ore = WorldUtil.cast(world.getTileEntity(pos), TileCrystalOre.class);
		if(ore != null)
			crystals = ore.crystals;
		else if(crystalAmts.containsKey(pos.toString()))
			crystals = crystalAmts.get(pos.toString());
		
		crystalAmts.remove(pos.toString());
		
		List<ItemStack> ret = new ArrayList<ItemStack>();
		
		if(crystals > 0)
		{
			ItemStack drop = getCrystal();
			drop.setCount(drop.getCount() * crystals);
			ret.add(drop);
		}
		
		return ret;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileCrystalOre ore = WorldUtil.cast(worldIn.getTileEntity(pos), TileCrystalOre.class);
		if(ore != null)
			crystalAmts.put(pos.toString(), ore.crystals);
		super.breakBlock(worldIn, pos, state);
	}
	
	private static final AxisAlignedBB[] aabbs = { new AxisAlignedBB(.25, .25, .25, .75, 1, .75), new AxisAlignedBB(.25, 0, .25, .75, .75, .75), new AxisAlignedBB(.25, .25, .25, .75, .75, 1), new AxisAlignedBB(.25, .25, 0, .75, .75, .75), new AxisAlignedBB(.25, .25, .25, 1, .75, .75), new AxisAlignedBB(0, .25, .25, .75, .75, .75) };
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		if(facing == null)
			return getDefaultState();
		
		placementMap.put(pos.toString(), (short) facing.ordinal());
		
		return getDefaultState();
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if(placer instanceof EntityPlayer)
		{
			TileCrystalOre tile = WorldUtil.cast(worldIn.getTileEntity(pos), TileCrystalOre.class);
			if(tile == null)
			{
				tile = new TileCrystalOre();
				worldIn.setTileEntity(pos, tile);
			}
			
			Short s = placementMap.remove(pos.toString());
			if(s != null)
				tile.orientation = s;
			else
				tile.orientation = (short) EnumFacing.getDirectionFromEntityLiving(pos, placer).ordinal();
			
			if(tile != null)
				tile.sync();
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		TileCrystalOre ore = WorldUtil.cast(source.getTileEntity(pos), TileCrystalOre.class);
		if(ore != null)
			return aabbs[ore.orientation % aabbs.length];
		return super.getBoundingBox(state, source, pos);
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
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
	{
		return true;
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
		return LTInfo.MOD_ID + ":blocks/crystal";
	}
}