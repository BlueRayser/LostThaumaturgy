package com.pengu.lostthaumaturgy.items;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.api.RecipesCrucible;
import com.pengu.lostthaumaturgy.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.init.ItemMaterialsLT;
import com.pengu.lostthaumaturgy.net.PacketParticle;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXGuideWisp;

public class ItemElementalPickaxe extends ItemPickaxe
{
	public ItemElementalPickaxe()
	{
		super(ItemMaterialsLT.tool_elemental);
		setUnlocalizedName("elemental_pickaxe");
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if(!player.world.isRemote)
			HammerCore.audioProxy.playSoundAt(player.world, "item.flintandsteel.use", entity.getPosition(), 1F, player.getRNG().nextFloat() * .4F + .8F, SoundCategory.PLAYERS);
		entity.setFire(3);
		return false;
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)
	{
		if(!player.world.isRemote)
		{
			
		}
		return false;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if(!world.isRemote)
		{
			player.getHeldItem(hand).damageItem(8, player);
			int x = (int) player.posX;
			int y = (int) player.posY;
			int z = (int) player.posZ;
			Vec3d vec3d = player.getLook(5F);
			boolean found = false;
			float fval = 0.0f;
			ArrayList<BlockPos> points = new ArrayList<BlockPos>();
			HammerCore.audioProxy.playSoundAt(world, "item.flintandsteel.use", player.getPosition(), 1F, world.rand.nextFloat() * .4F + .8F, SoundCategory.PLAYERS);
			for(int a = x - 8; a <= x + 8; ++a)
			{
				for(int b = y - 8; b <= y + 8; ++b)
				{
					for(int c = z - 8; c <= z + 8; ++c)
					{
						if(b < 0 || b >= world.getHeight())
							continue;
						BlockPos pos = new BlockPos(a, b, c);
						IBlockState state = world.getBlockState(pos);
						if(Item.getItemFromBlock(state.getBlock()) == null)
							continue;
						float value = RecipesCrucible.getSmeltingValue(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)));
						if(value == 0 && state.getBlock() instanceof BlockOreCrystal)
							value = 50;
						if(value > 1F && value == fval)
						{
							found = true;
							fval = value;
							points.add(pos);
							continue;
						}
						if(value <= 1F || value <= fval)
							continue;
						points.clear();
						found = true;
						fval = value;
						points.add(pos);
					}
				}
			}
			if(found)
			{
				double fx = 0;
				double fy = 0;
				double fz = 0;
				double ldist = Double.MAX_VALUE;
				for(BlockPos v : points)
				{
					double dist = player.getDistance(v.getX() + .5, v.getY() + .5, v.getZ() + .5);
					if(dist >= ldist)
						continue;
					ldist = dist;
					fx = v.getX() + .5;
					fy = v.getY() + .5;
					fz = v.getZ() + .5;
				}
				HCNetwork.manager.sendToAllAround(new PacketFXGuideWisp(player.posX + vec3d.xCoord * 0.30000001192092896, player.posY + vec3d.yCoord * 0.30000001192092896, player.posZ + vec3d.zCoord * 0.30000001192092896, fx, fy, fz, .8F, 0), new TargetPoint(world.provider.getDimension(), player.posX, player.posY, player.posZ, 48));
			}
		}
		
		return super.onItemRightClick(world, player, hand);
	}
	
	public static void poofSmelt(World worldObj, float x, float y, float z)
	{
		for(int a = 0; a < 6; ++a)
		{
			TargetPoint tp = new TargetPoint(worldObj.provider.getDimension(), x, y, z, 48);
			HCNetwork.manager.sendToAllAround(new PacketParticle(worldObj, EnumParticleTypes.EXPLOSION_NORMAL, new Vec3d(x + worldObj.rand.nextFloat(), y + worldObj.rand.nextFloat(), z + worldObj.rand.nextFloat()), new Vec3d(0, 0, 0)), tp);
			HCNetwork.manager.sendToAllAround(new PacketParticle(worldObj, EnumParticleTypes.FLAME, new Vec3d(x + worldObj.rand.nextFloat(), y + worldObj.rand.nextFloat(), z + worldObj.rand.nextFloat()), new Vec3d(0, 0, 0)), tp);
		}
	}
}