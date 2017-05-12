package com.pengu.lostthaumaturgy.proxy;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.pengu.hammercore.client.render.item.ItemRenderingHandler;
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
import com.pengu.lostthaumaturgy.client.render.tesr.TESRBellows;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRConduit;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRCrucible;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRCrystal;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRInfuser;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRPressurizedConduit;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRReinforcedVisTank;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRSilverwoodVisTank;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisFilter;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisPump;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisTank;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisValve;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.entity.EntitySmartZombie;
import com.pengu.lostthaumaturgy.entity.EntityThaumSlime;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.items.ItemGogglesRevealing;
import com.pengu.lostthaumaturgy.tile.TileAdvancedVisValve;
import com.pengu.lostthaumaturgy.tile.TileBellows;
import com.pengu.lostthaumaturgy.tile.TileConduit;
import com.pengu.lostthaumaturgy.tile.TileCrucible;
import com.pengu.lostthaumaturgy.tile.TileCrystalOre;
import com.pengu.lostthaumaturgy.tile.TileInfuser;
import com.pengu.lostthaumaturgy.tile.TilePressurizedConduit;
import com.pengu.lostthaumaturgy.tile.TileReinforcedVisTank;
import com.pengu.lostthaumaturgy.tile.TileSilverwoodVisTank;
import com.pengu.lostthaumaturgy.tile.TileVisFilter;
import com.pengu.lostthaumaturgy.tile.TileVisPump;
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
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileCrucible.class, TESRCrucible.INSTANCE);
		ItemRenderingHandler.INSTANCE.bindItemRender(Item.getItemFromBlock(BlocksLT.CRUCIBLE), TESRCrucible.INSTANCE);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileConduit.class, TESRConduit.INSTANCE);
		ItemRenderingHandler.INSTANCE.bindItemRender(Item.getItemFromBlock(BlocksLT.CONDUIT), TESRConduit.INSTANCE);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TilePressurizedConduit.class, TESRPressurizedConduit.INSTANCE);
		ItemRenderingHandler.INSTANCE.bindItemRender(Item.getItemFromBlock(BlocksLT.PRESSURIZED_CONDUIT), TESRPressurizedConduit.INSTANCE);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileVisTank.class, TESRVisTank.INSTANCE);
		ItemRenderingHandler.INSTANCE.bindItemRender(Item.getItemFromBlock(BlocksLT.VIS_TANK), TESRVisTank.INSTANCE);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileReinforcedVisTank.class, TESRReinforcedVisTank.INSTANCE);
		ItemRenderingHandler.INSTANCE.bindItemRender(Item.getItemFromBlock(BlocksLT.VIS_TANK_REINFORCED), TESRReinforcedVisTank.INSTANCE);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileSilverwoodVisTank.class, TESRSilverwoodVisTank.INSTANCE);
		ItemRenderingHandler.INSTANCE.bindItemRender(Item.getItemFromBlock(BlocksLT.VIS_TANK_SILVERWOOD), TESRSilverwoodVisTank.INSTANCE);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileVisPump.class, TESRVisPump.INSTANCE);
		ItemRenderingHandler.INSTANCE.bindItemRender(Item.getItemFromBlock(BlocksLT.VIS_PUMP), TESRVisPump.INSTANCE);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileInfuser.class, TESRInfuser.INSTANCE);
		ItemRenderingHandler.INSTANCE.bindItemRender(Item.getItemFromBlock(BlocksLT.INFUSER), TESRInfuser.INSTANCE);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileVisFilter.class, TESRVisFilter.INSTANCE);
		ItemRenderingHandler.INSTANCE.bindItemRender(Item.getItemFromBlock(BlocksLT.VIS_FILTER), TESRVisFilter.INSTANCE);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileVisValve.class, TESRVisValve.INSTANCE);
		ItemRenderingHandler.INSTANCE.bindItemRender(Item.getItemFromBlock(BlocksLT.VIS_VALVE), TESRVisValve.INSTANCE);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileAdvancedVisValve.class, TESRAdvancedVisValve.INSTANCE);
		ItemRenderingHandler.INSTANCE.bindItemRender(Item.getItemFromBlock(BlocksLT.ADVANCED_VIS_VALVE), TESRAdvancedVisValve.INSTANCE);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileBellows.class, TESRBellows.INSTANCE);
		ItemRenderingHandler.INSTANCE.bindItemRender(Item.getItemFromBlock(BlocksLT.BELLOWS), TESRBellows.INSTANCE);
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