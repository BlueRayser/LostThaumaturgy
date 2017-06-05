package com.pengu.lostthaumaturgy.entity;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.InterItemStack;
import com.mrdimka.hammercore.common.inventory.InventoryNonTile;
import com.mrdimka.hammercore.common.utils.ItemInsertionUtil;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.tiles.IUpgradable;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.inventory.ContainerTravelingTrunk;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.items.ItemWandReversal;
import com.pengu.lostthaumaturgy.net.PacketSyncEntity;
import com.pengu.lostthaumaturgy.net.PacketSyncTrunk;

public class EntityTravelingTrunk extends EntityAnimal implements IUpgradable
{
	public InventoryNonTile inventory = new InventoryNonTile(27);
	public float lidrot;
	public boolean open;
	private int jumpDelay;
	private int eatDelay;
	public int angerLevel;
	public boolean stay;
	public int[] upgrades = new int[] { -1, -1 };
	public int attackTime;
	public UUID owner;
	public ResourceLocation texture = new ResourceLocation(LTInfo.MOD_ID, "textures/entity/trunk.png");
	
	public float field_768_a;
	public float field_767_b;
	
	public EntityTravelingTrunk(World world)
	{
		super(world);
		setSize(.8F, .8F);
	}
	
	public EntityTravelingTrunk(World world, EntityPlayer player, double x, double y, double z)
	{
		super(world);
		owner = player.getGameProfile().getId();
		setPositionAndUpdate(x, y, z);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = super.writeToNBT(nbt);
		
		NBTTagCompound inv = new NBTTagCompound();
		inventory.writeToNBT(inv);
		nbt.setTag("Items", inv);
		nbt.setFloat("LidRotation", lidrot);
		nbt.setInteger("JumpDelay", jumpDelay);
		nbt.setInteger("EatDelay", eatDelay);
		nbt.setInteger("AngerLevel", angerLevel);
		nbt.setBoolean("Stay", stay);
		nbt.setIntArray("Upgrades", upgrades);
		nbt.setInteger("AttackTime", attackTime);
		nbt.setUniqueId("Owner", owner);
		nbt.setString("Texture", texture.toString());
		
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		inventory.readFromNBT(nbt.getCompoundTag("Items"));
		lidrot = nbt.getFloat("LidRotation");
		jumpDelay = nbt.getInteger("JumlDelay");
		eatDelay = nbt.getInteger("EatDelay");
		angerLevel = nbt.getInteger("AngerLevel");
		stay = nbt.getBoolean("Stay");
		upgrades = nbt.getIntArray("Upgrades");
		attackTime = nbt.getInteger("AttackTime");
		owner = nbt.getUniqueId("Owner");
		texture = new ResourceLocation(nbt.getString("Texture"));
	}
	
