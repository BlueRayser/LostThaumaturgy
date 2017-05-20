package com.pengu.lostthaumaturgy.proxy;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.bookAPI.Book;
import com.mrdimka.hammercore.bookAPI.BookCategory;
import com.mrdimka.hammercore.bookAPI.BookEntry;
import com.pengu.hammercore.client.render.item.ItemRenderingHandler;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.lostthaumaturgy.api.items.IGoggles;
import com.pengu.lostthaumaturgy.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.block.silverwood.BlockSilverwoodLeaves;
import com.pengu.lostthaumaturgy.client.ClientSIAuraChunk;
import com.pengu.lostthaumaturgy.client.HudDetector;
import com.pengu.lostthaumaturgy.client.render.color.ColorBlockOreCrystal;
import com.pengu.lostthaumaturgy.client.render.entity.RenderEntitySmartZombie;
import com.pengu.lostthaumaturgy.client.render.entity.RenderEntityThaumSlime;
import com.pengu.lostthaumaturgy.client.render.item.ColorItemResearch;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRAdvancedVisValve;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRAuxiliumTable;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRBellows;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRConduit;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRCrucible;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRCrystal;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRInfuser;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRLyingItem;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRPressurizedConduit;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRReinforcedVisTank;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRSilverwoodVisTank;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRStudiumTable;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRThaumiumBellows;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisFilter;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisPump;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisPumpThaumium;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisTank;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisValve;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.custom.research.ResearchRegisterEvent;
import com.pengu.lostthaumaturgy.custom.thaumonomicon.BookThaumonomicon;
import com.pengu.lostthaumaturgy.custom.thaumonomicon.CategoryThaumonomicon;
import com.pengu.lostthaumaturgy.custom.thaumonomicon.EntryThaumonomicon;
import com.pengu.lostthaumaturgy.entity.EntitySmartZombie;
import com.pengu.lostthaumaturgy.entity.EntityThaumSlime;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.items.ItemGogglesRevealing;
import com.pengu.lostthaumaturgy.tile.TileAdvancedVisValve;
import com.pengu.lostthaumaturgy.tile.TileAuxiliumTable;
import com.pengu.lostthaumaturgy.tile.TileBellows;
import com.pengu.lostthaumaturgy.tile.TileConduit;
import com.pengu.lostthaumaturgy.tile.TileCrucible;
import com.pengu.lostthaumaturgy.tile.TileCrystalOre;
import com.pengu.lostthaumaturgy.tile.TileInfuser;
import com.pengu.lostthaumaturgy.tile.TileLyingItem;
import com.pengu.lostthaumaturgy.tile.TilePressurizedConduit;
import com.pengu.lostthaumaturgy.tile.TileReinforcedVisTank;
import com.pengu.lostthaumaturgy.tile.TileSilverwoodVisTank;
import com.pengu.lostthaumaturgy.tile.TileStudiumTable;
import com.pengu.lostthaumaturgy.tile.TileThaumiumBellows;
import com.pengu.lostthaumaturgy.tile.TileVisFilter;
import com.pengu.lostthaumaturgy.tile.TileVisPump;
import com.pengu.lostthaumaturgy.tile.TileVisPumpThaumium;
import com.pengu.lostthaumaturgy.tile.TileVisTank;
import com.pengu.lostthaumaturgy.tile.TileVisValve;

public class ClientProxy extends CommonProxy
{
	@Override
	public void updateClientAuraChunk(SIAuraChunk chunk)
	{
		ClientSIAuraChunk.setClientChunk(chunk);
	}
	
