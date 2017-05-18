package com.pengu.lostthaumaturgy.custom.aura;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.annotations.MCFBus;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.math.MathHelper;
import com.mrdimka.hammercore.net.HCNetwork;
import com.pengu.lostthaumaturgy.LTConfigs;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;
import com.pengu.lostthaumaturgy.custom.aura.taint.TaintRegistry;
import com.pengu.lostthaumaturgy.net.PacketUpdateClientAura;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp_AuraTicker_spillTaint;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp_AuraTicker_taintExplosion;

@MCFBus
public class AuraTicker
{
	private static boolean loadedAuras = false;
	public static ArrayList<Biome> BIOME_MAGIC = new ArrayList();
	public static ArrayList<Biome> BIOME_AIR = new ArrayList();
	public static ArrayList<Biome> BIOME_WATER = new ArrayList();
	public static ArrayList<Biome> BIOME_EARTH = new ArrayList();
	public static ArrayList<Biome> BIOME_FIRE = new ArrayList();
	public static ArrayList<Biome> BIOME_TAINT = new ArrayList();
	public static ArrayList<Biome> BIOME_EXTREMEAURA = new ArrayList();
	public static ArrayList<Biome> BIOME_HIGHAURATAINTED = new ArrayList();
	public static ArrayList<Biome> BIOME_HIGHAURA = new ArrayList();
	public static ArrayList<Biome> BIOME_LOWAURA = new ArrayList();
	public static ArrayList<Biome> BIOME_SILVERWOOD = new ArrayList();
	public static ArrayList<Biome> BIOME_FIREFLOWER = new ArrayList();
	public static ArrayList<Biome> BIOME_GREATWOOD = new ArrayList();
	
