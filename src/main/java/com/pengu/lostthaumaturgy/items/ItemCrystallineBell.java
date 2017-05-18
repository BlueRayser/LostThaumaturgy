package com.pengu.lostthaumaturgy.items;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.lostthaumaturgy.api.items.IVisRepairable;
import com.pengu.lostthaumaturgy.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.tile.TileCrystalOre;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCrystallineBell extends Item implements IVisRepairable
{
	public ItemCrystallineBell()
	{
		setUnlocalizedName("crystalline_bell");
		setMaxStackSize(1);
		setMaxDamage(100);
		canRepair = false;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		return enchantment == Enchantments.MENDING || enchantment == Enchantments.UNBREAKING;
	}
	
	@Override
	public boolean isRepairable()
	{
		return false;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
	}

	@Override
    public float visRepairCost(ItemStack stack)
    {
	    return .5F;
    }
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileCrystalOre ore = WorldUtil.cast(worldIn.getTileEntity(pos), TileCrystalOre.class);
		BlockOreCrystal block = WorldUtil.cast(worldIn.getBlockState(pos).getBlock(), BlockOreCrystal.class);
		
		if(ore != null && block != null)
		{
			if(ore.crystals.get() == 1) return EnumActionResult.FAIL;
			
			if(!worldIn.isRemote) HammerCore.audioProxy.playSoundAt(worldIn, "entity.experience_orb.pickup", pos, .5F, .8f + ore.crystals.get() * .1F, SoundCategory.PLAYERS);
			ore.crystals.set((short) (ore.crystals.get() - 1));
			
			if(ore.crystals.get() == 0)
			{
				SIAuraChunk ac = AuraTicker.getAuraChunkFromBlockCoords(worldIn, pos);
				
				if(ac != null)
				{
					if(block.areVibesGood())
						ac.badVibes += 2;
					else
						ac.goodVibes += 2;
				}
			}
			
			EntityItem ent = new EntityItem(worldIn, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, block.getCrystal().copy());
			
			double mod = .10000000149011612;
			ent.motionX = (player.posX - ent.posX) * mod;
			ent.motionY = (player.posY - ent.posY) * mod;
			ent.motionZ = (player.posZ - ent.posZ) * mod;
			
			player.getHeldItem(hand).damageItem(1, player);
			
			if(!worldIn.isRemote) worldIn.spawnEntity(ent);
			
			return EnumActionResult.SUCCESS;
		}
		
	    return EnumActionResult.PASS;
	}
}