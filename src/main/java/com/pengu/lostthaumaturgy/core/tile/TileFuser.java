package com.pengu.lostthaumaturgy.core.tile;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.NameFormat;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.ArrayUtils;

import com.pengu.hammercore.common.utils.ChatUtil;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.utils.NetPropertyVec3i;
import com.pengu.hammercore.tile.ITileDroppable;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.api.fuser.FuserInventory;
import com.pengu.lostthaumaturgy.api.fuser.IFuserRecipe;
import com.pengu.lostthaumaturgy.client.gui.GuiFuser;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.inventory.ContainerFuser;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp2;

public class TileFuser extends TileSyncableTickable implements ITileDroppable
{
	public EntityPlayer craftingPlayer;
	public final FuserInventory inventory = new FuserInventory();
	public final NetPropertyVec3i<BlockPos> bound;
	public TileFuser gui;
	
	{
		bound = new NetPropertyVec3i<BlockPos>(this, pos);
	}
	
	@Override
	public void addProperties(Map<String, Object> properties, RayTraceResult trace)
	{
		EnumFacing face = EnumFacing.NORTH;
		
		if(gui != null)
		{
			Vec3i vec = pos.subtract(gui.getPos());
			
			face = EnumFacing.getFacingFromVector(vec.getX(), 0, vec.getZ());
			
			if(vec.getX() == 1 && vec.getZ() == 0)
				face = EnumFacing.NORTH;
			
			if(vec.getX() == 0 && vec.getZ() == 1)
				face = EnumFacing.SOUTH;
			
			if(vec.getX() == 0 && vec.getZ() == 0)
				face = EnumFacing.WEST;
			
			if(vec.getX() == 1 && vec.getZ() == 1)
				face = EnumFacing.EAST;
		}
		
		properties.put("part", face.getName());
	}
	
	@Override
	public boolean hasGui()
	{
		return gui != null;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiFuser(gui, player.inventory);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		b: if(gui != null && gui.craftingPlayer != null)
		{
			if(gui.craftingPlayer == player)
				break b;
			NameFormat evt = new NameFormat(gui.craftingPlayer, gui.craftingPlayer.getGameProfile().getName());
			MinecraftForge.EVENT_BUS.post(evt);
			ChatUtil.sendNoSpam(player, evt.getDisplayname() + " is already using this Arcane Crafter!");
			return null;
		}
		return new ContainerFuser(gui, player.inventory);
	}
	
	@Override
	public void tick()
	{
		if(craftingPlayer != null)
		{
			Container c = craftingPlayer.openContainer;
			if(!(c instanceof ContainerFuser))
				craftingPlayer = null;
		}
		
		WorldLocation l = inventory.location;
		if(l == null)
			inventory.location = l = new WorldLocation(world, pos);
		if(l != null && !l.getPos().equals(pos))
			inventory.location = l = new WorldLocation(world, pos);
		
		BlockPos b = bound.get() != null ? new BlockPos(bound.get()) : null;
		if(b != null && world.getTileEntity(b) instanceof TileFuser)
			gui = (TileFuser) world.getTileEntity(b);
		else
			world.setBlockState(this.pos, BlocksLT.INFUSER_BASE.getDefaultState());
		
		if(gui == this)
		{
			for(int x = 0; x < 2; ++x)
				for(int z = 0; z < 2; ++z)
				{
					BlockPos pos = this.pos.add(x, 0, z);
					TileFuser fuser = WorldUtil.cast(world.getTileEntity(pos), TileFuser.class);
					if(fuser == null)
					{
						world.setBlockState(this.pos, BlocksLT.INFUSER_BASE.getDefaultState());
						break;
					}
				}
			
			IFuserRecipe recipe = craftingPlayer != null ? inventory.findRecipe(craftingPlayer) : null;
			ItemStack result = recipe != null ? recipe.getCraftResult(inventory, craftingPlayer).copy() : ItemStack.EMPTY;
			if(!OreDictionary.itemMatches(result, inventory.outputInv.getStackInSlot(0), true))
				inventory.outputInv.setInventorySlotContents(0, result);
			
			part: if(!world.isRemote && !inventory.wandInv.getStackInSlot(0).isEmpty())
			{
				double x1 = pos.getX() + 1;
				double y1 = pos.getY() + 1.47;
				double z1 = pos.getZ() + 1;
				
				double x2 = pos.getX() + 1;
				double y2 = pos.getY() + .8;
				double z2 = pos.getZ() + 1;
				
				int[] slots = new int[0];
				int slot = -1;
				for(int i = 0; i < 9; ++i)
					if(!inventory.craftingInv.getStackInSlot(i).isEmpty())
						slots = ArrayUtils.add(slots, i);
				
				if(slots.length == 0)
					break part;
				
				slot = slots[ticksExisted % slots.length];
				
				switch(slot)
				{
				case 0:
					x2 = pos.getX() + .5;
					z2 = pos.getZ() + .5;
				break;
				case 1:
					x2 = pos.getX() + .9;
					z2 = pos.getZ() + .5;
				break;
				case 2:
					x2 = pos.getX() + 1.5;
					z2 = pos.getZ() + .5;
				break;
				case 3:
					x2 = pos.getX() + .5;
					z2 = pos.getZ() + 1;
				break;
				case 4:
					x2 = pos.getX() + .9;
					z2 = pos.getZ() + 1;
				break;
				case 5:
					x2 = pos.getX() + 1.5;
					z2 = pos.getZ() + 1;
				break;
				
				case 6:
					x2 = pos.getX() + .5;
					z2 = pos.getZ() + 1.5;
				break;
				case 7:
					x2 = pos.getX() + .9;
					z2 = pos.getZ() + 1.5;
				break;
				case 8:
					x2 = pos.getX() + 1.5;
					z2 = pos.getZ() + 1.5;
				break;
				
				default:
				break part;
				}
				
				if(world.rand.nextInt(7) == 0)
					HCNetwork.manager.sendToAllAround(new PacketFXWisp2(x1, y1, z1, x2, y2, z2, .4F, world.rand.nextInt(5)), getSyncPoint(48));
			}
		} else if(gui != null && world.getTileEntity(b) != gui)
		{
			world.setBlockState(this.pos, BlocksLT.INFUSER_BASE.getDefaultState());
			gui.inventory.drop(world, pos);
		}
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setTag("Items", inventory.writeToNBT(new NBTTagCompound()));
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		inventory.readFromNBT(nbt.getCompoundTag("Items"));
	}
	
	@Override
	public void createDrop(EntityPlayer player, World world, BlockPos pos)
	{
		if(gui == this || (bound.get() != null && bound.equals(pos)))
			inventory.drop(world, pos);
	}
}