package com.pengu.lostthaumaturgy.api.research.client;

import java.util.ArrayList;

import com.pengu.lostthaumaturgy.api.research.ResearchCategories;
import com.pengu.lostthaumaturgy.api.research.ResearchItem;

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
		
		ResearchCategories.researchCategories.values().forEach(cat -> cat.research.values().stream().filter(res -> res.isAutoUnlock()).forEach(res ->
		{
			if(!COMPLETED.contains(res.key))
				COMPLETED.add(res.key);
		}));
	}
	
	public static boolean isResearchCompleted(ResearchItem res)
	{
		return COMPLETED.contains(res.key);
	}
}