	static
	{
		// dawnInc = 0;
		// destDawn = 0;
		// loadedSpecialTiles = false;
		// inventory = new short[] { -1, -1, -1, -1, -1 };
		// carpetMounted = false;
		// carpetEndurance = 0;
		// loadedResearch = false;
		// tomeCategory = 0;
		// tomeIndex = 0;
		// loadedAuras = false;
		// chunkBuffer = 0;
		
		// toolMatThaumium = EnumHelper.addToolMaterial((String) "THAUMIUM",
		// (int) 3, (int) 400, (float) 7.0f, (int) 2, (int) 22);
		// toolMatVoid = EnumHelper.addToolMaterial((String) "VOID", (int) 3,
		// (int) 2000, (float) 8.0f, (int) 3, (int) 17);
		// armorMatThaumium = EnumHelper.addArmorMaterial((String) "THAUMIUM",
		// (int) 25, (int[]) new int[] { 2, 6, 5, 2 }, (int) 25);
		// armorMatVoid = EnumHelper.addArmorMaterial((String) "VOID", (int) 45,
		// (int[]) new int[] { 3, 8, 6, 3 }, (int) 17);
		// armorMatSpecial = EnumHelper.addArmorMaterial((String) "SPECIAL",
		// (int) 25, (int[]) new int[] { 1, 3, 2, 1 }, (int) 10);
		
		BIOME_LOWAURA.add(Biomes.DESERT);
		BIOME_LOWAURA.add(Biomes.HELL);
		BIOME_LOWAURA.add(Biomes.DESERT_HILLS);
		BIOME_HIGHAURA.add(Biomes.TAIGA);
		BIOME_HIGHAURA.add(Biomes.FOREST);
		BIOME_HIGHAURA.add(Biomes.FOREST_HILLS);
		BIOME_HIGHAURA.add(Biomes.TAIGA_HILLS);
		BIOME_EXTREMEAURA.add(Biomes.MUSHROOM_ISLAND);
		BIOME_EXTREMEAURA.add(Biomes.MUSHROOM_ISLAND_SHORE);
		BIOME_EXTREMEAURA.add(Biomes.JUNGLE);
		BIOME_EXTREMEAURA.add(Biomes.JUNGLE_HILLS);
		BIOME_HIGHAURATAINTED.add(Biomes.SWAMPLAND);
		// BIOME_MAGIC.add(Biomes.MAXGROUPCOUNT);
		BIOME_MAGIC.add(Biomes.MUSHROOM_ISLAND);
		BIOME_MAGIC.add(Biomes.MUSHROOM_ISLAND_SHORE);
		BIOME_MAGIC.add(Biomes.JUNGLE);
		BIOME_MAGIC.add(Biomes.JUNGLE_HILLS);
		BIOME_AIR.add(Biomes.DESERT);
		BIOME_AIR.add(Biomes.EXTREME_HILLS);
		BIOME_AIR.add(Biomes.ICE_MOUNTAINS);
		// BIOME_AIR.add(Biomes.MAXGROUPCOUNT);
		// BIOME_WATER.add(Biomes.MINGROUPCOUNT);
		BIOME_WATER.add(Biomes.FROZEN_OCEAN);
		BIOME_WATER.add(Biomes.RIVER);
		BIOME_WATER.add(Biomes.ICE_PLAINS);
		BIOME_WATER.add(Biomes.SWAMPLAND);
		BIOME_WATER.add(Biomes.ICE_MOUNTAINS);
		BIOME_EARTH.add(Biomes.EXTREME_HILLS);
		BIOME_EARTH.add(Biomes.TAIGA);
		BIOME_EARTH.add(Biomes.FOREST);
		BIOME_EARTH.add(Biomes.ICE_MOUNTAINS);
		BIOME_EARTH.add(Biomes.JUNGLE);
		BIOME_EARTH.add(Biomes.JUNGLE_HILLS);
		BIOME_FIRE.add(Biomes.DESERT);
		BIOME_FIRE.add(Biomes.HELL);
		BIOME_FIRE.add(Biomes.EXTREME_HILLS);
		BIOME_FIRE.add(Biomes.DESERT_HILLS);
		BIOME_TAINT.add(Biomes.MUSHROOM_ISLAND);
		BIOME_TAINT.add(Biomes.MUSHROOM_ISLAND_SHORE);
		BIOME_TAINT.add(Biomes.SWAMPLAND);
		BIOME_SILVERWOOD.add(Biomes.FOREST);
		BIOME_SILVERWOOD.add(Biomes.FOREST_HILLS);
		BIOME_SILVERWOOD.add(Biomes.TAIGA);
		BIOME_SILVERWOOD.add(Biomes.TAIGA_HILLS);
		BIOME_SILVERWOOD.add(Biomes.JUNGLE_HILLS);
		BIOME_GREATWOOD.add(Biomes.TAIGA);
		BIOME_GREATWOOD.add(Biomes.FOREST);
		// BIOME_GREATWOOD.add(Biomes.MAXGROUPCOUNT);
		BIOME_FIREFLOWER.add(Biomes.DESERT);
		BIOME_FIREFLOWER.add(Biomes.MESA);
		BIOME_FIREFLOWER.add(Biomes.DESERT_HILLS);
		BIOME_FIREFLOWER.add(Biomes.MESA_CLEAR_ROCK);
		BIOME_FIREFLOWER.add(Biomes.MESA_ROCK);
	}
	
	public short margin = (short) ((float) LTConfigs.auraMax / 7.5f);
	
	public static HashMap<String, SIAuraChunk> AuraHM = new HashMap<>();
	
	@SubscribeEvent
	public void serverTick(ServerTickEvent evt)
	{
		
	}
	
	@SubscribeEvent
	public void worldTick(WorldTickEvent evt)
	{
		if(!evt.world.isRemote)
			updateAura(evt.world);
	}
	
	@SubscribeEvent
	public void loadChunk(ChunkEvent.Load evt)
	{
		World world = evt.getWorld();
		Chunk chunk = evt.getChunk();
		
		if(!world.isRemote)
			getAuraChunkFromChunkCoords(world, chunk.x, chunk.z);
	}
	
	@SubscribeEvent
	public void worldSave(WorldEvent.Save evt)
	{
		if(evt.getWorld().provider.getDimension() == 0)
			SaveAuraData();
	}
	
