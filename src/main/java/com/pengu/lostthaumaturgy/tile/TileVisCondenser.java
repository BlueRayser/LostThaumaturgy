package com.pengu.lostthaumaturgy.tile;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import com.mrdimka.hammercore.common.inventory.InventoryNonTile;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.net.HCNetwork;
import com.mrdimka.hammercore.tile.ITileDroppable;
import com.mrdimka.hammercore.tile.TileSyncableTickable;
import com.pengu.hammercore.asm.WorldHooks;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;
import com.pengu.lostthaumaturgy.api.tiles.IUpgradable;
import com.pengu.lostthaumaturgy.client.gui.GuiVisCondenser;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.inventory.ContainerVisCondenser;
import com.pengu.lostthaumaturgy.items.ItemMultiMaterial.EnumMultiMaterialType;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp_TileVisCondenser_tick;

public class TileVisCondenser extends TileSyncableTickable implements IConnection, IUpgradable, ISidedInventory, ITileDroppable
{
	public float angle;
	public float speed;
	public long accTimer;
	public float progress;
	public float currentVis;
	public float currentTaint;
	public int currentType = -1;
	public short maxVis = 10;
	private short prevVis;
	public float degredation;
	private InventoryNonTile inventory = new InventoryNonTile(2);
	private int[] upgrades = new int[] { -1, -1 };
	
	@Override
	public boolean hasGui()
	{
		return true;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiVisCondenser(player, this);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerVisCondenser(player, this);
	}
	
	@Override
	public void addProperties(Map<String, Object> properties, RayTraceResult trace)
	{
		String name = "none";
		
		switch(currentType)
		{
		case 1:
		{
			name = "vaporous";
			break;
		}
		case 2:
		{
			name = "aqueous";
			break;
		}
		case 3:
		{
			name = "earthen";
			break;
		}
		case 4:
		{
			name = "fiery";
			break;
		}
		case 5:
		{
			name = "tainted";
			break;
		}
		default:
		{
			name = "vis";
			break;
		}
		}
		
		properties.put("crystal", currentType);
		properties.put("angle", LostThaumaturgy.standartDecimalFormat.format(angle));
	}
	
