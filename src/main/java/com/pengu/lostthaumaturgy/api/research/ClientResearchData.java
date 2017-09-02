package com.pengu.lostthaumaturgy.api.research;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class ClientResearchData
{
	public static final ArrayList<String> COMPLETED = new ArrayList<>();
	
	public static void fromNBT(NBTTagCompound nbt)
	{
		COMPLETED.clear();
		NBTTagList list = nbt.getTagList("Research", NBT.TAG_STRING);
		for(int i = 0; i < list.tagCount(); ++i)
			COMPLETED.add(list.getStringTagAt(i));
	}
	
	public static boolean isResearchCompleted(ResearchItem res)
	{
		return COMPLETED.contains(res.key);
	}
}