package com.pengu.lostthaumaturgy.core.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.capabilities.CapabilityEJ;
import com.pengu.hammercore.energy.IPowerStorage;
import com.pengu.hammercore.gui.container.ContainerEmpty;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.api.tiles.IUpgradable;
import com.pengu.lostthaumaturgy.api.tiles.TileVisUser;
import com.pengu.lostthaumaturgy.client.gui.GuiGenerator;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereTicker;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.net.zap.PacketSpawnGeneratorZap;

public class TileGenerator extends TileVisUser implements IEnergyStorage, IPowerStorage, IUpgradable
{
	private int genloop = 0;
	public float rotation = -1;
	
	public int storedEnergy = 0;
	public int energyMax = 5000;
	public int incomeEnergy = 0;
	public boolean reversed = false;
	
	public float maxVis = 20;
	public float pureVis, taintedVis;
	
	private int[] upgrades = new int[] { -1, -1 };
	
	@Override
	public void tick()
	{
		super.tick();
		
		AtmosphereChunk si = AtmosphereTicker.getAuraChunkFromBlockCoords(world, pos);
		
		reversed = hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.INFINITE_SADNESS));
		
		if(world.isRemote)
		{
			if(rotation == -1)
				rotation = world.rand.nextInt(360);
			
			rotation += 1;
			
			if(gettingPower())
				rotation--;
			
			if(rotation > 360)
				rotation -= 360;
		}
		
		boolean emitPower = false;
		
		if(loc.getRedstone() <= 0)
		{
			if(storedEnergy < energyMax && !reversed)
			{
				float visperunit;
				float s;
				float suck;
				float mod;
				int moon = world.provider.getMoonPhase(world.getWorldTime());
				if(hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE)))
					moon += .2F;
				if((suck = (visperunit = 6.6666666E-4F * (mod = hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.STABILIZED_SINGULARITY)) ? .8F : 1)) * Math.min(75 * moon, energyMax - storedEnergy)) > .006666667F && getExactPureVis(s = suck))
				{
					float add = suck * 150;
					storedEnergy += Math.round(add);
					emitPower = true;
					si.radiation += .000001F;
					
					if(emitPower && world.rand.nextInt(9) == 0)
						HCNetwork.getManager("particles").sendToAllAround(new PacketSpawnGeneratorZap(fromPos(pos), fromPos(pos)), getSyncPoint(64));
				}
			}
			
			if(storedEnergy > energyMax)
				storedEnergy = energyMax;
			
			if(!reversed)
				for(EnumFacing f : EnumFacing.VALUES)
				{
					WorldLocation l = new WorldLocation(world, pos.offset(f));
					TileEntity tile = l.getTile();
					if(tile != null)
					{
						int limit = hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE)) ? 800 : 400;
						if(tile.hasCapability(CapabilityEnergy.ENERGY, f.getOpposite()))
						{
							IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, f.getOpposite());
							if(storage.canReceive())
								emitPower = extractEnergy(storage.receiveEnergy(extractEnergy(limit, true), false), false) > 0;
							if(emitPower && world.rand.nextInt(9) == 0)
								HCNetwork.getManager("particles").sendToAllAround(new PacketSpawnGeneratorZap(fromPos(pos), fromPos(l.getPos())), getSyncPoint(64));
						} else if(tile.hasCapability(CapabilityEJ.ENERGY, f.getOpposite()))
						{
							IPowerStorage storage = tile.getCapability(CapabilityEJ.ENERGY, f.getOpposite());
							emitPower = extractEnergy(storage.receiveEnergy(extractEnergy(limit, true), false), false) > 0;
							if(emitPower && world.rand.nextInt(9) == 0)
								HCNetwork.getManager("particles").sendToAllAround(new PacketSpawnGeneratorZap(fromPos(pos), fromPos(l.getPos())), getSyncPoint(64));
						}
					}
				}
		}
		
		if(reversed && !gettingPower())
		{
			if(incomeEnergy > 0)
			{
				float gen = 1F;
				float maxTake = maxVis - (taintedVis + pureVis);
				int energyReq = 1000;
				int energyProv = Math.min(energyReq, incomeEnergy);
				gen = Math.min((float) energyProv / (float) energyReq, maxTake);
				energyProv = (int) (energyReq * gen);
				
				if(gen > 0F)
				{
					incomeEnergy -= energyProv;
					
					boolean evil = hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.CONCENTRATED_EVIL));
					
					if(evil)
						taintedVis += gen;
					else
					{
						pureVis += gen / 2;
						taintedVis += gen / 2;
					}
					
					si.radiation += .000002F;
					
					emitPower = true;
					
					if(world.rand.nextInt(9) == 0)
						HCNetwork.getManager("particles").sendToAllAround(new PacketSpawnGeneratorZap(fromPos(pos), fromPos(pos)), getSyncPoint(64));
				}
			}
		}
		
		if(world.isRemote)
			return;
		
		if(genloop == 0 && emitPower)
		{
			HammerCore.audioProxy.playSoundAt(world, Info.MOD_ID + ":elecloop", pos, .1F, 1F, SoundCategory.BLOCKS);
			AtmosphereChunk ac = AtmosphereTicker.getAuraChunkFromBlockCoords(world, pos);
			if(ac != null)
				ac.badVibes++;
		}
		
		++genloop;
		
		if(genloop >= 70)
			genloop = 0;
	}
	
	@Override
	public boolean hasGui()
	{
		return true;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiGenerator(this);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerEmpty();
	}
	
	public Vec3d fromPos(BlockPos pos)
	{
		return new Vec3d(pos.getX() + .5 + (world.rand.nextDouble() - world.rand.nextDouble()) * .45, pos.getY() + .5 + (world.rand.nextDouble() - world.rand.nextDouble()) * .45, pos.getZ() + .5 + (world.rand.nextDouble() - world.rand.nextDouble()) * .45);
	}
	
	@Override
	public boolean isVisSource()
	{
		return reversed;
	}
	
	@Override
	public float[] subtractVis(float amount)
	{
		if(reversed)
		{
			float pureAmount = amount / 2.0f;
			float taintAmount = amount / 2.0f;
			float[] result = new float[] { 0.0f, 0.0f };
			if(amount < 0.001f)
				return result;
			if(pureVis < pureAmount)
				pureAmount = pureVis;
			if(taintedVis < taintAmount)
				taintAmount = taintedVis;
			if(pureAmount < amount / 2.0f && taintAmount == amount / 2.0f)
				taintAmount = Math.min(amount - pureAmount, taintedVis);
			else if(taintAmount < amount / 2.0f && pureAmount == amount / 2.0f)
				pureAmount = Math.min(amount - taintAmount, pureVis);
			pureVis -= pureAmount;
			taintedVis -= taintAmount;
			result[0] = pureAmount;
			result[1] = taintAmount;
			return result;
		}
		
		return super.subtractVis(amount);
	}
	
	@Override
	public float getPureVis()
	{
		return pureVis;
	}
	
	@Override
	public void setPureVis(float amount)
	{
		pureVis = amount;
	}
	
	@Override
	public float getTaintedVis()
	{
		return taintedVis;
	}
	
	@Override
	public void setTaintedVis(float amount)
	{
		taintedVis = amount;
	}
	
	@Override
	public float getMaxVis()
	{
		return maxVis;
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		super.writeNBT(nbt);
		nbt.setIntArray("Upgrades", upgrades);
		nbt.setFloat("PureVis", this.pureVis);
		nbt.setFloat("TaintedVis", this.taintedVis);
		nbt.setInteger("IncomeEnergy", incomeEnergy);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		super.readNBT(nbt);
		upgrades = nbt.getIntArray("Upgrades");
		pureVis = nbt.getFloat("PureVis");
		taintedVis = nbt.getFloat("TaintedVis");
		incomeEnergy = nbt.getInteger("IncomeEnergy");
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityEnergy.ENERGY || capability == CapabilityEJ.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityEnergy.ENERGY || capability == CapabilityEJ.ENERGY)
			return (T) this;
		return super.getCapability(capability, facing);
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		if(!reversed)
			return 0;
		int limit = hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE)) ? 200 : 100;
		int receive = Math.min(Math.min(energyMax - incomeEnergy, limit), maxReceive);
		if(!simulate)
		{
			incomeEnergy += receive;
			if(receive > 0 && world.rand.nextInt(9) == 0)
				HCNetwork.getManager("particles").sendToAllAround(new PacketSpawnGeneratorZap(fromPos(pos), fromPos(pos)), getSyncPoint(64));
		}
		return receive;
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		if(reversed)
			return 0;
		int limit = hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE)) ? 800 : 400;
		int extract = Math.min(Math.min(limit, storedEnergy), maxExtract);
		if(!simulate)
			storedEnergy -= extract;
		return extract;
	}
	
	@Override
	public int getEnergyStored()
	{
		return storedEnergy;
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return energyMax;
	}
	
	@Override
	public boolean canExtract()
	{
		return !reversed;
	}
	
	@Override
	public boolean canReceive()
	{
		return reversed;
	}
	
	@Override
	public int[] getUpgrades()
	{
		return upgrades;
	}
	
	@Override
	public boolean canAcceptUpgrade(int upgrade)
	{
		if(upgrade == ItemUpgrade.idFromItem(ItemsLT.CONCENTRATED_EVIL) && hasUpgrade(ItemUpgrade.idFromItem(ItemsLT.INFINITE_SADNESS)))
			return true;
		if(upgrade != ItemUpgrade.idFromItem(ItemsLT.QUICKSILVER_CORE) && upgrade != ItemUpgrade.idFromItem(ItemsLT.STABILIZED_SINGULARITY) && upgrade != ItemUpgrade.idFromItem(ItemsLT.CONTAINED_EMPTINESS) && upgrade != ItemUpgrade.idFromItem(ItemsLT.INFINITE_SADNESS))
			return false;
		if(hasUpgrade(upgrade))
			return false;
		return true;
	}
	
	@Override
	public void onUpgradeInstalled(int id, int slot)
	{
		if(id == ItemUpgrade.idFromItem(ItemsLT.INFINITE_SADNESS))
		{
			EntityPlayer player = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 8, false);
			if(!world.isRemote)
				player.sendMessage(new TextComponentTranslation("chat." + Info.MOD_ID + ":generator_sadness"));
		}
	}
}