	@Override
	public void tick()
	{
		if(loc.getRedstone() > 0)
		{
			--ticksExisted;
			return;
		}
		
		angle += speed * 5;
		if(angle > 360)
			angle -= 360;
		
		if(accTimer < System.currentTimeMillis())
		{
			equalizeWithNeighbours();
			if(speed < (hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE)) ? 1.25F : 1.0F))
			{
				speed += hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE)) ? .01F : 5.0E-4F;
				if(speed > (hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE)) ? 1.25F : 1))
					speed = hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE)) ? 1.25F : 1;
			}
			if(speed > 0.0f && degredation == 0.0f)
			{
				speed -= .005F;
				if(speed < 0)
					speed = 0;
			}
			if(!hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.CONCENTRATED_EVIL)) && currentType == 5)
			{
				progress = 0.0f;
				degredation = 0.0f;
			}
			float moon = (float) (3 + Math.abs(WorldHooks.getMoonPhase(world) - 4)) * .2F;
			if(currentType >= 0)
				progress += speed * moon;
			if(progress >= (hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.STABILIZED_SINGULARITY)) ? 20 : 25) && currentVis <= maxVis - 1 && currentTaint <= maxVis - 1 && currentType >= 0)
			{
				SIAuraChunk ac = AuraTicker.getAuraChunkFromBlockCoords(world, pos);
				if(ac != null)
				{
					if(currentType != 5)
					{
						if(ac.vis >= 1)
						{
							ac.vis--;
							progress = 0;
							currentVis += 1;
							ac.radiation += .0002F;
						}
					} else if(hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.CONCENTRATED_EVIL)) && ac.taint >= 1)
					{
						ac.taint--;
						progress = 0.0f;
						currentTaint += 1.0f;
					}
				}
				sync();
			}
			boolean flag = false;
			if(progress < (float) (hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.STABILIZED_SINGULARITY)) ? 20 : 25))
			{
				if(degredation > 0F)
					flag = true;
				degredation -= Math.max(.25F, speed);
				if(degredation < 0)
					degredation = 0;
				if(degredation > 0F && (int) (degredation % 3F) == 0)
					HCNetwork.manager.sendToAllAround(new PacketFXWisp_TileVisCondenser_tick(pos.getX() + .5 + world.rand.nextFloat() - world.rand.nextFloat(), pos.getY() + 1.5 + world.rand.nextFloat() - world.rand.nextFloat(), (float) pos.getZ() + .5 + world.rand.nextFloat() - world.rand.nextFloat(), pos.getX() + .5, pos.getY() + 1.5, pos.getZ() + .5, .6F, currentType), getSyncPoint(48));
			}
			if(degredation < 10F && flag)
				HCNetwork.spawnParticle(world, EnumParticleTypes.SMOKE_LARGE, pos.getX() + .5, pos.getY() + 1.3, pos.getZ() + .5, 0, 0, 0);
			if(degredation <= 0.0f)
			{
				if(flag && !inventory.getStackInSlot(1).isEmpty() && inventory.getStackInSlot(1).getItem() == ItemsLT.MULTI_MATERIAL && inventory.getStackInSlot(1).getItemDamage() == EnumMultiMaterialType.DEPLETED_CRYSTAL.getDamage())
				{
					if(inventory.getStackInSlot(1).getCount() < 64)
						inventory.getStackInSlot(1).grow(1);
					else
					{
						EntityItem entityitem = new EntityItem(world, pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5, EnumMultiMaterialType.DEPLETED_CRYSTAL.stack());
						entityitem.motionY = 0.20000000298023224;
						world.spawnEntity(entityitem);
					}
				}
				if(flag && inventory.getStackInSlot(1).isEmpty())
					inventory.setInventorySlotContents(1, EnumMultiMaterialType.DEPLETED_CRYSTAL.stack());
				if(!inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(0).getItem() == ItemsLT.MULTI_MATERIAL && inventory.getStackInSlot(0).getItemDamage() < EnumMultiMaterialType.DEPLETED_CRYSTAL.getDamage() && (inventory.getStackInSlot(0).getItemDamage() != EnumMultiMaterialType.TAINTED_CRYSTAL.getDamage() || hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.CONCENTRATED_EVIL))))
				{
					degredation = 4550;
					currentType = inventory.getStackInSlot(0).getItemDamage();
					
					if(currentType == EnumMultiMaterialType.VIS_CRYSTAL.getDamage())
						currentType = 0;
					
					else if(currentType == EnumMultiMaterialType.VAPOROUS_CRYSTAL.getDamage())
						currentType = 1;
					
					else if(currentType == EnumMultiMaterialType.AQUEOUS_CRYSTAL.getDamage())
						currentType = 2;
					
					else if(currentType == EnumMultiMaterialType.EARTHEN_CRYSTAL.getDamage())
						currentType = 3;
					
					else if(currentType == EnumMultiMaterialType.FIERY_CRYSTAL.getDamage())
						currentType = 4;
					
					else if(currentType == EnumMultiMaterialType.TAINTED_CRYSTAL.getDamage())
						currentType = 5;
					
					inventory.getStackInSlot(0).shrink(1);
				} else
					currentType = -1;
			}
			accTimer = System.currentTimeMillis() + 100;
		}
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		NBTTagCompound tag = new NBTTagCompound();
		inventory.writeToNBT(tag);
		nbt.setTag("Items", tag);
		nbt.setFloat("Speed", speed);
		nbt.setFloat("Progress", progress);
		nbt.setFloat("CurrentVis", currentVis);
		nbt.setFloat("CurrentTaint", currentTaint);
		nbt.setInteger("CurrentType", currentType);
		nbt.setFloat("Taint", degredation);
		nbt.setIntArray("Upgrades", upgrades);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		inventory.readFromNBT(nbt.getCompoundTag("Items"));
		speed = nbt.getFloat("Speed");
		progress = nbt.getFloat("Progress");
		currentVis = nbt.getFloat("CurrentVis");
		currentTaint = nbt.getFloat("CurrentTaint");
		currentType = nbt.getInteger("CurrentType");
		degredation = nbt.getFloat("Taint");
		upgrades = nbt.getIntArray("Upgrades");
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(pos).expand(0, 1, 0);
	}
	
	protected void equalizeWithNeighbours()
	{
		ArrayList<IConnection> neighbours = new ArrayList<IConnection>();
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			TileEntity te = world.getTileEntity(pos);
			IConnection conn = WorldUtil.cast(te, IConnection.class);
			if(!getConnectable(facing) || (te = (TileEntity) conn) == null || !(te instanceof TileVisCondenser))
				continue;
			IConnection ent = (IConnection) te;
			neighbours.add(ent);
		}
		if(neighbours.size() > 0)
		{
			int a;
			float pVis = getPureVis();
			float tVis = getTaintedVis();
			for(a = 0; a < neighbours.size(); ++a)
			{
				pVis += ((IConnection) neighbours.get(a)).getPureVis();
			}
			for(a = 0; a < neighbours.size(); ++a)
			{
				tVis += ((IConnection) neighbours.get(a)).getTaintedVis();
			}
			pVis /= (float) (neighbours.size() + 1);
			tVis /= (float) (neighbours.size() + 1);
			for(a = 0; a < neighbours.size(); ++a)
			{
				((IConnection) neighbours.get(a)).setPureVis(pVis);
				((IConnection) neighbours.get(a)).setTaintedVis(tVis);
			}
			setPureVis(pVis);
			setTaintedVis(tVis);
		}
	}
	
	@Override
	public boolean isVisSource()
	{
		return true;
	}
	
	@Override
	public boolean isVisConduit()
	{
		return false;
	}
	
	@Override
	public float getPureVis()
	{
		return currentVis;
	}
	
	@Override
	public void setPureVis(float amount)
	{
		currentVis = amount;
	}
	
	@Override
	public float getTaintedVis()
	{
		return currentTaint;
	}
	
	@Override
	public float getMaxVis()
	{
		return maxVis;
	}
	
	@Override
	public void setTaintedVis(float amount)
	{
		currentTaint = amount;
	}
	
	@Override
	public float[] subtractVis(float amount)
	{
		float pureAmount = amount / 2.0f;
		float taintAmount = amount / 2.0f;
		float[] result = new float[] { 0.0f, 0.0f };
		if(amount < 0.001f)
		{
			return result;
		}
		if(currentVis < pureAmount)
		{
			pureAmount = currentVis;
		}
		if(currentTaint < taintAmount)
		{
			taintAmount = currentTaint;
		}
		if(pureAmount < amount / 2.0f && taintAmount == amount / 2.0f)
		{
			taintAmount = Math.min(amount - pureAmount, currentTaint);
		} else if(taintAmount < amount / 2.0f && pureAmount == amount / 2.0f)
		{
			pureAmount = Math.min(amount - taintAmount, currentVis);
		}
		currentVis -= pureAmount;
		currentTaint -= taintAmount;
		result[0] = pureAmount;
		result[1] = taintAmount;
		return result;
	}
	
	@Override
	public int getVisSuction(BlockPos loc)
	{
		return 0;
	}
	
	@Override
	public void setVisSuction(int suction)
	{
	}
	
	@Override
	public int getTaintSuction(BlockPos loc)
	{
		return 0;
	}
	
	@Override
	public void setTaintSuction(int suction)
	{
	}
	
	@Override
	public void setSuction(int suction)
	{
	}
	
	@Override
	public int getSuction(BlockPos loc)
	{
		return 0;
	}
	
	@Override
	public boolean getConnectable(EnumFacing to)
	{
		return to != EnumFacing.UP;
	}
	
	@Override
	public int[] getUpgrades()
	{
		return upgrades;
	}
	
	@Override
	public boolean canAcceptUpgrade(int type)
	{
		if(type != ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE) && type != ItemUpgrade.idFromItem(ItemsLT.STABILIZED_SINGULARITY) && type != ItemUpgrade.idFromItem(ItemsLT.CONCENTRATED_EVIL))
			return false;
		if(hasUpgrade(type))
			return false;
		return true;
	}
	
	@Override
	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}
	
	@Override
	public boolean isEmpty()
	{
		return inventory.isEmpty();
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory.getStackInSlot(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return inventory.decrStackSize(index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return inventory.removeStackFromSlot(index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		inventory.setInventorySlotContents(index, stack);
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return inventory.isUsableByPlayer(player, pos);
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return index == 0 && stack.getItem() == ItemsLT.MULTI_MATERIAL && stack.getItemDamage() < EnumMultiMaterialType.DEPLETED_CRYSTAL.getDamage();
	}
	
	@Override
	public int getField(int id)
	{
		return 0;
	}
	
	@Override
	public void setField(int id, int value)
	{
	}
	
	@Override
	public int getFieldCount()
	{
		return 0;
	}
	
	@Override
	public void clear()
	{
		inventory.clear();
	}
	
	@Override
	public String getName()
	{
		return "Vis Condenser";
	}
	
	@Override
	public boolean hasCustomName()
	{
		return true;
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return inventory.getAllAvaliableSlots();
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return isItemValidForSlot(index, itemStackIn);
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return !canInsertItem(index, stack, direction);
	}
	
	@Override
	public void createDrop(EntityPlayer player, World world, BlockPos pos)
	{
		inventory.drop(world, pos);
	}
}