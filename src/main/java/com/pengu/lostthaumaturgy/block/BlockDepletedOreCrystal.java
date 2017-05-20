package com.pengu.lostthaumaturgy.block;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.client.fx.FXWisp;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;
import com.pengu.lostthaumaturgy.proxy.ClientProxy;
import com.pengu.lostthaumaturgy.tile.TileCrystalOre;

public class BlockDepletedOreCrystal extends BlockOreCrystal
{
	public BlockDepletedOreCrystal()
	{
		super(EnumMultiMaterialType.DEPLETED_CRYSTAL, "depleted", true, 0xAAAAAA);
		generatesInWorld = false;
		setLightLevel(0);
	}
	
	@Override
	public int tickRate(World worldIn)
	{
		return 200;
	}
	
	@Override
	public void randomTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		if(world.isRemote)
			return;
		
		TileCrystalOre ore = WorldUtil.cast(world.getTileEntity(pos), TileCrystalOre.class);
		SIAuraChunk ac = AuraTicker.getAuraChunkFromBlockCoords(world, pos);
		
		short q2 = ore.crystals.get();
		
		if(q2 < 8 && random.nextInt(q2 * 25) == 0)
		{
			ore.crystals.set((short) (q2 + 1));
			world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 3);
		}
		
		for(int i = 0; i < q2; ++i) if(ac != null)
		{
			if(ac.vis > ac.taint)
			{
				ac.vis--;
				ac.taint += 2;
			} else if(ac.vis < ac.taint)
			{
				ac.vis++;
				ac.taint -= 2;
			}
			
			if(ac.goodVibes > ac.badVibes)
			{
				ac.goodVibes--;
				ac.badVibes += 2;
			} else if(ac.goodVibes < ac.badVibes)
			{
				ac.goodVibes++;
				ac.badVibes -= 2;
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
		
		if(rand.nextBoolean())
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
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		if(placer instanceof EntityPlayer)
		{
			TileCrystalOre tile = WorldUtil.cast(worldIn.getTileEntity(pos), TileCrystalOre.class);
			if(tile == null)
			{
				tile = new TileCrystalOre();
				worldIn.setTileEntity(pos, tile);
			}
			
			tile.crystals.set((short) 4);
		}
	}
}