	@SubscribeEvent
	public void playerTick(PlayerTickEvent evt)
	{
		EntityPlayer player = evt.player;
		EntityPlayerMP mp = WorldUtil.cast(player, EntityPlayerMP.class);
		
		SIAuraChunk chunk = getAuraChunkFromBlockCoords(player.world, player.getPosition());
		if(mp != null && !player.world.isRemote && chunk != null)
		{
			boolean changedChunks = false;
			
			BlockPos n = new BlockPos(player.posX, player.posY, player.posZ);
			BlockPos o = new BlockPos(player.prevPosX, player.prevPosY, player.prevPosZ);
			
			Chunk nchunk = player.world.getChunkFromBlockCoords(n);
			Chunk ochunk = player.world.getChunkFromBlockCoords(o);
			
			changedChunks = !nchunk.equals(ochunk);
			
			if(player.ticksExisted % 20 == 0 || changedChunks)
				HCNetwork.manager.sendTo(new PacketUpdateClientAura(chunk), mp);
		}
	}
	
	public static SIAuraChunk getAuraChunkFromBlockCoords(World world, BlockPos pos)
	{
		Chunk c = world.getChunkFromBlockCoords(pos);
		return getAuraChunkFromChunkCoords(world, c.x, c.z);
	}
	
	public static SIAuraChunk getAuraChunkFromBlockCoords(World world, int x, int z)
	{
		return getAuraChunkFromBlockCoords(world, new BlockPos(x, 0, z));
	}
	
	public static SIAuraChunk getAuraChunkFromChunkCoords(World world, int chunkX, int chunkZ)
	{
		SIAuraChunk chunk = AuraHM.get(asKey(chunkX, chunkZ, world.provider.getDimension()));
		if(chunk == null)
		{
			GenerateAura(world, world.rand, chunkX, chunkZ);
			return AuraHM.get(asKey(chunkX, chunkZ, world.provider.getDimension()));
		}
		return chunk;
	}
	
