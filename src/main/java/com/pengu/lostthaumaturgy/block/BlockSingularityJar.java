package com.pengu.lostthaumaturgy.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.mrdimka.hammercore.api.ITileBlock;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.def.BlockRendered;
import com.pengu.lostthaumaturgy.tile.TileSingularityJar;

public class BlockSingularityJar extends BlockRendered implements ITileEntityProvider, ITileBlock<TileSingularityJar>
{
	public BlockSingularityJar()
	{
		super(Material.GLASS);
		setUnlocalizedName("jar_singularity");
		setHardness(1.2F);
		setSoundType(SoundType.WOOD);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileSingularityJar jar = WorldUtil.cast(worldIn.getTileEntity(pos), TileSingularityJar.class);
		if(jar != null && !worldIn.isRemote && jar.storedXP.get() > 0)
		{
			int stored = jar.storedXP.get();
			int retrieve = (int) Math.ceil(Math.sqrt(stored));
			if(stored > 64 && !playerIn.isSneaking())
				retrieve = (int) Math.ceil(Math.cbrt(stored));
			jar.storedXP.set(stored - retrieve);
			
			EntityXPOrb orb = new EntityXPOrb(worldIn, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, retrieve);
			
			double mod = .10000000149011612;
			orb.motionX = (playerIn.posX - orb.posX) * mod;
			orb.motionY = (playerIn.posY - orb.posY) * mod;
			orb.motionZ = (playerIn.posZ - orb.posZ) * mod;
			
			jar.absorbCooldown.set(20);
			worldIn.spawnEntity(orb);
		}
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileSingularityJar jar = WorldUtil.cast(worldIn.getTileEntity(pos), TileSingularityJar.class);
		if(jar != null && jar.storedXP.get() > 0)
		{
			if(!worldIn.isRemote)
				worldIn.spawnEntity(new EntityXPOrb(worldIn, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, jar.storedXP.get()));
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if(entityIn instanceof EntityXPOrb)
		{
			EntityXPOrb orb = (EntityXPOrb) entityIn;
			TileSingularityJar jar = WorldUtil.cast(worldIn.getTileEntity(pos), TileSingularityJar.class);
			if(jar != null && jar.absorbCooldown.get() <= 0)
			{
				jar.storedXP.set(jar.storedXP.get() + orb.getXpValue());
				orb.setDead();
			}
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileSingularityJar();
	}
	
	@Override
	public String getParticleSprite(World world, BlockPos pos)
	{
		return LTInfo.MOD_ID + ":blocks/warded_glass";
	}
	
	@Override
	public Class<TileSingularityJar> getTileClass()
	{
		return TileSingularityJar.class;
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
	
	public static final AxisAlignedBB JAR_AABB = new AxisAlignedBB(1 / 8D, 0, 1 / 8D, 7 / 8D, 1, 7 / 8D);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return JAR_AABB;
	}
}