	@Override
	public void preInit()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityThaumSlime.class, RenderEntityThaumSlime.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntitySmartZombie.class, RenderEntitySmartZombie.FACTORY);
	}
	
	private static List<Particle> queue = new ArrayList<>();
	
	public static void queueParticle(Particle particle)
	{
		queue.add(particle);
	}
	
	@Override
	public void init()
	{
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ColorItemResearch(), ItemsLT.DISCOVERY);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileCrystalOre.class, TESRCrystal.INSTANCE);
		for(BlockOreCrystal ore : BlockOreCrystal.crystals)
		{
			Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new ColorBlockOreCrystal(), ore);
			ItemRenderingHandler.INSTANCE.bindItemRender(Item.getItemFromBlock(ore), TESRCrystal.INSTANCE);
		}
		
		registerRender(TileCrucible.class, BlocksLT.CRUCIBLE, TESRCrucible.INSTANCE);
		registerRender(TileConduit.class, BlocksLT.CONDUIT, TESRConduit.INSTANCE);
		registerRender(TilePressurizedConduit.class, BlocksLT.PRESSURIZED_CONDUIT, TESRPressurizedConduit.INSTANCE);
		registerRender(TileVisTank.class, BlocksLT.VIS_TANK, TESRVisTank.INSTANCE);
		registerRender(TileReinforcedVisTank.class, BlocksLT.VIS_TANK_REINFORCED, TESRReinforcedVisTank.INSTANCE);
		registerRender(TileSilverwoodVisTank.class, BlocksLT.VIS_TANK_SILVERWOOD, TESRSilverwoodVisTank.INSTANCE);
		registerRender(TileVisPump.class, BlocksLT.VIS_PUMP, TESRVisPump.INSTANCE);
		registerRender(TileVisPumpThaumium.class, BlocksLT.THAUMIUM_VIS_PUMP, TESRVisPumpThaumium.INSTANCE);
		registerRender(TileInfuser.class, BlocksLT.INFUSER, TESRInfuser.INSTANCE);
		registerRender(TileVisFilter.class, BlocksLT.VIS_FILTER, TESRVisFilter.INSTANCE);
		registerRender(TileVisValve.class, BlocksLT.VIS_VALVE, TESRVisValve.INSTANCE);
		registerRender(TileAdvancedVisValve.class, BlocksLT.ADVANCED_VIS_VALVE, TESRAdvancedVisValve.INSTANCE);
		registerRender(TileBellows.class, BlocksLT.BELLOWS, TESRBellows.INSTANCE);
		registerRender(TileThaumiumBellows.class, BlocksLT.THAUMIUM_BELLOWS, TESRThaumiumBellows.INSTANCE);
		registerRender(TileStudiumTable.class, BlocksLT.STUDIUM_TABLE, TESRStudiumTable.INSTANCE);
		registerRender(TileAuxiliumTable.class, BlocksLT.AUXILIUM_TABLE, TESRAuxiliumTable.INSTANCE);
		registerRender(TileLyingItem.class, BlocksLT.LYING_ITEM, TESRLyingItem.INSTANCE);
		
		HammerCore.bookProxy.registerBookInstance(BookThaumonomicon.instance);
	}
	
	@Override
	public Side getProxySide()
	{
		return Side.CLIENT;
	}
	
	private <T extends TileEntity> void registerRender(Class<T> tileEntityClass, Block block, TESR<? super T> specialRenderer)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, specialRenderer);
		ItemRenderingHandler.INSTANCE.bindItemRender(Item.getItemFromBlock(block), specialRenderer);
	}
	
	@Nonnull
	public static TextureAtlasSprite getSprite(String path)
	{
		TextureMap m = Minecraft.getMinecraft().getTextureMapBlocks();
		TextureAtlasSprite s = m.getTextureExtry(path);
		if(s == null)
			s = m.getAtlasSprite(path);
		return s != null ? s : m.getMissingSprite();
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void registerResearch(ResearchRegisterEvent.OnClient evt)
	{
		BookThaumonomicon tm = BookThaumonomicon.instance;
		CategoryThaumonomicon tc = null;
		for(BookCategory cat : tm.categories)
			if(cat instanceof CategoryThaumonomicon && cat.categoryId.equals(evt.research.category))
				tc = (CategoryThaumonomicon) cat;
		if(tc == null) tc = new CategoryThaumonomicon(evt.research.category);
		new EntryThaumonomicon(tc, evt.research.uid, "research." + evt.research.uid + ".title", evt.research);
		evt.research.pageHandler.reload();
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void reloadTextrues(TextureStitchEvent evt)
	{
		BookThaumonomicon tm = BookThaumonomicon.instance;
		for(BookCategory cat : tm.categories)
			for(BookEntry ent : cat.entries)
				if(ent instanceof EntryThaumonomicon)
				{
					EntryThaumonomicon m = (EntryThaumonomicon) ent;
					m.res.getPageHandler().reload();
				}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderHUD(RenderGameOverlayEvent evt)
	{
		((BlockSilverwoodLeaves) BlocksLT.SILVERWOOD_LEAVES).setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
		
		// Unqueue all particle that pend for spawning
		while(!queue.isEmpty())
			Minecraft.getMinecraft().effectRenderer.addEffect(queue.remove(0));
		
		if(evt.getType() == ElementType.ALL)
		{
			IGoggles goggles = ItemGogglesRevealing.getWearing(Minecraft.getMinecraft().player);
			if(goggles != null)
				HudDetector.instance.render(goggles.getRevealType(), ClientSIAuraChunk.getClientChunk(), true);
		}
	}
}