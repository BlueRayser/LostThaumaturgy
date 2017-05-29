package com.pengu.lostthaumaturgy.net;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.mrdimka.hammercore.common.utils.IOUtils;
import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.api.RecipesCrucible;
import com.pengu.lostthaumaturgy.init.CrucibleLT;

public class PacketReloadCR implements IPacket, IPacketListener<PacketReloadCR, IPacket>
{
	@Override
	public IPacket onArrived(PacketReloadCR packet, MessageContext context)
	{
		LostThaumaturgy.LOG.info("Reloading crucible recipes, from server...");
		RecipesCrucible.reloadRecipes();
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("json", new String(IOUtils.pipeOut(LostThaumaturgy.class.getResourceAsStream("/assets/" + LTInfo.MOD_ID + "/crucible.json"))));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		CrucibleLT.json = nbt.getString("json");
	}
}