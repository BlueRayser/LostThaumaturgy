package com.pengu.lostthaumaturgy.proxy;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.pengu.hammercore.client.render.item.ItemRenderingHandler;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.utils.GLImageManager;
import com.pengu.hammercore.color.Color;
import com.pengu.hammercore.math.MathHelper;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.api.items.IGoggles;
import com.pengu.lostthaumaturgy.api.tiles.IUpgradable;
import com.pengu.lostthaumaturgy.api.wand.WandRegistry;
import com.pengu.lostthaumaturgy.client.ClientAtmosphereChunk;
import com.pengu.lostthaumaturgy.client.HudDetector;
import com.pengu.lostthaumaturgy.client.fx.FXEmote;
import com.pengu.lostthaumaturgy.client.gui.GuiThaumonomicon;
import com.pengu.lostthaumaturgy.client.render.color.ColorBlockOreCrystal;
import com.pengu.lostthaumaturgy.client.render.color.ColorItemSeal;
import com.pengu.lostthaumaturgy.client.render.entity.RenderCustomSplashPotion;
import com.pengu.lostthaumaturgy.client.render.entity.RenderEntityGolem;
import com.pengu.lostthaumaturgy.client.render.entity.RenderEntitySmartZombie;
import com.pengu.lostthaumaturgy.client.render.entity.RenderEntityThaumSlime;
import com.pengu.lostthaumaturgy.client.render.entity.RenderEntityWisp;
import com.pengu.lostthaumaturgy.client.render.entity.RenderSingularity;
import com.pengu.lostthaumaturgy.client.render.entity.RenderTravelingTrunk;
import com.pengu.lostthaumaturgy.client.render.item.ColorItemResearch;
import com.pengu.lostthaumaturgy.client.render.item.RenderItemWand;
import com.pengu.lostthaumaturgy.client.render.item.RenderItemWandOfItemFreeze;
import com.pengu.lostthaumaturgy.client.render.item.RenderItemWandReversal;
import com.pengu.lostthaumaturgy.client.render.seal.TESRSeal;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRAdvancedVisValve;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRAuxiliumTable;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRBellows;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRConduit;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRCrucible;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRCrucibleEyes;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRCrucibleThaumium;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRCrucibleVoid;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRCrystal;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRCrystallizer;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRDarknessGenerator;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRDuplicator;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRFuser;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRGenerator;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRInfuser;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRInfuserDark;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRLyingItem;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRPenguCobbleGen;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRPressurizedConduit;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRReinforcedVisTank;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRRepairer;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRSilverwoodVisTank;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRSingularityJar;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRStudiumTable;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRThaumiumBellows;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisCondenser;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisFilter;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisPump;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisPumpThaumium;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisTank;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVisValve;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRVoidChest;
import com.pengu.lostthaumaturgy.client.render.tesr.TESRWandConstructor;
import com.pengu.lostthaumaturgy.client.render.tesr.monolith.TESRCrystalReceptacle;
import com.pengu.lostthaumaturgy.client.render.tesr.monolith.TESRMonolith;
import com.pengu.lostthaumaturgy.client.render.tesr.monolith.TESRMonolithExtraRoom;
import com.pengu.lostthaumaturgy.client.render.tesr.monolith.TESRMonolithOpener;
import com.pengu.lostthaumaturgy.core.Info;
import com.pengu.lostthaumaturgy.core.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.core.block.wood.BlockTaintedLeaves;
import com.pengu.lostthaumaturgy.core.block.wood.greatwood.BlockGreatwoodLeaves;
import com.pengu.lostthaumaturgy.core.block.wood.silverwood.BlockSilverwoodLeaves;
import com.pengu.lostthaumaturgy.core.emote.EmoteData;
import com.pengu.lostthaumaturgy.core.entity.EntityCustomSplashPotion;
import com.pengu.lostthaumaturgy.core.entity.EntityGolemBase;
import com.pengu.lostthaumaturgy.core.entity.EntitySingularity;
import com.pengu.lostthaumaturgy.core.entity.EntitySmartZombie;
import com.pengu.lostthaumaturgy.core.entity.EntityThaumSlime;
import com.pengu.lostthaumaturgy.core.entity.EntityTravelingTrunk;
import com.pengu.lostthaumaturgy.core.entity.EntityWisp;
import com.pengu.lostthaumaturgy.core.events.TooltipEvent;
import com.pengu.lostthaumaturgy.core.items.ItemUpgrade;
import com.pengu.lostthaumaturgy.core.items.armor.helm.ItemGogglesRevealing;
import com.pengu.lostthaumaturgy.core.tile.TileAdvancedVisValve;
import com.pengu.lostthaumaturgy.core.tile.TileAuxiliumTable;
import com.pengu.lostthaumaturgy.core.tile.TileBellows;
import com.pengu.lostthaumaturgy.core.tile.TileConduit;
import com.pengu.lostthaumaturgy.core.tile.TileCrucible;
import com.pengu.lostthaumaturgy.core.tile.TileCrucibleEyes;
import com.pengu.lostthaumaturgy.core.tile.TileCrucibleThaumium;
import com.pengu.lostthaumaturgy.core.tile.TileCrucibleVoid;
import com.pengu.lostthaumaturgy.core.tile.TileCrystalOre;
import com.pengu.lostthaumaturgy.core.tile.TileCrystallizer;
import com.pengu.lostthaumaturgy.core.tile.TileDarknessGenerator;
import com.pengu.lostthaumaturgy.core.tile.TileDuplicator;
import com.pengu.lostthaumaturgy.core.tile.TileFuser;
import com.pengu.lostthaumaturgy.core.tile.TileGenerator;
import com.pengu.lostthaumaturgy.core.tile.TileInfuser;
import com.pengu.lostthaumaturgy.core.tile.TileInfuserDark;
import com.pengu.lostthaumaturgy.core.tile.TileLyingItem;
import com.pengu.lostthaumaturgy.core.tile.TilePenguCobbleGen;
import com.pengu.lostthaumaturgy.core.tile.TilePressurizedConduit;
import com.pengu.lostthaumaturgy.core.tile.TileReinforcedVisTank;
import com.pengu.lostthaumaturgy.core.tile.TileRepairer;
import com.pengu.lostthaumaturgy.core.tile.TileSeal;
import com.pengu.lostthaumaturgy.core.tile.TileSilverwoodConduit;
import com.pengu.lostthaumaturgy.core.tile.TileSilverwoodVisTank;
import com.pengu.lostthaumaturgy.core.tile.TileSingularityJar;
import com.pengu.lostthaumaturgy.core.tile.TileStudiumTable;
import com.pengu.lostthaumaturgy.core.tile.TileThaumiumBellows;
import com.pengu.lostthaumaturgy.core.tile.TileVisCondenser;
import com.pengu.lostthaumaturgy.core.tile.TileVisFilter;
import com.pengu.lostthaumaturgy.core.tile.TileVisPump;
import com.pengu.lostthaumaturgy.core.tile.TileVisPumpThaumium;
import com.pengu.lostthaumaturgy.core.tile.TileVisTank;
import com.pengu.lostthaumaturgy.core.tile.TileVisValve;
import com.pengu.lostthaumaturgy.core.tile.TileVoidChest;
import com.pengu.lostthaumaturgy.core.tile.TileWandConstructor;
import com.pengu.lostthaumaturgy.core.tile.monolith.TileCrystalReceptacle;
import com.pengu.lostthaumaturgy.core.tile.monolith.TileExtraRoom;
import com.pengu.lostthaumaturgy.core.tile.monolith.TileMonolith;
import com.pengu.lostthaumaturgy.core.tile.monolith.TileMonolithOpener;
import com.pengu.lostthaumaturgy.custom.aura.AtmosphereChunk;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.init.ItemsLT;

