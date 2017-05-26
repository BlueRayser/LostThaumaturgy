package com.pengu.lostthaumaturgy.custom.research;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ResearchRegisterEvent extends Event
{
	public final Research research;
	
	public ResearchRegisterEvent(Research research)
	{
		this.research = research;
	}
	
	@Cancelable
	public static class OnClient extends ResearchRegisterEvent
	{
		
		public OnClient(Research research)
		{
			super(research);
		}
		
	}
}