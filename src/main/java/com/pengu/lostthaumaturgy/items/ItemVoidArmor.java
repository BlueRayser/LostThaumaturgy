package com.pengu.lostthaumaturgy.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;

public class ItemVoidArmor extends ItemArmor
{
	public static final ArmorMaterial void_material = EnumHelper.addArmorMaterial(LTInfo.MOD_ID + ":void", LTInfo.MOD_ID + ":textures/armor/void_1.png", 15, new int[] { 4, 7, 8, 4 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
	
	public ItemVoidArmor(EntityEquipmentSlot equipmentSlotIn)
	{
		super(void_material, 2, equipmentSlotIn);
		if(equipmentSlotIn == EntityEquipmentSlot.HEAD)
			setUnlocalizedName("void_helmet");
		if(equipmentSlotIn == EntityEquipmentSlot.CHEST)
			setUnlocalizedName("void_chestplate");
		if(equipmentSlotIn == EntityEquipmentSlot.LEGS)
			setUnlocalizedName("void_leggings");
		if(equipmentSlotIn == EntityEquipmentSlot.FEET)
			setUnlocalizedName("void_boots");
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
	{
		SIAuraChunk si = AuraTicker.getAuraChunkFromBlockCoords(world, player.getPosition());
		if(itemStack.getItemDamage() > 0 && player.ticksExisted % 60 == 0)
		{
			itemStack.setItemDamage(itemStack.getItemDamage() - 1);
			++si.badVibes;
			--si.vis;
		}
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		return LTInfo.MOD_ID + ":textures/armor/void_" + (slot == EntityEquipmentSlot.LEGS ? 2 : 1) + ".png";
	}
}