public class ClientProxy extends CommonProxy
{
	private static int penguSkinId;
	
	@Override
	public void updateClientAuraChunk(AtmosphereChunk chunk)
	{
		ClientAtmosphereChunk.setClientChunk(chunk);
	}
	
	public static void bindPenguSkin()
	{
		GlStateManager.bindTexture(penguSkinId);
	}
	
	@Override
	public void preInit()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityThaumSlime.class, RenderEntityThaumSlime.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntitySmartZombie.class, RenderEntitySmartZombie.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityCustomSplashPotion.class, RenderCustomSplashPotion.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityTravelingTrunk.class, RenderTravelingTrunk.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntitySingularity.class, RenderSingularity.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityWisp.class, RenderEntityWisp.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityGolemBase.class, RenderEntityGolem.FACTORY);
	}
	
	public static final ThreadLocal<Random> seedableRandom = ThreadLocal.withInitial(() -> new Random());
	
	@Override
	public void init()
	{
		MinecraftForge.EVENT_BUS.register(new TooltipEvent());
		
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ColorItemResearch(), ItemsLT.DISCOVERY);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ColorItemSeal(), Item.getItemFromBlock(BlocksLT.SEAL));
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> Minecraft.getMinecraft().getBlockColors().colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()), null, null, tintIndex), BlocksLT.GREATWOOD_LEAVES);
		
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, world, pos, tintIndex) ->
		{
			Random rand = seedableRandom.get();
			rand.setSeed(pos != null ? pos.toLong() : 0L);
			int color = world != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(world, pos) : ColorizerFoliage.getFoliageColorBasic();
			int r = (color >> 16) & 0xFF;
			int g = (color >> 8) & 0xFF;
			int b = (color >> 0) & 0xFF;
			int max = 16;
			return Color.packARGB((int) MathHelper.clip(r + rand.nextInt(max) - rand.nextInt(max), 0, 255), (int) MathHelper.clip(g + rand.nextInt(max) - rand.nextInt(max), 0, 255), (int) MathHelper.clip(rand.nextInt(max) - rand.nextInt(max), 0, 255), 255);
		}, BlocksLT.GREATWOOD_LEAVES);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileCrystalOre.class, TESRCrystal.INSTANCE);
		for(BlockOreCrystal ore : BlockOreCrystal.crystals)
		{
			Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new ColorBlockOreCrystal(), ore);
			ItemRenderingHandler.INSTANCE.bindItemRender(Item.getItemFromBlock(ore), TESRCrystal.INSTANCE);
		}
		
		registerRender(TileCrucible.class, BlocksLT.CRUCIBLE, TESRCrucible.INSTANCE);
		registerRender(TileCrucibleEyes.class, BlocksLT.CRUCIBLE_EYES, TESRCrucibleEyes.INSTANCE);
		registerRender(TileCrucibleThaumium.class, BlocksLT.CRUCIBLE_THAUMIUM, TESRCrucibleThaumium.INSTANCE);
		registerRender(TileCrucibleVoid.class, BlocksLT.CRUCIBLE_VOID, TESRCrucibleVoid.INSTANCE);
		registerRender(TileConduit.class, BlocksLT.CONDUIT, TESRConduit.INSTANCE);
		registerRender(TileSilverwoodConduit.class, BlocksLT.CONDUIT_SILVERWOOD, TESRConduit.INSTANCE);
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
		registerRender(TileVisCondenser.class, BlocksLT.VIS_CONDENSER, TESRVisCondenser.INSTANCE);
		registerRender(TileWandConstructor.class, BlocksLT.WAND_CONSTRUCTOR, TESRWandConstructor.INSTANCE);
		registerRender(TileFuser.class, BlocksLT.FUSER_MB, TESRFuser.INSTANCE);
		registerRender(TileDarknessGenerator.class, BlocksLT.DARKNESS_GENERATOR, TESRDarknessGenerator.INSTANCE);
		registerRender(TileRepairer.class, BlocksLT.REPAIRER, TESRRepairer.INSTANCE);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileVoidChest.class, TESRVoidChest.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileSeal.class, TESRSeal.INSTANCE);
		
		ItemRenderingHandler.INSTANCE.bindItemRender(ItemsLT.WAND_ITEM_FREEZE, new RenderItemWandOfItemFreeze());
		ItemRenderingHandler.INSTANCE.bindItemRender(ItemsLT.WAND_REVERSAL, new RenderItemWandReversal());
		ItemRenderingHandler.INSTANCE.bindItemRender(ItemsLT.WAND, new RenderItemWand());
		
		penguSkinId = GlStateManager.generateTexture();
		
		try
		{
			BufferedImage img = ImageIO.read(new URL("https://raw.githubusercontent.com/APengu/HammerCore/1.11.x/skins/APengu.png"));
			GLImageManager.loadTexture(img, penguSkinId, false);
		} catch(Throwable err)
		{
		}
	}
	
	@Override
	public void spawnEmote(EmoteData data, MessageContext ctx)
	{
		if(ctx.side == Side.SERVER)
		{
			super.spawnEmote(data, ctx);
			return;
		}
		
		if(data == null)
			return;
		
		ParticleProxy_Client.queueParticleSpawn(new FXEmote(data));
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
	
	private static Map<String, Integer> mapSprites = new HashMap<>();
	
	public static int getTexSize(String sprite, int div)
	{
		if(mapSprites.get(sprite + ":" + div) != null)
			return mapSprites.get(sprite + ":" + div).intValue();
		
		try
		{
			InputStream inputstream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(sprite)).getInputStream();
			if(inputstream == null)
				throw new Exception("Image not found: " + sprite);
			BufferedImage bi = ImageIO.read(inputstream);
			int size = bi.getWidth() / div;
			mapSprites.put(sprite + ":" + div, size);
			return size;
		} catch(Exception e)
		{
			return 16;
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void reloadTextrues(TextureStitchEvent evt)
	{
		mapSprites.clear();
		
		LostThaumaturgy.LOG.info("Loading sprites...");
		try
		{
			LostThaumaturgy.LOG.info("  -JSON...");
			int sprite = 0;
			Scanner s = new Scanner(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Info.MOD_ID, "textures/blocks/_sprites.txt")).getInputStream());
			while(s.hasNextLine())
			{
				String ln = s.nextLine();
				if(ln.isEmpty())
					continue;
				evt.getMap().registerSprite(new ResourceLocation(Info.MOD_ID, "blocks/" + ln));
				sprite++;
			}
			s.close();
			LostThaumaturgy.LOG.info("   +Loaded " + sprite + " JSON Sprites!");
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		LostThaumaturgy.LOG.info("  -Wand:");
		
		LostThaumaturgy.LOG.info("    -Caps...");
		WandRegistry.getCaps().forEach(cap -> evt.getMap().registerSprite(new ResourceLocation(cap.getCapTexture())));
		LostThaumaturgy.LOG.info("     +Registered " + WandRegistry.getCaps().count() + " Wand Cap Textures");
		
		LostThaumaturgy.LOG.info("    -Rods...");
		WandRegistry.getRods().forEach(rod -> evt.getMap().registerSprite(new ResourceLocation(rod.getRodTexture())));
		LostThaumaturgy.LOG.info("     +Registered " + WandRegistry.getRods().count() + " Wand Rod Textures");
	}
	
	public static boolean dvis, dtaint, drad;
	
	@SubscribeEvent
	public void clientTick(ClientTickEvent evt)
	{
		if(Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.ticksExisted % 10 == 0)
		{
			dvis = false;
			dtaint = false;
			drad = false;
			
			for(ItemStack stack : Minecraft.getMinecraft().player.inventory.mainInventory)
			{
				if(stack.getItem() == ItemsLT.AURA_DETECTOR)
				{
					if(stack.getItemDamage() == 0 || stack.getItemDamage() == 3)
						dvis = true;
					if(stack.getItemDamage() == 1 || stack.getItemDamage() == 3)
						dtaint = true;
					if(stack.getItemDamage() == 2 || stack.getItemDamage() == 3)
						drad = true;
				}
			}
		}
	}
	
	private ItemStack[] handStacks = new ItemStack[2];
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderHUD(RenderGameOverlayEvent evt)
	{
		((BlockSilverwoodLeaves) BlocksLT.SILVERWOOD_LEAVES).setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
		((BlockGreatwoodLeaves) BlocksLT.GREATWOOD_LEAVES).setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
		((BlockTaintedLeaves) BlocksLT.TAINTED_LEAVES).setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
		
		Minecraft mc = Minecraft.getMinecraft();
		
		if(evt.getType() == ElementType.ALL)
		{
			IGoggles goggles = ItemGogglesRevealing.getWearing(mc.player);
			if(goggles != null)
			{
				int t = goggles.getRevealType();
				HudDetector.instance.render(t == 0 || t == 2 || t == 3, t == 1 || t == 2 || t == 3, t == 2 || t == 3, ClientAtmosphereChunk.getClientChunk(), true);
			} else if(dvis || dtaint || drad)
				HudDetector.instance.render(dvis, dtaint, drad, ClientAtmosphereChunk.getClientChunk(), false);
			
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
					String s = I18n.translateToLocal("gui." + Info.MOD_ID + ":" + (valid ? "" : "not_") + "upgradable");
					mc.fontRenderer.drawString(s, (k - mc.fontRenderer.getStringWidth(s)) / 2, l / 2 - 30, valid ? 0x22FF33 : 0xFF2233);
					Color.glColourRGBA(0xFFFFFFFF);
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderAura(RenderWorldLastEvent e)
	{
		
	}
	
	@Override
	public World getClientWorld()
	{
		return Minecraft.getMinecraft().world;
	}
	
	@Override
	public void openThaumonomicon()
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiThaumonomicon());
	}
}