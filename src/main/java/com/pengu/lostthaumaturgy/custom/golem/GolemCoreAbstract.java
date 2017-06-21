package com.pengu.lostthaumaturgy.custom.golem;

import net.minecraft.nbt.NBTTagCompound;

import com.pengu.lostthaumaturgy.emote.EmoteManager.DefaultEmotes;
import com.pengu.lostthaumaturgy.entity.EntityGolemBase;

public abstract class GolemCoreAbstract
{
	protected EntityGolemBase golem;
	
	public GolemCoreAbstract()
	{
	}
	
	public final void setGolem(EntityGolemBase golem)
	{
		this.golem = golem;
	}
	
	public abstract void updateLogic();
	
	public abstract boolean isActive();
	
	public abstract boolean isThinking();
	
	public String getEmote()
	{
		String type = null;
		if(!isActive())
			type = DefaultEmotes.SLEEPING;
		if(isThinking())
			type = DefaultEmotes.QUESTION;
		if(golem.paused)
			type = DefaultEmotes.PAUSE;
		return type;
	}
	
	public abstract void writeToNBT(NBTTagCompound nbt);
	
	public abstract void readFromNBT(NBTTagCompound nbt);
	
	public final NBTTagCompound writeCoreToNBT(NBTTagCompound nbt)
	{
		writeToNBT(nbt);
		nbt.setString("Class", getClass().getName());
		return nbt;
	}
	
	public static GolemCoreAbstract readCoreFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("Class"))
			try
			{
				GolemCoreAbstract core = (GolemCoreAbstract) Class.forName(nbt.getString("Class")).newInstance();
				core.readFromNBT(nbt);
				return core;
			} catch(Throwable err)
			{
			}
		return null;
	}
}