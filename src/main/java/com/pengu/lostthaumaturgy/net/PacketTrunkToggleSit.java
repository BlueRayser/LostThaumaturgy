package com.pengu.lostthaumaturgy.net;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.entity.EntityTravelingTrunk;

public class PacketTrunkToggleSit implements IPacket, IPacketListener<PacketTrunkToggleSit, IPacket>
{
	public UUID uuid;
	
	public PacketTrunkToggleSit(UUID trunk)
	{
		this.uuid = trunk;
	}
	
	public PacketTrunkToggleSit()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setUniqueId("Trunk", uuid);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		uuid = nbt.getUniqueId("Trunk");
	}
	
	@Override
	public IPacket onArrived(PacketTrunkToggleSit packet, MessageContext context)
	{
		if(context.side == Side.SERVER)
		{
			UUID player = context.getServerHandler().player.getGameProfile().getId();
			EntityTravelingTrunk e = WorldUtil.cast(context.getServerHandler().player.getServer().getEntityFromUuid(packet.uuid), EntityTravelingTrunk.class);
			if(e != null)
				if(player.equals(e.owner))
				{
					e.stay = !e.stay;
					context.getServerHandler().player.sendMessage(new TextComponentTranslation("chat." + LTInfo.MOD_ID + ":trunk_" + (e.stay ? "stay" : "follow")));
					return new PacketSyncTrunk(e);
				} else
					context.getServerHandler().player.sendMessage(new TextComponentTranslation("chat." + LTInfo.MOD_ID + ":not_my_master"));
		}
		return null;
	}
}