	public void updateAura(World worldObj)
	{
		if(worldObj.isRemote)
			return;
		
		Collection<SIAuraChunk> c = AuraHM.values();
		boolean noupdates = true;
		int counter = AuraHM.size() / 200;
		for(SIAuraChunk ac2 : c)
		{
			if(ac2.updated || ac2.dimension != worldObj.provider.getDimension())
				continue;
			noupdates = false;
			ac2.updated = true;
			ac2.previousVis = ac2.vis;
			ac2.previousTaint = ac2.taint;
			if(ac2.goodVibes > 100)
			{
				ac2.goodVibes = 100;
			}
			if(ac2.badVibes > 100)
			{
				ac2.badVibes = 100;
			}
			if(ac2.badVibes > 50 && ac2.goodVibes == 0)
			{
				ac2.vis = (short) (ac2.vis - 1);
				ac2.badVibes = (short) (ac2.badVibes - 5);
			}
			if(ac2.badVibes > 0 && ac2.goodVibes > 0)
			{
				ac2.goodVibes = (short) (ac2.goodVibes - 1);
				ac2.badVibes = (short) (ac2.badVibes - 1);
			}
			if(worldObj.rand.nextInt(100) < ac2.goodVibes && ac2.goodVibes > 0)
			{
				ac2.vis = (short) (ac2.vis + 1);
				ac2.goodVibes = (short) (ac2.goodVibes - Math.max(1, ac2.vis / (LTConfigs.auraMax / 10)));
			}
			if(worldObj.rand.nextInt(100) < ac2.badVibes && ac2.badVibes > 0)
			{
				ac2.taint = (short) (ac2.taint + 1);
				ac2.badVibes = (short) (ac2.badVibes - Math.max(1, ac2.taint / (LTConfigs.auraMax / 10)));
			}
			for(int a = 0; a < 4; ++a)
			{
				int val;
				int auraX = ac2.x;
				int auraZ = ac2.z;
				switch(a)
				{
				case 0:
				{
					++auraX;
					break;
				}
				case 1:
				{
					--auraX;
					break;
				}
				case 2:
				{
					++auraZ;
					break;
				}
				case 3:
				{
					--auraZ;
				}
				}
				SIAuraChunk nc = AuraHM.get(asKey(auraX, auraZ, worldObj.provider.getDimension()));
				if(nc == null)
					continue;
				if(nc.vis > ac2.vis + this.margin && ac2.vis < LTConfigs.auraMax && nc.vis > 0)
				{
					val = (int) Math.max(1.0f, (float) (nc.vis - ac2.vis) / (float) LTConfigs.auraMax * 10.0f);
					ac2.vis = (short) (ac2.vis + val);
					nc.vis = (short) (nc.vis - val);
				}
				if((float) nc.taint > (float) ac2.taint + (float) this.margin * 0.75f && ac2.taint < LTConfigs.auraMax && nc.taint > 0)
				{
					val = (int) Math.max(1.0f, (float) (nc.taint - ac2.taint) / (float) LTConfigs.auraMax * 10.0f);
					ac2.taint = (short) (ac2.taint + val);
					nc.taint = (short) (nc.taint - val);
				}
				if(nc.boost <= ac2.boost || nc.boost <= 50 || ac2.boost >= 100)
					continue;
				ac2.boost = (short) (ac2.boost + 1);
				nc.boost = (short) (nc.boost - 1);
			}
			ac2.vis = (short) MathHelper.clip((int) ac2.vis, (int) 0, (int) LTConfigs.auraMax);
			ac2.taint = (short) MathHelper.clip((int) ac2.taint, (int) 0, (int) LTConfigs.auraMax);
			ac2.goodVibes = (short) MathHelper.clip((int) ac2.goodVibes, (int) 0, (int) 100);
			ac2.badVibes = (short) MathHelper.clip((int) ac2.badVibes, (int) 0, (int) 100);
			if(ac2.taint > LTConfigs.auraMax / 2)
				taintifyChunk(worldObj, ac2);
			if(--counter > 0)
				continue;
			break;
		}
		if(noupdates && AuraHM.size() > 0)
		{
			for(SIAuraChunk ac2 : c)
			{
				if(ac2.dimension != worldObj.provider.getDimension())
					continue;
				ac2.updated = false;
			}
		}
	}
	
	public static void GenerateAura(World world, Random random, int x, int z)
	{
		int tchance;
		int upper = LTConfigs.auraMax / 3;
		int lower = LTConfigs.auraMax / 5;
		boolean extraTaint = false;
		Biome bio = world.getBiomeProvider().getBiome(new BlockPos(x, 0, z));
		
		if(BIOME_LOWAURA.contains(bio))
		{
			upper = LTConfigs.auraMax / 8;
			lower = LTConfigs.auraMax / 20;
			if(bio == Biomes.HELL)
				extraTaint = true;
		} else if(BIOME_HIGHAURA.contains(bio))
		{
			upper = (int) ((float) LTConfigs.auraMax * 0.6f);
			lower = LTConfigs.auraMax / 3;
		} else if(BIOME_EXTREMEAURA.contains(bio))
		{
			upper = (int) ((float) LTConfigs.auraMax * 0.7f);
			lower = LTConfigs.auraMax / 2;
		} else if(BIOME_HIGHAURATAINTED.contains(bio))
		{
			upper = (int) ((float) LTConfigs.auraMax * 0.5f);
			lower = LTConfigs.auraMax / 3;
			extraTaint = true;
		}
		
		short auraStrength = (short) (lower + random.nextInt(upper - lower));
		short auraTaint = (short) (auraStrength / 3);
		int n = tchance = LTConfigs.taintSpawn == 2 ? 300 : 2200;
		if(LTConfigs.taintSpawn > 0 && random.nextInt(tchance) == 0)
		{
			auraStrength = (short) ((lower + random.nextInt(upper - lower)) / 2);
			auraTaint = LTConfigs.taintSpawn == 2 ? (short) ((float) LTConfigs.auraMax * 0.8f + (float) random.nextInt((int) ((float) LTConfigs.auraMax * 0.2f))) : (short) ((float) LTConfigs.auraMax * 0.5f + (float) random.nextInt((int) ((float) LTConfigs.auraMax * 0.2f)));
			GenerateTaintedArea(world, random, x + 8, z + 8, auraTaint);
		}
		if(extraTaint)
		{
			auraTaint = (short) ((double) auraTaint * 1.5);
		}
		SIAuraChunk ac = new SIAuraChunk();
		ac.vis = auraStrength;
		ac.taint = auraTaint;
		ac.x = x;
		ac.z = z;
		ac.dimension = world.provider.getDimension();
		AddAuraToList(ac);
	}
	
