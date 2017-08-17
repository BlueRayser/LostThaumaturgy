package com.pengu.lostthaumaturgy.net;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.net.packetAPI.IPacket;
import com.pengu.hammercore.net.packetAPI.IPacketListener;
import com.pengu.lostthaumaturgy.client.gui.GuiTravelingTrunk;
import com.pengu.lostthaumaturgy.entity.EntityTravelingTrunk;

public class PacketSyncTrunk implements IPacket, IPacketListener<PacketSyncTrunk, IPacket>
{
	private UUID uuid;
	private NBTTagCompound nbt;
	private int id;
	
	public PacketSyncTrunk(Entity entity)
	{
		uuid = entity.getUniqueID();
		id = entity.getEntityId();
		nbt = entity.writeToNBT(new NBTTagCompound());
		nbt.removeTag("Pos");
	}
	
	public PacketSyncTrunk()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setUniqueId("p1", uuid);
		nbt.setTag("p2", this.nbt);
		nbt.setInteger("p3", id);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		uuid = nbt.getUniqueId("p1");
		this.nbt = nbt.getCompoundTag("p2");
		id = nbt.getInteger("p3");
	}
	
	@Override
	public IPacket onArrived(PacketSyncTrunk packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.client();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void client()
	{
		Entity e = Minecraft.getMinecraft().world.getEntityByID(id);
		if(e != null)
		{
			NBTTagList nbttaglist = new NBTTagList();
			for(double d0 : new double[] { e.posX, e.posY, e.posZ })
				nbttaglist.appendTag(new NBTTagDouble(d0));
			nbt.setTag("Pos", nbttaglist);
			
			e.readFromNBT(nbt);
		}
		Minecraft.getMinecraft().addScheduledTask(() ->
		{
			if(e instanceof EntityTravelingTrunk)
			{
				GuiTravelingTrunk gui;
				Minecraft.getMinecraft().displayGuiScreen(gui = new GuiTravelingTrunk((EntityTravelingTrunk) e, Minecraft.getMinecraft().player));
				Minecraft.getMinecraft().player.openContainer = gui.inventorySlots;
			}
		});
	}
}