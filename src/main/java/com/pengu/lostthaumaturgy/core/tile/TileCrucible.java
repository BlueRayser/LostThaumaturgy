package com.pengu.lostthaumaturgy.core.tile;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.api.RecipesCrucible;
import com.pengu.lostthaumaturgy.api.tiles.CapabilityVisConnection;
import com.pengu.lostthaumaturgy.api.tiles.ConnectionManager;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;
import com.pengu.lostthaumaturgy.api.tiles.IThaumSlimeDrainable;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.entity.EntityThaumSlime;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereTicker;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp2;

public class TileCrucible extends TileSyncableTickable implements IConnection, IThaumSlimeDrainable
{
	public int smeltDelay;
	public float pureVis = 0;
	public float taintedVis = 0;
	public float maxVis;
	protected float conversion;
	protected float speed;
	public int bellows = 0;
	protected int soundDelay = 25;
	protected float pPure;
	protected float pTaint;
	protected int wait;
	protected boolean updateNextPeriod;
	protected float radMod = 1.2F;
	
	public void readNBT(NBTTagCompound nbt)
	{
		this.pureVis = nbt.getFloat("PureVis");
		this.taintedVis = nbt.getFloat("TaintedVis");
		this.bellows = nbt.getInteger("Bellows");
		setTier(nbt.getFloat("MaxVis"), nbt.getFloat("Conversion"), nbt.getFloat("Speed"));
	}
	
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setFloat("PureVis", this.pureVis);
		nbt.setFloat("TaintedVis", this.taintedVis);
		nbt.setInteger("Bellows", this.bellows);
		nbt.setFloat("MaxVis", maxVis);
		nbt.setFloat("Conversion", conversion);
		nbt.setFloat("Speed", speed);
	}
	
	public void setTier(float maxVis, float conversion, float speed)
	{
		this.maxVis = maxVis;
		this.conversion = conversion;
		this.speed = speed;
		
		// switch(t)
		// {
		// case 4:
		// {
		// this.maxVis = 750.0f;
		// this.conversion = 0.4f;
		// this.speed = 0.75f;
		// this.type = 4;
		// }
		// }
	}
	
	public void tick()
	{
		float totalVis = pureVis + taintedVis;
		--smeltDelay;
		--wait;
		
		if(!world.isRemote && world.rand.nextInt(40) == 0)
			attractSlimeToTile(8, new WorldLocation(world, pos), 3);
		
		if(pPure != pureVis || pTaint != taintedVis)
		{
			pTaint = taintedVis;
			pPure = pureVis;
			updateNextPeriod = true;
		}
		
		if(wait <= 0 && updateNextPeriod)
		{
			sync();
			updateNextPeriod = false;
			wait = 10;
		}
		
		if(pureVis - Math.floor(pureVis) < 0.005F)
			pureVis = (float) Math.floor(pureVis);
		if(taintedVis - Math.floor(taintedVis) < 0.005F)
			taintedVis = (float) Math.floor(taintedVis);
		
		--soundDelay;
		if(soundDelay <= 0)
			soundDelay = 15 + world.rand.nextInt(15);
		if(totalVis > maxVis)
		{
			float overflowSplit = Math.min((pureVis + taintedVis - maxVis) / 2f, 1f);
			if(pureVis >= overflowSplit)
				pureVis -= overflowSplit;
			
			if(overflowSplit >= 1.0f)
			{
				AtmosphereChunk ac = (AtmosphereChunk) AtmosphereTicker.getAuraChunkFromBlockCoords(world, pos);
				if(ac != null && taintedVis >= 1)
				{
					taintedVis -= 1;
					ac.taint++;
					HCNetwork.manager.sendToAllAround(new PacketFXWisp2((float) this.getPos().getX() + this.world.rand.nextFloat(), (float) this.getPos().getY() + 0.8f, (float) this.getPos().getZ() + this.world.rand.nextFloat(), (float) this.getPos().getX() + 0.5f + (this.world.rand.nextFloat() - this.world.rand.nextFloat()), (float) this.getPos().getY() + 3.0f + this.world.rand.nextFloat(), (float) this.getPos().getZ() + 0.5f + (this.world.rand.nextFloat() - this.world.rand.nextFloat()), .5F, 5), getSyncPoint(50));
				}
			}
			
			sync();
		}
		
		if(smeltDelay <= 0)
		{
			smeltDelay = 5;
			List<EntityItem> list = getContents();
			if(list.size() > 0 && !world.isRemote)
			{
				EntityItem entity = list.get(world.rand.nextInt(list.size()));
				ItemStack item = entity.getItem();
				if(canCook(item))
				{
					try
					{
						Field f = EntityItem.class.getDeclaredFields()[2];
						f.setAccessible(true);
						f.setInt(entity, 0);
					} catch(Throwable err)
					{
					}
					
					boolean aboveFurnace = false;
					boolean aboveBoostedFurnace = false;
					// if(this.i.b(this.j, this.k - 1, this.l) instanceof
					// TileArcaneFurnace && ((TileArcaneFurnace)
					// this.i.b(this.j, this.k - 1, this.l)).isBurning())
					// {
					// aboveFurnace = true;
					// if(((TileArcaneFurnace) this.i.b((int) this.j, (int)
					// (this.k - 1), (int) this.l)).boost)
					// {
					// aboveBoostedFurnace = true;
					// }
					// }
					float currentItemCookValue = RecipesCrucible.getSmeltingValue(item);
					float tconv = this.conversion;
					// if(aboveFurnace)
					// {
					// tconv += 0.1f + (float) ((TileArcaneFurnace)
					// this.i.b((int) this.j, (int) (this.k - 1), (int)
					// this.l)).bellows * 0.025f;
					// if(aboveBoostedFurnace)
					// {
					// tconv += 0.1f;
					// }
					// tconv = Math.min(tconv, 1.0f);
					// }
					float pureCook = currentItemCookValue * tconv;
					float taintCook = currentItemCookValue - pureCook;
					if(totalVis <= maxVis)
					{
						pureVis += pureCook;
						taintedVis += taintCook;
						float tspeed = speed + bellows * .1F;
						smeltDelay = 20 + Math.round(currentItemCookValue / (5F * tspeed));
						// if(aboveFurnace)
						// {
						// this.smeltDelay = (int) ((float) this.smeltDelay *
						// (0.8f - (float) ((TileArcaneFurnace) this.i.b((int)
						// this.j, (int) (this.k - 1), (int) this.l)).bellows *
						// 0.05f));
						// }
						
						AtmosphereChunk ac = AtmosphereTicker.getAuraChunkFromBlockCoords(world, pos);
						if(ac != null)
						{
							ac.badVibes = (short) ((float) ac.badVibes + currentItemCookValue / 10F);
							ac.radiation += .0001F * currentItemCookValue * radMod;
						}
						
						item.shrink(1);
						sync();
						HCNetwork.spawnParticle(world, EnumParticleTypes.SMOKE_LARGE, entity.posX, entity.posY, entity.posZ, 0, 0, 0);
						HammerCore.audioProxy.playSoundAt(world, Info.MOD_ID + ":bubbling", pos, .25F, .9F + world.rand.nextFloat() * .2F, SoundCategory.BLOCKS);
					}
				} else
				{
					entity.motionX = (world.rand.nextFloat() - world.rand.nextFloat()) * .2F;
					entity.motionY = .2F + world.rand.nextFloat() * .3F;
					entity.motionZ = (world.rand.nextFloat() - world.rand.nextFloat()) * .2F;
					HammerCore.audioProxy.playSoundAt(world, "entity.item.pickup", pos, .5F, 2F + world.rand.nextFloat() * .45F, SoundCategory.BLOCKS);
					entity.setPickupDelay(10);
					
					try
					{
						Field f = EntityItem.class.getDeclaredFields()[2];
						f.setAccessible(true);
						f.setInt(entity, 0);
					} catch(Throwable err)
					{
					}
				}
			}
		} else
		{
			for(EntityItem entity : getContents())
				if(canCook(entity.getItem()))
				{
					entity.motionX = (world.rand.nextFloat() - world.rand.nextFloat()) * .05F;
					entity.motionY = -.01F;
					entity.motionZ = (world.rand.nextFloat() - world.rand.nextFloat()) * .05F;
					
					entity.setPickupDelay(10);
					
					try
					{
						Field f = EntityItem.class.getDeclaredFields()[2];
						f.setAccessible(true);
						f.setInt(entity, 0);
					} catch(Throwable err)
					{
					}
				}
		}
	}
	
	private List<EntityItem> getContents()
	{
		return world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), (double) pos.getX() + 1, (double) pos.getY() + 1, (double) pos.getZ() + 1));
	}
	
	public boolean ejectContents(EntityPlayer player)
	{
		boolean ret = false;
		List<EntityItem> list = getContents();
		for(int a = 0; a < list.size(); ++a)
		{
			EntityItem ei = list.get(a);
			ei.noClip = true;
			ei.setNoPickupDelay();
			ei.motionX = (player.posX - ei.posX) * .20000000298023224;
			ei.motionY = (player.posY - ei.posY) * .20000000298023224;
			ei.motionZ = (player.posZ - ei.posZ) * .20000000298023224;
			ei.onUpdate();
			ei.noClip = false;
			ret = true;
		}
		return ret;
	}
	
	private boolean canCook(ItemStack items)
	{
		if(items == null || items.isEmpty())
			return false;
		float cookvalue = RecipesCrucible.getSmeltingValue(items);
		return cookvalue > 0;
	}
	
	public boolean isConnected(EnumFacing to)
	{
		BlockPos tpos = pos.offset(to);
		if(world.isBlockLoaded(tpos))
		{
			IConnection c = ConnectionManager.getConnection(loc, to);
			if(c != null)
				return c.getConnectable(to.getOpposite());
		}
		return false;
	}
	
	@Override
	public boolean getConnectable(EnumFacing face)
	{
		return face != EnumFacing.UP;
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
	public float[] subtractVis(float amount)
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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityVisConnection.VIS)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityVisConnection.VIS)
			return (T) this;
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void onDrained(EntityThaumSlime slime)
	{
		boolean t = slime.isTainted();
		if(t)
		{
			if(taintedVis > 0)
			{
				float e = Math.min(taintedVis, .1F);
				taintedVis -= e;
				slime.setTaintedVis(slime.getTaintedVis() + e);
				sync();
			} else if(pureVis > 0)
			{
				float e = Math.min(pureVis, .1F);
				pureVis -= e;
				slime.setPureVis(slime.getPureVis() + e);
				sync();
			}
		} else
		{
			if(pureVis > 0)
			{
				float e = Math.min(pureVis, .1F);
				pureVis -= e;
				slime.setPureVis(slime.getPureVis() + e);
				sync();
			} else if(taintedVis > 0)
			{
				float e = Math.min(taintedVis, .1F);
				taintedVis -= e;
				slime.setTaintedVis(slime.getTaintedVis() + e);
				sync();
			}
		}
	}
}