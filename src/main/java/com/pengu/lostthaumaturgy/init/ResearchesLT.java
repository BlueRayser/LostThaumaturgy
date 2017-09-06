package com.pengu.lostthaumaturgy.init;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.pengu.lostthaumaturgy.api.research.ResearchCategories;
import com.pengu.lostthaumaturgy.api.research.ResearchItem;
import com.pengu.lostthaumaturgy.api.research.ResearchPage;
import com.pengu.lostthaumaturgy.core.Info;

public class ResearchesLT
{
	public static void registerResearches()
	{
		ResearchCategories.registerCategory("basics", new ResourceLocation(Info.MOD_ID, "textures/gui/book/basics_icon.png"), new ResourceLocation(Info.MOD_ID, "textures/gui/book/basics_back.png"));
		ResearchCategories.registerCategory("lost_knowledge", new ResourceLocation(Info.MOD_ID, "textures/gui/book/lost_knowledge_icon.png"), new ResourceLocation(Info.MOD_ID, "textures/gui/book/lost_knowledge_back.png"));
		ResearchCategories.registerCategory("thaumaturgy", new ResourceLocation(Info.MOD_ID, "textures/gui/book/thaumaturgy_icon.png"), new ResourceLocation(Info.MOD_ID, "textures/gui/book/thaumaturgy_back.png"));
		ResearchCategories.registerCategory("tainted", new ResourceLocation(Info.MOD_ID, "textures/gui/book/tainted_icon.png"), new ResourceLocation(Info.MOD_ID, "textures/gui/book/tainted_back.png"));
		ResearchCategories.registerCategory("forbidden", new ResourceLocation(Info.MOD_ID, "textures/gui/book/forbidden_icon.png"), new ResourceLocation(Info.MOD_ID, "textures/gui/book/forbidden_back.png"));
		ResearchCategories.registerCategory("eldritch", new ResourceLocation(Info.MOD_ID, "textures/gui/book/eldritch_icon.png"), new ResourceLocation(Info.MOD_ID, "textures/gui/book/eldritch_back.png"));
		ResearchCategories.registerCategory("primordial", new ResourceLocation(Info.MOD_ID, "textures/gui/book/primordial_icon.png"), new ResourceLocation(Info.MOD_ID, "textures/gui/book/primordial_back.png"));
		
		new ResearchItem("atmosphere", "basics", 0, -1, 0, new ResourceLocation(Info.MOD_ID, "textures/gui/book/r_aura.png")).setAutoUnlock().setRound().setPages(new ResearchPage("lt.research_desc.atmosphere")).registerResearch();
		new ResearchItem("researching", "basics", -1, -1, 0, new ResourceLocation(Info.MOD_ID, "textures/gui/book/r_research.png")).setAutoUnlock().setRound().setPages(new ResearchPage("lt.research_desc.researching.1"), new ResearchPage("lt.research_desc.researching.2"), new ResearchPage(new ItemStack(BlocksLT.STUDIUM_TABLE), true), new ResearchPage(new ItemStack(BlocksLT.AUXILIUM_TABLE), true)).registerResearch();
//		new ResearchItem("minerals", "basics", -2, -1, 0, new ItemStack(BlocksLT.CINNABAR_ORE)).setAutoUnlock().setRound().setPages(new ResearchPage("lt.research_desc.minerals")).registerResearch();
		
		new ResearchItem("crucible", "basics", 1, 1, 0, new ItemStack(BlocksLT.CRUCIBLE)).setPages(new ResearchPage("lt.research_desc.crucible"), new ResearchPage(new ItemStack(BlocksLT.CRUCIBLE), true)).setAutoUnlock().setSpecial().registerResearch();
		new ResearchItem("vis_conduit", "basics", 3, 0, 0, new ItemStack(BlocksLT.CONDUIT)).setPages(new ResearchPage("lt.research_desc.vis_conduit"), new ResearchPage(new ItemStack(BlocksLT.CONDUIT), true)).setParents("crucible").setAutoUnlock().setRound().registerResearch();
		new ResearchItem("vis_tank", "basics", 2, -1, 0, new ItemStack(BlocksLT.VIS_TANK)).setPages(new ResearchPage("lt.research_desc.vis_tank"), new ResearchPage(new ItemStack(BlocksLT.VIS_TANK), true)).setParents("vis_conduit").setAutoUnlock().setRound().registerResearch();
		new ResearchItem("arc_crafter", "basics", 2, 3, 0, new ItemStack(BlocksLT.FUSER_MB)).setPages(new ResearchPage("lt.research_desc.arc_crafter"), new ResearchPage(Arrays.asList(true, 2, 1, 2, Arrays.asList(new ItemStack(BlocksLT.INFUSER_BASE), new ItemStack(BlocksLT.INFUSER_BASE), new ItemStack(BlocksLT.INFUSER_BASE), new ItemStack(BlocksLT.INFUSER_BASE))))).setParents("infuser").setAutoUnlock().setRound().registerResearch();
		
		new ResearchItem("crystalline_bell", "basics", 1, 3, 75, new ItemStack(ItemsLT.CRYSTALLINE_BELL)).setColor(0xAFFFFF).setPages(new ResearchPage("lt.research_desc.crystalline_bell"), new ResearchPage(null, new ItemStack(ItemsLT.CRYSTALLINE_BELL))).registerResearch();
		
		new ResearchItem("singularity", "thaumaturgy", 1, 3, 35, new ItemStack(ItemsLT.SINGULARITY)).setPages(new ResearchPage("lt.research_desc.singularity"), new ResearchPage(null, new ItemStack(ItemsLT.SINGULARITY))).registerResearch();
		new ResearchItem("wand_reversal", "thaumaturgy", 3, 3, 45, new ItemStack(ItemsLT.WAND_REVERSAL)).setColor(0x7298B3).setParents("singularity").setPages(new ResearchPage("lt.research_desc.wand_reversal"), new ResearchPage(null, new ItemStack(ItemsLT.WAND_REVERSAL))).registerResearch();
		new ResearchItem("quicksilver_core", "thaumaturgy", -1, -5, 35, new ItemStack(ItemsLT.QUICKSILVER_CORE)).setColor(0xBEBDEA).setPages(new ResearchPage("lt.research_desc.quicksilver_core"), new ResearchPage(null, new ItemStack(ItemsLT.QUICKSILVER_CORE))).registerResearch();
		new ResearchItem("stabilized_singularity", "thaumaturgy", 1, -5, 35, new ItemStack(ItemsLT.STABILIZED_SINGULARITY)).setParents("singularity").setPages(new ResearchPage("lt.research_desc.stabilized_singularity"), new ResearchPage(null, new ItemStack(ItemsLT.STABILIZED_SINGULARITY))).registerResearch();
		
		new ResearchItem("crucible_eyes", "thaumaturgy", -5, 4, 65, new ItemStack(BlocksLT.CRUCIBLE_EYES)).setPages(new ResearchPage("lt.research_desc.crucible_eyes"), new ResearchPage(null, new ItemStack(BlocksLT.CRUCIBLE_EYES))).registerResearch();
		new ResearchItem("crucible_thaumium", "thaumaturgy", -5, 5, 66.6F, new ItemStack(BlocksLT.CRUCIBLE_THAUMIUM)).setParents("crucible_eyes").setPages(new ResearchPage("lt.research_desc.crucible_thaumium"), new ResearchPage(null, new ItemStack(BlocksLT.CRUCIBLE_THAUMIUM))).registerResearch();
		new ResearchItem("crucible_void", "thaumaturgy", -5, 6, 70F, new ItemStack(BlocksLT.CRUCIBLE_VOID)).setParents("crucible_thaumium").setHidden().setPages(new ResearchPage("lt.research_desc.crucible_void"), new ResearchPage(null, new ItemStack(BlocksLT.CRUCIBLE_VOID))).registerResearch();
		
		new ResearchItem("totem_twilight", "tainted", 1, 3, 40, new ItemStack(BlocksLT.TOTEM_TWILIGHT)).setPages(new ResearchPage("lt.research_desc.totem_twilight"), new ResearchPage(null, new ItemStack(BlocksLT.TOTEM_TWILIGHT))).registerResearch();
		new ResearchItem("totem_dawn", "tainted", 3, 3, 40, new ItemStack(BlocksLT.TOTEM_DAWN)).setPages(new ResearchPage("lt.research_desc.totem_dawn"), new ResearchPage(null, new ItemStack(BlocksLT.TOTEM_DAWN))).registerResearch();
	}
}