	public static void SaveAuraData()
	{
		LoadAuraData();
		try
		{
			FileOutputStream fos = new FileOutputStream(getAuraSaveFile().getAbsolutePath());
			GZIPOutputStream gzos = new GZIPOutputStream(fos);
			ObjectOutputStream out = new ObjectOutputStream(gzos);
			out.writeObject(AuraHM);
			out.flush();
			out.close();
		} catch(IOException e)
		{
			e.printStackTrace();
		} catch(Exception e)
		{
			// empty catch block
		}
	}
	
	public static void LoadAuraData()
	{
		if(loadedAuras)
			return;
		loadedAuras = true;
		
		try
		{
			FileInputStream fis = new FileInputStream(getAuraSaveFile().getAbsolutePath());
			GZIPInputStream gzis = new GZIPInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(gzis);
			HashMap loaded = (HashMap) in.readObject();
			in.close();
			AuraHM = loaded;
			LostThaumaturgy.LOG.info("Loaded " + AuraHM.size() + " aura chunks.");
		} catch(FileNotFoundException e)
		{
			LostThaumaturgy.LOG.info("Aura file not found. New one will be generated");
			try
			{
				FileInputStream fis = new FileInputStream(getLegacyAuraSaveFile().getAbsolutePath());
				GZIPInputStream gzis = new GZIPInputStream(fis);
				ObjectInputStream in = new ObjectInputStream(gzis);
				HashMap loaded = (HashMap) in.readObject();
				in.close();
				AuraHM = loaded;
				Collection<SIAuraChunk> c = AuraHM.values();
				for(SIAuraChunk ac : c)
				{
					ac.vis = (short) (ac.vis / 2);
					ac.taint = (short) (ac.taint / 2);
				}
				LostThaumaturgy.LOG.info("Converted " + AuraHM.size() + " legacy auras.");
			} catch(Exception e2)
			{
			}
		} catch(Exception e)
		{
			// empty catch block
		}
	}
	