	public void setTrunkType()
	{
		if(hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.STABILIZED_SINGULARITY)))
			texture = new ResourceLocation(LTInfo.MOD_ID, "textures/entity/trunk_greedy.png");
		
		if(hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.CONTAINED_EMPTINESS)))
		{
			setInvSize(36);
			setSize(.9F, .9F);
			texture = new ResourceLocation(LTInfo.MOD_ID, "textures/entity/trunk_roomy.png");
		} else
		{
			setInvSize(27);
			setSize(.8F, .8F);
		}
		
		if(hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.HARNESSED_RAGE)))
			texture = new ResourceLocation(LTInfo.MOD_ID, "textures/entity/trunk_angry.png");
	}
	
	public void setInvSize(int size)
	{
		if(inventory.getSizeInventory() == size)
			return;
		
		if(size > inventory.getSizeInventory())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			inventory.writeToNBT(nbt);
			nbt.setInteger("InvSize", size);
			inventory = new InventoryNonTile(size);
			inventory.readFromNBT(nbt);
		} else
		
		if(size < inventory.getSizeInventory())
		{
			// drop items that were in extra slots
			for(int i = size; i < inventory.getSizeInventory(); ++i)
			{
				if(!world.isRemote)
					world.spawnEntity(new EntityItem(world, posX, posY, posZ, inventory.getStackInSlot(i).copy()));
				inventory.setInventorySlotContents(i, ItemStack.EMPTY);
			}
			
			NBTTagCompound nbt = new NBTTagCompound();
			inventory.writeToNBT(nbt);
			nbt.setInteger("InvSize", size);
			inventory = new InventoryNonTile(size);
			inventory.readFromNBT(nbt);
		}
	}
	
	@Override
	public EntityAgeable createChild(EntityAgeable ageable)
	{
		return null;
	}
	
	public void eatItems()
	{
		if(getHealth() <= 25 && eatDelay == 0)
		{
			for(int a = 0; a < inventory.getSizeInventory(); ++a)
			{
				if(inventory.getStackInSlot(a).isEmpty() || !(inventory.getStackInSlot(a).getItem() instanceof ItemFood))
					continue;
				ItemFood itemfood = (ItemFood) inventory.getStackInSlot(a).getItem();
				heal(itemfood.getHealAmount(inventory.getStackInSlot(a)));
				inventory.getStackInSlot(a).shrink(1);
				eatDelay = 10 + rand.nextInt(15);
				if(getHealth() == 50)
					HammerCore.audioProxy.playSoundAt(world, "random.burp", getPosition(), .5F, rand.nextFloat() * .5F + .5F, SoundCategory.AMBIENT);
				else
					HammerCore.audioProxy.playSoundAt(world, "random.eat", getPosition(), .5F, rand.nextFloat() * .5F + .5F, SoundCategory.AMBIENT);
				showHeartsOrSmokeFX(true);
				lidrot = .15F;
				break;
			}
		}
		
		if(hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.STABILIZED_SINGULARITY)))
			pullItems();
	}
	
	private void pullItems()
	{
		if(eatDelay > 0)
			return;
		
		List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX - .5, posY - .5, posZ - .5, posX + .5, posY + .5, posZ + .5));
		
		for(EntityItem ei : items)
		{
			ItemStack leftover = ItemInsertionUtil.putStackInInventoryAllSlots(inventory, ei.getEntityItem().copy(), EnumFacing.UP);
			boolean changed = InterItemStack.isStackNull(leftover) || !leftover.isItemEqual(ei.getEntityItem());
			if(InterItemStack.isStackNull(leftover))
				ei.setDead();
			else
				ei.setEntityItemStack(leftover);
			if(changed)
			{
				eatDelay += 2 + rand.nextInt(4);
				return;
			}
		}
	}
	
	protected void updateEntity()
	{
		boolean sync = false;
		
		if(angerLevel > 0)
			--angerLevel;
		if(eatDelay > 0)
			--eatDelay;
		
		MinecraftServer mc = getServer();
		fallDistance = 0F;
		EntityPlayer entityplayer = mc != null ? mc.getPlayerList().getPlayerByUUID(owner) : null;
		if(entityplayer != null)
		{
			if(!(entityplayer.openContainer instanceof ContainerTravelingTrunk) && open)
			{
				open = false;
				HammerCore.audioProxy.playSoundAt(world, "block.chest.close", getPosition(), .1F, rand.nextFloat() * .1F + .9F, SoundCategory.AMBIENT);
			}
			
			EntityLivingBase entity;
			List<EntityLivingBase> list;
			
			tp: if(!stay && entityplayer != null && (getDistanceToEntity(entityplayer) > 20 || (inWater && getDistanceToEntity(entityplayer) > 8 && !entityplayer.isInWater())))
			{
				int i = MathHelper.floor((double) entityplayer.posX) - 2;
				int j = MathHelper.floor((double) entityplayer.posZ) - 2;
				int k = MathHelper.floor((double) entityplayer.posY);
				
				for(int l = 0; l <= 4; ++l)
				{
					for(int i1 = 0; i1 <= 4; ++i1)
					{
						if(l >= 1 && i1 >= 1 && l <= 3 && i1 <= 3 || !world.isBlockNormalCube(new BlockPos(i + l, k - 1, j + i1), true) || world.isBlockNormalCube(new BlockPos(i + l, k, j + i1), true) || world.isBlockNormalCube(new BlockPos(i + l, k + 1, j + i1), true))
							continue;
						HammerCore.audioProxy.playSoundAt(world, "entity.endermen.teleport", i + l + .5, k, j + i1 + .5, .5F, 1, SoundCategory.AMBIENT);
						setPositionAndUpdate(i + l + .5, k, j + i1 + .5);
						showHeartsOrSmokeFX(false);
						setAttackTarget(null);
						angerLevel = 0;
						SIAuraChunk si = AuraTicker.getAuraChunkFromBlockCoords(world, getPosition());
						if(si != null)
							si.badVibes += 5;
						break tp;
					}
				}
			}
			
			if((angerLevel == 0 || getAttackingEntity() == null) && hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.STABILIZED_SINGULARITY)) && !(list = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).expand(16.0, 4.0, 16.0))).isEmpty() && (entity = list.get(rand.nextInt(list.size()))) instanceof IMob && canEntityBeSeen(entity))
			{
				angerLevel = 600;
				setAttackTarget(entity);
			}
			
			boolean move = false;
			
			if(angerLevel > 0 && getAttackTarget() != null && getAttackTarget() != entityplayer)
			{
				faceEntity(getAttackTarget(), 10, 20);
				move = true;
				
				if(attackTime <= 0 && (double) getDistanceSqToEntity(getAttackTarget()) < 1.5 && getAttackTarget().getEntityBoundingBox().maxY > getEntityBoundingBox().minY && getAttackTarget().getEntityBoundingBox().minY < getEntityBoundingBox().maxY)
				{
					float byte0 = 0;
					attackTime = 10 + rand.nextInt(5);
					float by = 4 + upgrades[0] != -1 ? 1 : (byte0 = 0 + upgrades[1] != -1 ? 1 : 0);
					if(hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.HARNESSED_RAGE)))
						byte0 = byte0 + 1;
					if(byte0 > 0F)
						getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), byte0);
					lidrot += 0.015f;
					HammerCore.audioProxy.playSoundAt(world, "entity.blaze.hit", getPosition(), .5F, rand.nextFloat() * .1F + .9F, SoundCategory.HOSTILE);
				}
				
				if(getAttackTarget().isDead)
				{
					setAttackTarget(null);
					angerLevel = 5;
				}
			}
			
			if(entityplayer != null && getDistanceSqToEntity(entityplayer) > 5 && angerLevel == 0 && !stay)
			{
				faceEntity(entityplayer, 10, 20);
				move = true;
			}
			
			if(onGround && jumpDelay-- <= 0 && move)
			{
				jumpDelay = rand.nextInt(10) + 5;
				jumpDelay /= 3;
				
				isJumping = true;
				field_768_a = 1;
				
				// moveStrafing = 1 - rand.nextFloat() * 2;
				// moveForward = 6 +
				// (hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE))
				// ? 2 : 0);
				
				moveStrafing = 0;
				moveForward = 0;
				
				jumpMovementFactor = .03F + (hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE)) ? .01F : 0);
				
				double div = hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE)) ? .4 : .2;
				motionX = com.mrdimka.hammercore.math.MathHelper.clip((entityplayer.posX - posX) / 16D, -div, div);
				motionZ = com.mrdimka.hammercore.math.MathHelper.clip((entityplayer.posZ - posZ) / 16D, -div, div);
				
				jump();
				
				HammerCore.audioProxy.playSoundAt(world, "block.chest.close", getPosition(), .1F, rand.nextFloat() * .1F + .9F, SoundCategory.AMBIENT);
			} else
			{
				isJumping = false;
				if(motionY < 0.0 || open)
					if(lidrot < .5F)
					{
						lidrot += 0.015f;
						sync = true;
					}
				if(lidrot > .5F)
				{
					lidrot = .5F;
					sync = true;
				}
				if(onGround)
				{
					moveForward = 0;
					moveStrafing = 0;
					if(!open)
					{
						if(lidrot > 0)
						{
							lidrot -= .1F;
							sync = true;
						}
						
						if(lidrot < 0F)
						{
							lidrot = 0F;
							sync = true;
						}
					}
				}
			}
			
			if(open)
			{
				lidrot += .035F;
				sync = true;
			}
			
			if(lidrot > .5F)
			{
				lidrot = .5F;
				sync = true;
			}
		}
		
		if(sync)
			HCNetwork.manager.sendToAllAround(new PacketSyncEntity(this), new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 32));
	}
	
	void showHeartsOrSmokeFX(boolean flag)
	{
		EnumParticleTypes s = EnumParticleTypes.HEART;
		int amount = 1;
		
		if(!flag)
		{
			s = EnumParticleTypes.EXPLOSION_NORMAL;
			amount = 7;
		}
		
		for(int i = 0; i < amount; ++i)
		{
			double d = rand.nextGaussian() * 0.02;
			double d1 = rand.nextGaussian() * 0.02;
			double d2 = rand.nextGaussian() * 0.02;
			world.spawnParticle(s, posX + rand.nextFloat() * width * 2 - width, posZ + .5 + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2 - width, d, d1, d2);
		}
	}
	
	@Override
	public void onUpdate()
	{
		this.field_767_b = this.field_768_a;
		boolean flag = onGround;
		super.onUpdate();
		updateEntity();
		eatItems();
		if(onGround && !flag)
			field_768_a = -0.5f;
		field_768_a *= 0.6f;
	}
	
	@Override
	public boolean canMateWith(EntityAnimal otherAnimal)
	{
		return false;
	}
	
	@Override
	protected boolean canDespawn()
	{
		return false;
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer player)
	{
		return 0;
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		if(!player.getGameProfile().getId().equals(owner))
		{
			if(!world.isRemote)
			{
				player.sendMessage(new TextComponentTranslation("chat." + LTInfo.MOD_ID + ":not_my_master"));
				return true;
			}
		}
		
		ItemStack held = player.getHeldItem(hand);
		
		if(!held.isEmpty() && held.getItem() instanceof ItemFood && getHealth() < 50F)
		{
			heal(((ItemFood) held.getItem()).getHealAmount(held));
			held.shrink(1);
			if(getHealth() == 50)
				HammerCore.audioProxy.playSoundAt(world, "random.burp", getPosition(), .5F, rand.nextFloat() * .5F + .5F, SoundCategory.AMBIENT);
			else
				HammerCore.audioProxy.playSoundAt(world, "random.eat", getPosition(), .5F, rand.nextFloat() * .5F + .5F, SoundCategory.AMBIENT);
			showHeartsOrSmokeFX(true);
			lidrot = .15F;
			
			return true;
		}
		
		if(!held.isEmpty() && held.getItem() instanceof ItemWandReversal)
		{
			if(!dropUpgrade(player))
			{
				if(!world.isRemote)
				{
					inventory.drop(world, getPosition());
					setDead();
				}
				WorldUtil.spawnItemStack(world, posX, posY, posZ, EnumMultiMaterialType.TRAVELING_TRUNK.stack());
			}
			held.damageItem(1, player);
			if(!world.isRemote)
				HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":zap", getPosition(), .5F, 1F, SoundCategory.PLAYERS);
			player.swingArm(hand);
			setTrunkType();
			return true;
		}
		
		if(!held.isEmpty() && held.getItem() instanceof ItemUpgrade)
		{
			int up = ItemUpgrade.idFromItem((ItemUpgrade) held.getItem());
			if(canAcceptUpgrade(up))
			{
				if(setUpgrade(up))
				{
					if(!world.isRemote)
						HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":upgrade", getPosition(), .5F, 1F, SoundCategory.PLAYERS);
					player.swingArm(hand);
					player.getHeldItem(hand).shrink(1);
					setTrunkType();
				}
			}
			return true;
		}
		
		open = true;
		
		if(player instanceof EntityPlayerMP && !world.isRemote)
		{
			HCNetwork.manager.sendTo(new PacketSyncTrunk(this), (EntityPlayerMP) player);
			HammerCore.audioProxy.playSoundAt(world, "block.chest.open", getPosition(), .1F, rand.nextFloat() * .1F + .9F, SoundCategory.AMBIENT);
		}
		
		player.openContainer = new ContainerTravelingTrunk(this, player);
		
		return true;
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50);
	}
	
	private float updateRotation(float f, float f1, float f2)
	{
		float f3;
		for(f3 = f1 - f; f3 < -180; f3 += 360)
			;
		while(f3 >= 180)
			f3 -= 360;
		if(f3 > f2)
			f3 = f2;
		if(f3 < -f2)
			f3 = -f2;
		return f + f3;
	}
	
	@Override
	public int[] getUpgrades()
	{
		return upgrades;
	}
	
	@Override
	public boolean canAcceptUpgrade(int type)
	{
		// disable other upgrades for now
		// && type != ItemUpgrade.idFromItem(ItemsLT.HARNESSED_RAGE)
		// && type != ItemUpgrade.idFromItem(ItemsLT.STABILIZED_SINGULARITY)
		if(type != ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE) && type != ItemUpgrade.idFromItem(ItemsLT.CONTAINED_EMPTINESS))
			return false;
		if(hasUpgrade(type))
			return false;
		return true;
	}
}