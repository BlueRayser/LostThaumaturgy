package com.pengu.lostthaumaturgy.proxy;

import java.io.IOException;
import java.util.Scanner;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.bookAPI.BookCategory;
import com.mrdimka.hammercore.bookAPI.BookEntry;
import com.pengu.hammercore.client.render.item.ItemRenderingHandler;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.color.Color;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.api.items.IGoggles;
import com.pengu.lostthaumaturgy.api.tiles.IUpgradable;
import com.pengu.lostthaumaturgy.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.block.silverwood.BlockSilverwoodLeaves;
import com.pengu.lostthaumaturgy.client.ClientSIAuraChunk;
import com.pengu.lostthaumaturgy.client.HudDetector;
import com.pengu.lostthaumaturgy.client.render.color.ColorBlockOreCrystal;
import com.pengu.lostthaumaturgy.client.render.entity.RenderCustomSplashPotion;
import com.pengu.lostthaumaturgy.client.render.entity.RenderEntitySmartZombie;
import com.pengu.lostthaumaturgy.client.render.entity.RenderEntityThaumSlime;
import com.pengu.lostthaumaturgy.client.render.entity.RenderSingularity;
import com.pengu.lostthaumaturgy.client.render.entity.RenderTravelingTrunk;
import com.pengu.lostthaumaturgy.client.render.item.ColorItemResearch;
import com.pengu.lostthaumaturgy.client.render.item.RenderItemWandOfItemFreeze;
import com.pengu.lostthaumaturgy.client.render.item.RenderItemWandReversal;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRAdvancedVisValve;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRAuxiliumTable;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRBellows;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRConduit;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRCrucible;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRCrucibleEyes;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRCrucibleThaumium;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRCrystal;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRCrystallizer;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRDuplicator;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRGenerator;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRInfuser;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRInfuserDark;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRLyingItem;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRPenguCobbleGen;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRPressurizedConduit;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRReinforcedVisTank;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRSilverwoodVisTank;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRSingularityJar;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRStudiumTable;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRThaumiumBellows;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisFilter;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisPump;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisPumpThaumium;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisTank;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisValve;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVoidChest;
import com.pengu.lostthaumaturgy.client.render.tesr.monolith.TESRCrystalReceptacle;
import com.pengu.lostthaumaturgy.client.render.tesr.monolith.TESRMonolith;
import com.pengu.lostthaumaturgy.client.render.tesr.monolith.TESRMonolithExtraRoom;
import com.pengu.lostthaumaturgy.client.render.tesr.monolith.TESRMonolithOpener;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.custom.research.ResearchRegisterEvent;
import com.pengu.lostthaumaturgy.custom.thaumonomicon.BookThaumonomicon;
import com.pengu.lostthaumaturgy.custom.thaumonomicon.CategoryThaumonomicon;
import com.pengu.lostthaumaturgy.custom.thaumonomicon.EntryThaumonomicon;
import com.pengu.lostthaumaturgy.entity.EntityCustomSplashPotion;
import com.pengu.lostthaumaturgy.entity.EntitySingularity;
import com.pengu.lostthaumaturgy.entity.EntitySmartZombie;
import com.pengu.lostthaumaturgy.entity.EntityThaumSlime;
import com.pengu.lostthaumaturgy.entity.EntityTravelingTrunk;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.init.ItemsLT;
import com.pengu.lostthaumaturgy.items.ItemAuraDetector;
import com.pengu.lostthaumaturgy.items.ItemGogglesRevealing;
import com.pengu.lostthaumaturgy.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.tile.TileAdvancedVisValve;
import com.pengu.lostthaumaturgy.tile.TileAuxiliumTable;
import com.pengu.lostthaumaturgy.tile.TileBellows;
import com.pengu.lostthaumaturgy.tile.TileConduit;
import com.pengu.lostthaumaturgy.tile.TileCrucible;
import com.pengu.lostthaumaturgy.tile.TileCrucibleEyes;
import com.pengu.lostthaumaturgy.tile.TileCrucibleThaumium;
import com.pengu.lostthaumaturgy.tile.TileCrystalOre;
import com.pengu.lostthaumaturgy.tile.TileCrystallizer;
import com.pengu.lostthaumaturgy.tile.TileDuplicator;
import com.pengu.lostthaumaturgy.tile.TileGenerator;
import com.pengu.lostthaumaturgy.tile.TileInfuser;
import com.pengu.lostthaumaturgy.tile.TileInfuserDark;
import com.pengu.lostthaumaturgy.tile.TileLyingItem;
import com.pengu.lostthaumaturgy.tile.TilePenguCobbleGen;
import com.pengu.lostthaumaturgy.tile.TilePressurizedConduit;
import com.pengu.lostthaumaturgy.tile.TileReinforcedVisTank;
import com.pengu.lostthaumaturgy.tile.TileSilverwoodVisTank;
import com.pengu.lostthaumaturgy.tile.TileSingularityJar;
import com.pengu.lostthaumaturgy.tile.TileStudiumTable;
import com.pengu.lostthaumaturgy.tile.TileThaumiumBellows;
import com.pengu.lostthaumaturgy.tile.TileVisFilter;
import com.pengu.lostthaumaturgy.tile.TileVisPump;
import com.pengu.lostthaumaturgy.tile.TileVisPumpThaumium;
import com.pengu.lostthaumaturgy.tile.TileVisTank;
import com.pengu.lostthaumaturgy.tile.TileVisValve;
import com.pengu.lostthaumaturgy.tile.TileVoidChest;
import com.pengu.lostthaumaturgy.tile.monolith.TileCrystalReceptacle;
import com.pengu.lostthaumaturgy.tile.monolith.TileExtraRoom;
import com.pengu.lostthaumaturgy.tile.monolith.TileMonolith;
import com.pengu.lostthaumaturgy.tile.monolith.TileMonolithOpener;

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
		RenderingRegistry.registerEntityRenderingHandler(EntityCustomSplashPotion.class, RenderCustomSplashPotion.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityTravelingTrunk.class, RenderTravelingTrunk.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntitySingularity.class, RenderSingularity.FACTORY);
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
		registerRender(TileCrucibleEyes.class, BlocksLT.CRUCIBLE_EYES, TESRCrucibleEyes.INSTANCE);
		registerRender(TileCrucibleThaumium.class, BlocksLT.CRUCIBLE_THAUMIUM, TESRCrucibleThaumium.INSTANCE);
		registerRender(TileConduit.class, BlocksLT.CONDUIT, TESRConduit.INSTANCE);
		registerRender(TilePressurizedConduit.class, BlocksLT.PRESSURIZED_CONDUIT, TESRPressurizedConduit.INSTANCE);
		registerRender(TileVisTank.class, BlocksLT.VIS_TANK, TESRVisTank.INSTANCE);
		registerRender(TileReinforcedVisTank.class, BlocksLT.VIS_TANK_REINFORCED, TESRReinforcedVisTank.INSTANCE);
		registerRender(TileSilverwoodVisTank.class, BlocksLT.VIS_TANK_SILVERWOOD, TESRSilverwoodVisTank.INSTANCE);
		registerRender(TileVisPump.class, BlocksLT.VIS_PUMP, TESRVisPump.INSTANCE);
		registerRender(TileVisPumpThaumium.class, BlocksLT.THAUMIUM_VIS_PUMP, TESRVisPumpThaumium.INSTANCE);
		registerRender(TileInfuser.class, BlocksLT.INFUSER, TESRInfuser.INSTANCE);
		registerRender(TileInfuserDark.class, BlocksLT.INFUSER_DARK, TESRInfuserDark.INSTANCE);
		registerRender(TileVisFilter.class, BlocksLT.VIS_FILTER, TESRVisFilter.INSTANCE);
		registerRender(TileVisValve.class, BlocksLT.VIS_VALVE, TESRVisValve.INSTANCE);
		registerRender(TileAdvancedVisValve.class, BlocksLT.ADVANCED_VIS_VALVE, TESRAdvancedVisValve.INSTANCE);
		registerRender(TileBellows.class, BlocksLT.BELLOWS, TESRBellows.INSTANCE);
		registerRender(TileThaumiumBellows.class, BlocksLT.THAUMIUM_BELLOWS, TESRThaumiumBellows.INSTANCE);
		registerRender(TileStudiumTable.class, BlocksLT.STUDIUM_TABLE, TESRStudiumTable.INSTANCE);
		registerRender(TileAuxiliumTable.class, BlocksLT.AUXILIUM_TABLE, TESRAuxiliumTable.INSTANCE);
		registerRender(TileLyingItem.class, BlocksLT.LYING_ITEM, TESRLyingItem.INSTANCE);
		registerRender(TileCrystallizer.class, BlocksLT.CRYSTALLIZER, TESRCrystallizer.INSTANCE);
		registerRender(TilePenguCobbleGen.class, BlocksLT.PENGU_COBBLEGEN, TESRPenguCobbleGen.INSTANCE);
		registerRender(TileGenerator.class, BlocksLT.GENERATOR, TESRGenerator.INSTANCE);
		registerRender(TileSingularityJar.class, BlocksLT.SINGULARITY_JAR, TESRSingularityJar.INSTANCE);
		registerRender(TileDuplicator.class, BlocksLT.DUPLICATOR, TESRDuplicator.INSTANCE);
		registerRender(TileMonolith.class, BlocksLT.MONOLITH, TESRMonolith.INSTANCE);
		registerRender(TileCrystalReceptacle.class, BlocksLT.MONOLITH_CRYSTAL_RECEPTACLE, TESRCrystalReceptacle.INSTANCE);
		registerRender(TileMonolithOpener.class, BlocksLT.MONOLITH_OPENER, TESRMonolithOpener.INSTANCE);
		registerRender(TileExtraRoom.class, BlocksLT.MONOLITH_EXTRA_ROOM, TESRMonolithExtraRoom.INSTANCE);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileVoidChest.class, TESRVoidChest.INSTANCE);
		
		ItemRenderingHandler.INSTANCE.bindItemRender(ItemsLT.WAND_ITEM_FREEZE, new RenderItemWandOfItemFreeze());
		ItemRenderingHandler.INSTANCE.bindItemRender(ItemsLT.WAND_REVERSAL, new RenderItemWandReversal());
		
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
		if(tc == null)
			tc = new CategoryThaumonomicon(evt.research.category);
		new EntryThaumonomicon(tc, evt.research.uid, "research." + evt.research.uid + ".title", evt.research);
		evt.research.pageHandler.reload();
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void reloadTextrues(TextureStitchEvent evt)
	{
		try
		{
			int sprite = 0;
			LostThaumaturgy.LOG.info("Loadeding sprites...");
			Scanner s = new Scanner(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(LTInfo.MOD_ID, "textures/blocks/_sprites.txt")).getInputStream());
			while(s.hasNextLine())
			{
				String ln = s.nextLine();
				if(ln.isEmpty())
					continue;
				evt.getMap().registerSprite(new ResourceLocation(LTInfo.MOD_ID, "blocks/" + ln));
				sprite++;
			}
			s.close();
			LostThaumaturgy.LOG.info("Loaded " + sprite + " sprites!");
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		
		BookThaumonomicon tm = BookThaumonomicon.instance;
		for(BookCategory cat : tm.categories)
			for(BookEntry ent : cat.entries)
				if(ent instanceof EntryThaumonomicon)
				{
					EntryThaumonomicon m = (EntryThaumonomicon) ent;
					m.res.getPageHandler().reload();
				}
	}
	
	@SubscribeEvent
	public void clientTick(ClientTickEvent evt)
	{
		if(Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().world.getTotalWorldTime() % 40 == 0)
		{
			ItemAuraDetector.type = -1;
			
			for(ItemStack stack : Minecraft.getMinecraft().player.inventory.mainInventory)
			{
				if(stack.getItem() == ItemsLT.AURA_DETECTOR)
				{
					int type = stack.getItemDamage();
					if(type == -1)
					{
						int dmg = stack.getItemDamage();
						type = dmg;
					} else if(type != 2 && (stack.getItemDamage() == 2 || stack.getItemDamage() + type == 1))
						type = 2;
				}
			}
		}
	}
	
	private ItemStack[] handStacks = new ItemStack[2];
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderHUD(RenderGameOverlayEvent evt)
	{
		((BlockSilverwoodLeaves) BlocksLT.SILVERWOOD_LEAVES).setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
		Minecraft mc = Minecraft.getMinecraft();
		
		if(evt.getType() == ElementType.ALL)
		{
			IGoggles goggles = ItemGogglesRevealing.getWearing(mc.player);
			if(goggles != null)
				HudDetector.instance.render(goggles.getRevealType(), ClientSIAuraChunk.getClientChunk(), true);
			else
			{
				int type = ItemAuraDetector.type;
				if(type >= 0)
					HudDetector.instance.render(type, ClientSIAuraChunk.getClientChunk(), false);
			}
			
			ScaledResolution sr = new ScaledResolution(mc);
			int k = sr.getScaledWidth();
			int l = sr.getScaledHeight();
			
			handStacks[0] = mc.player.getHeldItem(EnumHand.MAIN_HAND);
			handStacks[1] = mc.player.getHeldItem(EnumHand.OFF_HAND);
			
			for(ItemStack item : handStacks)
			{
				RayTraceResult res = mc.objectMouseOver;
				if(!item.isEmpty() && item.getItem() == ItemsLT.WAND_REVERSAL && res != null && res.typeOfHit == Type.BLOCK && mc.world.getTileEntity(res.getBlockPos()) instanceof IUpgradable)
				{
					IUpgradable tet = (IUpgradable) mc.world.getTileEntity(res.getBlockPos());
					
					int last = -1;
					int[] u = tet.getUpgrades();
					for(int i = 0; i < u.length; ++i)
						if(u[i] != -1)
							last = i;
					
					if(last != -1 && u[last] != -1)
					{
						ItemUpgrade iu = ItemUpgrade.byId(u[last]);
						ItemStack stack = new ItemStack(iu);
						mc.getRenderItem().renderItemAndEffectIntoGUI(stack, k / 2 - 8, l / 2 - 20);
					}
				}
				
				if(!item.isEmpty() && item.getItem() instanceof ItemUpgrade && res != null && res.typeOfHit == Type.BLOCK && mc.world.getTileEntity(res.getBlockPos()) instanceof IUpgradable)
				{
					IUpgradable tet = (IUpgradable) mc.world.getTileEntity(res.getBlockPos());
					
					int id = ItemUpgrade.idFromItem((ItemUpgrade) item.getItem());
					
					boolean valid = tet.canAcceptUpgrade(id);
					String s = I18n.translateToLocal("gui." + LTInfo.MOD_ID + ":" + (valid ? "" : "not_") + "upgradable");
					mc.fontRenderer.drawString(s, (k - mc.fontRenderer.getStringWidth(s)) / 2, l / 2 - 30, valid ? 0x22FF33 : 0xFF2233);
					Color.glColourRGBA(0xFFFFFFFF);
				}
			}
		}
	}
}