	public static File getAuraSaveFile() throws Exception
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		return new File((server.isDedicatedServer() ? "" : "saves" + File.separator) + server.getFolderName(), "pengu-lt.au");
	}
	
	public static File getLegacyAuraSaveFile() throws Exception
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		return new File((server.isDedicatedServer() ? "" : "saves" + File.separator) + server.getFolderName(), "pengu-lt.au2");
	}
	
	public static void GenerateTaintedArea(World world, Random random, int x, int z, short auraTaint)
	{
		for(int a = 0; a < 500; ++a)
		{
			int xx = x + random.nextInt(31) - random.nextInt(31);
			int zz = z + random.nextInt(31) - random.nextInt(31);
			increaseTaintedPlants(world, xx, world.getHeight(xx, zz), zz);
			Chunk c = world.getChunkFromBlockCoords(new BlockPos(x, 0, z));
			SIAuraChunk ac = (SIAuraChunk) AuraHM.get(asKey(c.x, c.z, world.provider.getDimension()));
			if(ac == null || (float) ac.taint >= (float) auraTaint * 0.8f)
				continue;
			ac.taint = (short) ((float) auraTaint * (0.8f + random.nextFloat() * 0.25f));
		}
	}
	
	public static boolean taintifyChunk(World world, SIAuraChunk ac)
	{
		int z;
		int x = (ac.x << 4) + world.rand.nextInt(16);
		int y = world.rand.nextInt(world.getHeight(x, z = (ac.z << 4) + world.rand.nextInt(16)) > 0 ? world.getHeight(x, z) : 256);
		if(increaseTaintedPlants(world, x, y, z))
			return true;
		return false;
	}
	
	public static void decreaseTaintedPlantsInArea(World world, int x, int y, int z, int area)
	{
		for(int xx = -area; xx <= area; ++xx)
		{
			for(int yy = -area; yy <= area; ++yy)
			{
				for(int zz = -area; zz <= area; ++zz)
				{
					if(!decreaseTaintedPlants(world, x + xx, y + yy, z + zz))
						continue;
					return;
				}
			}
		}
	}
	
	public static boolean decreaseTaintedPlants(World world, int x, int y, int z)
	{
		return TaintRegistry.cureBlock(world, new BlockPos(x, y, z));
	}
	
	public static boolean isNear(World world, int x, int y, int z, int range, IBlockState block)
	{
		for(int a = -range; a < range + 1; ++a)
		{
			for(int b = -range; b < range + 1; ++b)
			{
				for(int c = -range; c < range + 1; ++c)
				{
					if(b + y < 0 || b + y >= world.getHeight() || !world.isBlockLoaded(new BlockPos(x, y, z)) || !block.equals(world.getBlockState(new BlockPos(x + a, y + b, z + c))))
						continue;
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isNear(World world, int x, int y, int z, int range, IBlockState[] states)
	{
		for(int a = -range; a <= range; ++a)
		{
			for(int b = -range; b <= range; ++b)
			{
				for(int c = -range; c <= range; ++c)
				{
					if(b + y < 0 || b + y >= world.getHeight() || !world.isBlockLoaded(new BlockPos(x + a >> 4, y, z + c >> 4)))
						continue;
					for(int q = 0; q < states.length; ++q)
					{
						if(states[q].equals(world.getBlockState(new BlockPos(x + a, y + b, z + c))))
							return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean increaseTaintedPlants(World world, int x, int y, int z)
	{
		// TODO: silverwood and greatwood
		// if(isNear(world, x, y, z, 3, new IBlockState[] {}))
		// {
		// return false;
		// }
		
		Chunk c = world.getChunkFromBlockCoords(new BlockPos(x, y, z));
		short taint = 0;
		SIAuraChunk ac = (SIAuraChunk) AuraHM.get(asKey(c.x, c.z, world.provider.getDimension()));
		if(ac != null)
			taint = ac.taint;
		for(int xx = -1; xx <= 1; ++xx)
		{
			for(int yy = -1; yy <= 1; ++yy)
			{
				for(int zz = -1; zz <= 1; ++zz)
				{
					BlockPos pos = new BlockPos(x + xx, y + yy, z + zz);
					boolean taintable = TaintRegistry.canTaintBlock(world, pos);
					if(taintable)
					{
						TaintRegistry.taintBlock(world, pos);
						badSound(world, (float) x + (float) xx, (float) y + (float) yy + 1.0f, (float) z + (float) zz);
						
						for(int q = 0; q < 100; ++q)
							HCNetwork.manager.sendToAllAround(new PacketFXWisp_AuraTicker_taintExplosion((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f, (float) x + 0.5f + (world.rand.nextFloat() - world.rand.nextFloat()) * 2.0f, (float) y + 0.5f + (world.rand.nextFloat() - world.rand.nextFloat()) * 2.0f, (float) z + 0.5f + (world.rand.nextFloat() - world.rand.nextFloat()) * 2.0f, 1.0f, 5), new TargetPoint(world.provider.getDimension(), x, y, z, 50));
					}
				}
			}
		}
		return false;
	}
	
	public static void taintExplosion(World w, int x, int y, int z)
	{
		w.createExplosion(null, (double) ((float) x + 0.5f), (double) ((float) y + 0.5f), (double) ((float) z + 0.5f), 1.0f, false);
		for(int xx = x - 2; xx <= x + 2; ++xx)
			for(int yy = y - 2; yy <= y + 2; ++yy)
				for(int zz = z - 2; zz <= z + 2; ++zz)
					increaseTaintedPlants(w, xx, yy, zz);
		for(int q = 0; q < 100; ++q)
			HCNetwork.manager.sendToAllAround(new PacketFXWisp_AuraTicker_taintExplosion((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f, (float) x + 0.5f + (w.rand.nextFloat() - w.rand.nextFloat()) * 2.0f, (float) y + 0.5f + (w.rand.nextFloat() - w.rand.nextFloat()) * 2.0f, (float) z + 0.5f + (w.rand.nextFloat() - w.rand.nextFloat()) * 2.0f, 1.0f, 5), new TargetPoint(w.provider.getDimension(), x, y, z, 50));
	}
	
	public static void spillTaint(World world, BlockPos pos)
	{
		IConnection ic;
		TileEntity tc = world.getTileEntity(pos);
		if(tc != null && tc instanceof IConnection && (ic = (IConnection) tc).getTaintedVis() > 0.0f)
		{
			int at = (int) ic.getTaintedVis();
			Chunk c = world.getChunkFromBlockCoords(pos);
			SIAuraChunk ac = (SIAuraChunk) AuraHM.get(asKey(c.x, c.z, world.provider.getDimension()));
			if(ac != null)
			{
				ac.taint = (short) (ac.taint + at);
				HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":fizz", pos, .2F, 2F - world.rand.nextFloat() * .4F, SoundCategory.AMBIENT);
				for(int a = 0; a < Math.min(at, 50); ++a)
				{
					float x = (float) pos.getX() + world.rand.nextFloat();
					float y = (float) pos.getY() + world.rand.nextFloat();
					float z = (float) pos.getZ() + world.rand.nextFloat();
					
					HCNetwork.manager.sendToAllAround(new PacketFXWisp_AuraTicker_spillTaint(x, y, z, x, y + 1.0f, z, 0.5f, 5), new TargetPoint(world.provider.getDimension(), x, y, z, 50));
				}
			}
		}
	}
	
	public static void badSound(World w, float x, float y, float z)
	{
		HammerCore.audioProxy.playSoundAt(w, LTInfo.MOD_ID + ":roots", x, y, z, .05F, 1.1F + w.rand.nextFloat() * .2F, SoundCategory.AMBIENT);
	}
	
	public static boolean isAdjacentToOpenBlock(World world, int x, int y, int z)
	{
		for(int a = 0; a < 6; ++a)
		{
			int xx = 0;
			int yy = 0;
			int zz = 0;
			switch(a)
			{
			case 0:
			{
				++yy;
				break;
			}
			case 1:
			{
				--yy;
				break;
			}
			case 2:
			{
				++zz;
				break;
			}
			case 3:
			{
				--zz;
				break;
			}
			case 4:
			{
				++xx;
				break;
			}
			case 5:
			{
				--xx;
			}
			}
			
			BlockPos pos = new BlockPos(x + xx, y + yy, z + zz);
			IBlockState state = world.getBlockState(pos);
			
			if(world.isAirBlock(pos))
				// || state == mod_ThaumCraft.blockTaint.bO
				// && (world.a(x + xx, y + yy, z + zz) !=
				// mod_ThaumCraft.blockTaint.bO
				// || world.e(x + xx, y + yy, z + zz) != 10))
				continue;
			
			return true;
		}
		return false;
	}
	
	private static int chunkBuffer;
	
	public static void AddAuraToList(SIAuraChunk ac)
	{
		LoadAuraData();
		SIAuraChunk current = (SIAuraChunk) AuraHM.get(asKey(ac.x, ac.z, ac.dimension));
		if(current == null)
		{
			AuraHM.put(asKey(ac.x, ac.z, ac.dimension), ac);
			if(++chunkBuffer > 10)
			{
				SaveAuraData();
				chunkBuffer = 0;
			}
		}
	}
	
	private static String asKey(int x, int z, int dim)
	{
		return x + "|" + z + "@" + dim;
	}
}