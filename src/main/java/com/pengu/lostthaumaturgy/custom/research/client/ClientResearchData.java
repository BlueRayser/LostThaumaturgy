package com.pengu.lostthaumaturgy.custom.research.client;

import java.util.HashSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

import com.pengu.lostthaumaturgy.custom.research.Research;

public class ClientResearchData
{
	public static final HashSet<String> COMPLETED = new HashSet<>();
	
	public static void fromNBT(NBTTagCompound nbt)
	{
		COMPLETED.clear();
		NBTTagList list = nbt.getTagList("Research", NBT.TAG_STRING);
		for(int i = 0; i < list.tagCount(); ++i)
			COMPLETED.add(list.getStringTagAt(i));
	}
	
	public static boolean isResearchCompleted(Research res)
	{
		return COMPLETED.contains(res.uid);
	}
}