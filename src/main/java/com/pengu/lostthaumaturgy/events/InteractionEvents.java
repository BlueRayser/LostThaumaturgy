package com.pengu.lostthaumaturgy.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.tile.TileSyncable;
import com.pengu.hammercore.utils.AdvancementUtils;
import com.pengu.hammercore.utils.RoundRobinList;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.LTConfigs;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.event.TaintedSoilEvent;
import com.pengu.lostthaumaturgy.api.items.ISpeedBoots;
import com.pengu.lostthaumaturgy.api.tiles.IUpgradable;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereTicker;
import com.pengu.lostthaumaturgy.emote.EmoteManager;
import com.pengu.lostthaumaturgy.emote.EmoteManager.DefaultEmotes;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.items.tools.axe.ItemAxeElemental;
import com.pengu.lostthaumaturgy.tile.TileTaintedSoil;

@MCFBus
public class InteractionEvents
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void interactWithUpgradable(PlayerInteractEvent.RightClickBlock e)
	{
		World world = e.getWorld();
		BlockPos pos = e.getPos();
		ItemStack held = e.getItemStack();
		
		WorldLocation l = new WorldLocation(world, pos);
		
		if(!held.isEmpty())
		{
			ItemUpgrade item = WorldUtil.cast(held.getItem(), ItemUpgrade.class);
			IUpgradable tile = WorldUtil.cast(world.getTileEntity(pos), IUpgradable.class);
			
			if(item != null && tile != null)
			{
				if(!world.isRemote && tile.setUpgrade(ItemUpgrade.idFromItem(item)))
				{
					held.shrink(1);
					HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":upgrade", pos, 1F, .8F + world.rand.nextFloat() * .4F, SoundCategory.PLAYERS);
					TileSyncable t = WorldUtil.cast(tile, TileSyncable.class);
					if(t != null)
						t.sync();
					
					e.setUseBlock(Result.DENY);
					e.setUseItem(Result.DENY);
					e.setCanceled(true);
				}
				e.getEntityPlayer().swingArm(e.getHand());
			} else if(held.getItem() == ItemsLT.WAND_REVERSAL)
			{
				if(!world.isRemote && tile != null && tile.dropUpgrade(e.getEntityPlayer()))
				{
					held.damageItem(1, e.getEntityLiving());
					HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":zap", pos, 1F, .8F + world.rand.nextFloat() * .4F, SoundCategory.PLAYERS);
					
					TileSyncable t = WorldUtil.cast(tile, TileSyncable.class);
					if(t != null)
						t.sync();
					
					e.setUseBlock(Result.DENY);
					e.setUseItem(Result.DENY);
					e.setCanceled(true);
				}
				e.getEntityPlayer().swingArm(e.getHand());
			}
		}
	}
	
	@SubscribeEvent
	public void blockBroken(BlockEvent.BreakEvent e)
	{
		WorldLocation loc = new WorldLocation(e.getWorld(), e.getPos());
		TileEntity tile = loc.getTile();
		
		if(tile instanceof IUpgradable)
		{
			IUpgradable upgradable = (IUpgradable) tile;
			while(upgradable.getInstalledUpgradeCount() > 0)
				upgradable.dropUpgrade(e.getPlayer());
		}
		
		AtmosphereTicker.spillTaint(e.getWorld(), e.getPos());
	}
	
	@SubscribeEvent
	public void getTaintedDrops(TaintedSoilEvent.GetDrops evt)
	{
		TileTaintedSoil soil = evt.soil;
		RoundRobinList<ItemStack> stacks = evt.drops;
		BlockSnapshot s = soil.getSnapshot();
		IBlockState taintedState = s.getReplacedBlock();
		Block taintedBlock = taintedState.getBlock();
		if(taintedBlock instanceof BlockOre && soil.getWorld().rand.nextBoolean())
			stacks.add(EnumMultiMaterialType.CONGEALED_TAINT.stack());
	}
	
	@SubscribeEvent
	public void getDrops(BlockEvent.HarvestDropsEvent e)
	{
		try
		{
			Random rand = e.getWorld().rand;
			
			boolean isInTopazRange = e.getPos().getY() >= 16 && e.getPos().getY() <= 24;
			if(e.getState().getBlock() == Blocks.STONE && Blocks.STONE.getMetaFromState(e.getState()) == 0 && !e.isSilkTouching() && isInTopazRange)
				if(rand.nextInt(75) == 0)
					e.getDrops().add(EnumMultiMaterialType.TOPAZ.stack());
		} catch(Throwable err)
		{
		}
	}
	
	private Map<String, Float> walkSpeeds = new HashMap<>();
	private Map<String, Integer> inactive = new HashMap<>();
	
	@SubscribeEvent
	public void playerTick(PlayerTickEvent e)
	{
		EntityPlayer player = e.player;
		if(player.world.isRemote && e.phase == Phase.END)
			return;
		String id = player.getGameProfile().getName();
		
		AtmosphereChunk chunk = AtmosphereTicker.getAuraChunkFromBlockCoords(player.world, player.getPosition());
		
		if(!player.world.isRemote && chunk.isTainted())
		{
			ResourceLocation loc = new ResourceLocation(LTInfo.MOD_ID, "aura/atmosphere_problems");
			if(!AdvancementUtils.isAdvancementCompleted(loc, player))
				AdvancementUtils.completeAdvancement(loc, player);
		}
		
		if(walkSpeeds.get(id) == null)
			walkSpeeds.put(id, .1F);
		
		float speed = walkSpeeds.get(id);
		float newSpeed = speed;
		
		ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
		if(stack.getItem() instanceof ISpeedBoots)
		{
			ISpeedBoots boots = (ISpeedBoots) stack.getItem();
			newSpeed += boots.getWalkBoost(stack);
			player.stepHeight = player.isSneaking() ? .5F : boots.getStepAssist(stack);
		} else
			player.stepHeight = .5F;
		
		player.capabilities.walkSpeed = newSpeed;
		walkSpeeds.put(id, speed);
		
		if(LTConfigs.effects_AFK)
		{
			int inactive = this.inactive.get(id) != null ? this.inactive.get(id) : 0;
			if(player.prevDistanceWalkedModified == player.distanceWalkedModified && player.onGround)
				inactive++;
			else
				inactive = 0;
			this.inactive.put(id, inactive);
			
			if(inactive > 20 * 60 && inactive % 50 == 0)
				EmoteManager.newEmote(player.world, player.getPositionVector().addVector(0, player.height, 0), DefaultEmotes.SLEEPING).setLifespan(5, 4, 5).setColorRGB(player.getRNG().nextFloat(), player.getRNG().nextFloat(), player.getRNG().nextFloat()).build();
		}
	}
	
	@SubscribeEvent
	public void livingHurt(LivingHurtEvent evt)
	{
		if(evt.getEntityLiving() instanceof EntityPlayer && !evt.getEntity().world.isRemote && LTConfigs.effects_Damage)
			EmoteManager.newEmote(evt.getEntity().world, evt.getEntity().getPositionVector().addVector(0, evt.getEntity().height, 0), DefaultEmotes.WTF).setLifespan(5, 4, 5).setColorRGB(evt.getEntity().world.rand.nextFloat(), evt.getEntity().world.rand.nextFloat(), evt.getEntity().world.rand.nextFloat()).build();
	}
	
	@SubscribeEvent
	public void livingJump(LivingJumpEvent evt)
	{
		if(evt.getEntity() instanceof EntityLivingBase)
		{
			EntityLivingBase base = (EntityLivingBase) evt.getEntity();
			ItemStack stack = base.getItemStackFromSlot(EntityEquipmentSlot.FEET);
			if(stack.getItem() instanceof ISpeedBoots)
			{
				ISpeedBoots boots = (ISpeedBoots) stack.getItem();
				evt.getEntity().motionY *= 1F + boots.getJumpMod(stack) * (evt.getEntity().isSneaking() ? .5F : 1F);
			}
		}
	}
	
	@SubscribeEvent
	public void livingFall(LivingFallEvent evt)
	{
		if(evt.getEntity() instanceof EntityLivingBase)
		{
			EntityLivingBase base = (EntityLivingBase) evt.getEntity();
			{
				ItemStack stack = base.getItemStackFromSlot(EntityEquipmentSlot.FEET);
				if(stack.getItem() instanceof ISpeedBoots)
				{
					ISpeedBoots boots = (ISpeedBoots) stack.getItem();
					if(evt.getDistance() >= 5.5)
						stack.damageItem(1, base);
					evt.setDamageMultiplier(evt.getDamageMultiplier() / (1 + boots.getJumpMod(stack) * 2));
					evt.setDistance(evt.getDistance() / (1 + boots.getJumpMod(stack)));
				}
			}
			for(EnumHand hand : EnumHand.values())
			{
				ItemStack stack = base.getHeldItem(hand);
				if(!stack.isEmpty() && stack.getItem() instanceof ItemAxeElemental)
				{
					evt.setDistance(0);
					evt.setDamageMultiplier(0);
					break;
				}
			}
		}
	}
}