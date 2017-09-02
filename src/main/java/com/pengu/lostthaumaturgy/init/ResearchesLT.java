package com.pengu.lostthaumaturgy.init;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.research.ResearchCategories;
import com.pengu.lostthaumaturgy.api.research.ResearchItem;
import com.pengu.lostthaumaturgy.api.research.ResearchPage;

public class ResearchesLT
{
	public static void registerResearches()
	{
		ResearchCategories.registerCategory("basics", new ResourceLocation(LTInfo.MOD_ID, "textures/gui/book/basics_icon.png"), new ResourceLocation(LTInfo.MOD_ID, "textures/gui/book/basics_back.png"));
		ResearchCategories.registerCategory("lost_knowledge", new ResourceLocation(LTInfo.MOD_ID, "textures/gui/book/lost_knowledge_icon.png"), new ResourceLocation(LTInfo.MOD_ID, "textures/gui/book/lost_knowledge_back.png"));
		ResearchCategories.registerCategory("thaumaturgy", new ResourceLocation(LTInfo.MOD_ID, "textures/gui/book/thaumaturgy_icon.png"), new ResourceLocation(LTInfo.MOD_ID, "textures/gui/book/thaumaturgy_back.png"));
		ResearchCategories.registerCategory("tainted", new ResourceLocation(LTInfo.MOD_ID, "textures/gui/book/tainted_icon.png"), new ResourceLocation(LTInfo.MOD_ID, "textures/gui/book/tainted_back.png"));
		ResearchCategories.registerCategory("forbidden", new ResourceLocation(LTInfo.MOD_ID, "textures/gui/book/forbidden_icon.png"), new ResourceLocation(LTInfo.MOD_ID, "textures/gui/book/forbidden_back.png"));
		
		new ResearchItem("atmosphere", "basics", 0, 0, 0, new ResourceLocation(LTInfo.MOD_ID, "textures/gui/book/r_aura.png")).setAutoUnlock().setRound().setPages(new ResearchPage("lt.research_desc.atmosphere")).registerResearch();
		new ResearchItem("researching", "basics", -1, 0, 0, new ResourceLocation(LTInfo.MOD_ID, "textures/gui/book/r_research.png")).setAutoUnlock().setRound().setPages(new ResearchPage("lt.research_desc.researching.1"), new ResearchPage("lt.research_desc.researching.2"), new ResearchPage(new ItemStack(BlocksLT.STUDIUM_TABLE), true), new ResearchPage(new ItemStack(BlocksLT.AUXILIUM_TABLE), true)).registerResearch();
	}
}