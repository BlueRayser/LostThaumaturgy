package com.pengu.lostthaumaturgy.custom.research;

import java.security.SecureRandom;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;

import com.mrdimka.hammercore.HammerCore;
import com.pengu.lostthaumaturgy.block.BlockOreCrystal.Getter;
import com.pengu.lostthaumaturgy.custom.research.client.ResearchPageHandler;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.init.ItemsLT;

public class Research
{
	protected final SecureRandom random = new SecureRandom();
	public float failChance;
	public final String uid;
	protected int color;
	
	public final String category;
	public ResearchPageHandler pageHandler;
	
	public static final String //
	        CATEGORY_BASICS = "basics", //
	        CATEGORY_THAUMATURGY = "thaumaturgy", //
	        CATEGORY_LOST_KNOWLEDGE = "lost_knowledge", //
	        CATEGORY_ELDRITCH = "eldritch", //
	        CATEGORY_FORBIDDEN = "forbidden", //
	        CATEGORY_UNDEFINED = "undefined";
	
	public Research(String uid, float failChance)
	{
		this(uid, failChance, "undefined");
	}
	
	public Research(String uid, float failChance, String category)
	{
		this.uid = uid;
		this.failChance = failChance;
		color = uid.hashCode();
		this.category = category;
		pageHandler = new ResearchPageHandler(this);
	}
	
	public Research setIcon(ItemStack stack)
	{
		if(HammerCore.pipelineProxy.pipeIfOnGameSide(this, Side.CLIENT) != null)
			getPageHandler().thaumonomiconIcon = new Getter(stack);
		return this;
	}
	
	public ResearchPageHandler getPageHandler()
	{
		return HammerCore.pipelineProxy.pipeIfOnGameSide(pageHandler, Side.CLIENT);
	}
	
	public String sucessToString()
	{
		if(failChance <= 10)
			return "Easy";
		if(failChance <= 30)
			return "Moderate";
		if(failChance <= 50)
			return "Hard";
		if(failChance <= 75)
			return "Tricky";
		if(failChance <= 99)
			return "Tortuous";
		if(failChance <= 100)
			return "Almost Impossible";
		return "Impossible";
	}
	
	public int getColor()
	{
		return color;
	}
	
	public Research setColor(int color)
	{
		this.color = color;
		return this;
	}
	
	protected ItemStack researchStack = ItemStack.EMPTY;
	
	public Research setRequiredObtainStack(ItemStack stack)
	{
		researchStack = stack;
		return this;
	}
	
	public final boolean isCompleted(EntityPlayer player)
	{
		return ResearchSystem.isResearchCompleted(player, this);
	}
	
	public String getTitle()
	{
		return I18n.translateToLocal("research." + uid + ".title");
	}
	
	public String getDesc()
	{
		return I18n.translateToLocal("research." + uid + ".desc");
	}
	
	/**
	 * Used for special researches like Brain-in-a-jar to only be researchable
	 * via Zombie Brains
	 */
	public boolean canObtainFrom(ItemStack baseStack, EntityPlayer initiator)
	{
		if(!researchStack.isEmpty() && !baseStack.equals(researchStack))
			return false;
		return true;
	}
}