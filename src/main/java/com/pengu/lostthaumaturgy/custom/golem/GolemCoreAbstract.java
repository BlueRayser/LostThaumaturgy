package com.pengu.lostthaumaturgy.custom.golem;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;

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
	
	protected abstract void writeToNBT(NBTTagCompound nbt);
	protected abstract void readFromNBT(NBTTagCompound nbt);
	
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
	
	public static final DataSerializer<GolemCoreAbstract> SERIALIZER = new DataSerializer<GolemCoreAbstract>()
	{
		public void write(PacketBuffer buf, GolemCoreAbstract value)
		{
			if(value != null)
				buf.writeCompoundTag(value.writeCoreToNBT(new NBTTagCompound()));
		}
		
		public GolemCoreAbstract read(PacketBuffer buf) throws IOException
		{
			return readCoreFromNBT(buf.readCompoundTag());
		}
		
		public DataParameter<GolemCoreAbstract> createKey(int id)
		{
			return new DataParameter(id, this);
		}
	};
}