package com.pengu.lostthaumaturgy.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import com.mrdimka.hammercore.common.inventory.InventoryNonTile;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.golem.GolemCoreAbstract;
import com.pengu.lostthaumaturgy.emote.EmoteManager;
import com.pengu.lostthaumaturgy.emote.EmoteManager.DefaultEmotes;
import com.pengu.lostthaumaturgy.entity.ai.LTPathNavigate;

public class EntityGolemBase extends EntityGolem
{
	public ItemStack itemCarried = ItemStack.EMPTY;
	public int maxCarried = 16;
	protected short color;
	public EnumFacing homeFacing = EnumFacing.DOWN;
	public short golemType = 0;
	public boolean paused = false;
	public InventoryNonTile inventory = null;
	public String decoration = "";
	protected LTPathNavigate navigator;
	public int regenTimer = 0;
	protected int regenInterval = 200;
	boolean pdw = false;
	public int action = 0;
	public int leftArm = 0;
	public int healing = 0;
	
	private static final DataParameter<String> DISPLAY_DECORATIONS = EntityDataManager.createKey(EntityGolemBase.class, DataSerializers.STRING);
	private static final DataParameter<String> OWNER = EntityDataManager.createKey(EntityGolemBase.class, DataSerializers.STRING);
	private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityGolemBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityGolemBase.class, DataSerializers.VARINT);
	private static final DataParameter<ItemStack> CARRIED = EntityDataManager.createKey(EntityGolemBase.class, DataSerializers.OPTIONAL_ITEM_STACK);
	private static final DataParameter<GolemCoreAbstract> CORE = EntityDataManager.createKey(EntityGolemBase.class, GolemCoreAbstract.SERIALIZER);
	
	public EntityGolemBase(World worldIn)
	{
		super(worldIn);
		stepHeight = 1;
		setSize(.4F, .95F);
		navigator = new LTPathNavigate(this, worldIn, 32);
		getLTNavigator().setAvoidsWater(true);
		getLTNavigator().setBreakDoors(true);
		getLTNavigator().setEnterDoors(true);
		getLTNavigator().setCanSwim(true);
	}
	
	public LTPathNavigate getLTNavigator()
	{
		return navigator;
	}
	
	@Override
	public float getAIMoveSpeed()
	{
		float speed = (golemType == 1 ? 0.36f : 0.28f) * (decoration.contains("B") ? 1.1f : 1.0f);
		if(decoration.contains("P"))
			speed *= 0.88f;
		return speed;
	}
	
	public void setup(int type, int color, EnumFacing side)
	{
		golemType = (short) type;
		color = (short) color;
		homeFacing = side;
		maxCarried = type == 4 ? 32 : 16;
		dataManager.set(COLOR, color);
		dataManager.set(TYPE, type);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(DISPLAY_DECORATIONS, "");
		dataManager.register(COLOR, 0);
		dataManager.register(TYPE, 0);
		dataManager.register(OWNER, "");
		dataManager.register(CARRIED, ItemStack.EMPTY);
		dataManager.register(CORE, null);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		paused = false;
		if(source == DamageSource.CACTUS)
			return false;
		return super.attackEntityFrom(source, amount);
	}
	
	public boolean isAIEnabled()
	{
		return !paused;
	}
	
	protected void updateAITasks()
	{
	}
	
	public String getOwnerName()
	{
		return dataManager.get(OWNER);
	}
	
	public GolemCoreAbstract getCore()
	{
		return dataManager.get(CORE);
	}
	
	public void setCore(GolemCoreAbstract core)
	{
		dataManager.set(CORE, core);
	}
	
	public void setOwner(String par1Str)
	{
		dataManager.set(OWNER, par1Str);
	}
	
	public EntityPlayer getOwner()
	{
		return world.getPlayerEntityByName(getOwnerName());
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
	}
	
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if(this.action > 0)
			--this.action;
		if(this.leftArm > 0)
			--this.leftArm;
		if(this.healing > 0)
			--this.healing;
		if(this.regenTimer > 0)
			--this.regenTimer;
		else
		{
			this.regenTimer = this.regenInterval;
			if(this.decoration.contains("F"))
				this.regenTimer /= 2;
			if(!this.world.isRemote && this.getHealth() < this.getMaxHealth() && AuraTicker.decreaseClosestAura(world, posX, posZ, 2))
			{
				this.world.setEntityState(this, (byte) 5);
				this.heal(1);
			}
		}
		if(!world.isRemote && (this.getDistanceSq((double) this.getHomePosition().getX(), (double) this.getHomePosition().getY(), (double) this.getHomePosition().getZ()) >= 2304.0 || this.isEntityInsideOpaqueBlock()))
		{
			int var1 = MathHelper.floor((double) this.getHomePosition().getX());
			int var2 = MathHelper.floor((double) this.getHomePosition().getY());
			int var3 = MathHelper.floor((double) this.getHomePosition().getZ());
			for(int var0 = 1; var0 >= -1; --var0)
			{
				for(int var4 = -1; var4 <= 1; ++var4)
				{
					for(int var5 = -1; var5 <= 1; ++var5)
					{
						if(!this.world.getBlockState(new BlockPos(var1 + var4, var3 - 1 + var0, var2 + var5)).isSideSolid(world, new BlockPos(var1 + var4, var3 - 1 + var0, var2 + var5), EnumFacing.UP) || world.isBlockNormalCube(new BlockPos(var1 + var4, var3 + var0, var2 + var5), false))
							continue;
						this.setLocationAndAngles((double) ((float) (var1 + var4) + 0.5f), (double) var3 + (double) var0, (double) ((float) (var2 + var5) + 0.5f), this.rotationYaw, this.rotationPitch);
						getLTNavigator().clearPathEntity();
						return;
					}
				}
			}
		}
		GolemCoreAbstract core = getCore();
		if(!world.isRemote && core != null)
		{
			core.setGolem(this);
			core.updateLogic();
			String type = core.getEmote();
			if(type != null && ticksExisted % 25 == 0)
				EmoteManager.newEmote(world, getPositionVector().addVector(0, height, 0), type).setColorRGB(0xFFFF00).setLifespan(5, 10, 5).build();
		}
	}
	
	public float getRange()
	{
		float dmod = this.golemType == 3 ? 24 : 16;
		if(this.decoration.contains("G"))
			dmod *= 1.2;
		return dmod;
	}
	
	public boolean isWithinHomeDistance(int par1, int par2, int par3)
	{
		float dmod = getRange();
		return getHomePosition().distanceSq(par1, par2, par3) < dmod * dmod;
	}
	
	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}
	
	@Override
	protected boolean canDespawn()
	{
		return false;
	}
	
	@Override
	protected void despawnEntity()
	{
	}
	
	@Override
	protected int decreaseAirSupply(int air)
	{
		return air;
	}
	
	public void setCarried(ItemStack stack)
	{
		this.itemCarried = stack;
		this.updateCarried();
	}
	
	public boolean hasSomething()
	{
		return !this.inventory.isEmpty();
	}
	
	public ItemStack getCarriedForDisplay()
	{
		return dataManager.get(CARRIED);
	}
	
	public ItemStack getCarried()
	{
		return this.itemCarried;
	}
	
	public int getCarrySpace()
	{
		if(this.itemCarried == null)
			return this.maxCarried;
		return Math.min(this.maxCarried - this.itemCarried.getCount(), this.itemCarried.getMaxStackSize() - this.itemCarried.getCount());
	}
	
	public int getGolemTypeForDisplay()
	{
		return dataManager.get(TYPE);
	}
	
	public void updateCarried()
	{
		if(itemCarried.isEmpty())
		{
			dataManager.set(CARRIED, itemCarried.copy());
			// if(!this.pdw && this.itemCarried.getItem().itemID ==
			// Config.sonicscrew && this.decoration.contains("F") &&
			// this.decoration.contains("B"))
			// {
			// this.pdw = true;
			// this.worldObj.playSoundAtEntity((Entity) this, "thaumcraft.dw",
			// 0.5f, 1.0f);
			// }
		} else
			dataManager.setDirty(CARRIED);
	}
	
	public short getColor()
	{
		return dataManager.get(COLOR).shortValue();
	}
	
	public void setColor(short color)
	{
		this.color = color;
		dataManager.set(COLOR, (int) color);
	}
	
	public int getActionTimer()
	{
		return 4 - Math.abs(action - 4);
	}
	
	public void startActionTimer()
	{
		if(action == 0)
		{
			action = 8;
			world.setEntityState(this, (byte) 4);
		}
	}
	
	public void startLeftArmTimer()
	{
		if(leftArm == 0)
		{
			leftArm = 3;
			world.setEntityState(this, (byte) 6);
		}
	}
	
	@Override
	public void handleStatusUpdate(byte id)
	{
		if(id == 4)
			action = 8;
		else if(id == 5)
			healing = 5;
		else if(id == 6)
			leftArm = 3;
		else
			super.handleStatusUpdate(id);
	}
	
	protected void dropFewItems(boolean par1, int par2)
	{
	}
	
	protected boolean addDecoration(String type, ItemStack itemStack)
	{
		if(decoration.contains(type))
		{
			return false;
		}
		if((type.equals("F") || type.equals("H")) && (decoration.contains("F") || decoration.contains("H")))
			return false;
		if((type.equals("G") || type.equals("V")) && (decoration.contains("G") || decoration.contains("V")))
			return false;
		if((type.equals("B") || type.equals("P")) && (decoration.contains("P") || decoration.contains("B")))
			return false;
		decoration = decoration + type;
		if(!world.isRemote)
		{
			setGolemDecoration();
			itemStack.shrink(1);
		}
		return true;
	}
	
	public String getGolemDecorationForDisplay()
	{
		return dataManager.get(DISPLAY_DECORATIONS);
	}
	
	public void setGolemDecoration()
	{
		dataManager.set(DISPLAY_DECORATIONS, decoration);
	}
	
	@Override
	protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
	{
		Block var7;
		int var6;
		int var5;
		int var4;
		if(onGroundIn && fallDistance > 0.0f && (var7 = world.getBlockState(new BlockPos(var4 = MathHelper.floor((double) posX), var5 = MathHelper.floor((double) (posY - 0.20000000298023224)), var6 = MathHelper.floor((double) posZ))).getBlock()) == Blocks.AIR && world.getBlockState(new BlockPos(var4, var5 - 1, var6)) instanceof BlockFence)
			var7 = world.getBlockState(new BlockPos(var4, var5 - 1, var6)).getBlock();
		if(onGroundIn)
		{
			if(fallDistance > 0.0f)
			{
				fall(fallDistance, 1);
				fallDistance = 0.0f;
			}
		} else if(y < 0.0)
		{
			fallDistance = (float) ((double) fallDistance - y);
		}
	}
}