package com.pengu.lostthaumaturgy.custom.aura;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.math.MathHelper;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.utils.ChunkUtils;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.lostthaumaturgy.LTConfigs;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.api.event.AuraEvent;
import com.pengu.lostthaumaturgy.api.tiles.IConnection;
import com.pengu.lostthaumaturgy.custom.aura.api.AuraAttachments;
import com.pengu.lostthaumaturgy.custom.aura.taint.TaintRegistry;
import com.pengu.lostthaumaturgy.custom.research.ResearchSystem;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.net.PacketReloadCR;
import com.pengu.lostthaumaturgy.net.PacketUpdateClientAura;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp2;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp_AuraTicker_spillTaint;
import com.pengu.lostthaumaturgy.net.wisp.PacketFXWisp_AuraTicker_taintExplosion;

@MCFBus
public class AuraTicker
{
	public static boolean loadedAuras = false;
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
		BIOME_MAGIC.add(Biomes.MUSHROOM_ISLAND);
		BIOME_MAGIC.add(Biomes.MUSHROOM_ISLAND_SHORE);
		BIOME_MAGIC.add(Biomes.JUNGLE);
		BIOME_MAGIC.add(Biomes.JUNGLE_HILLS);
		BIOME_AIR.add(Biomes.DESERT);
		BIOME_AIR.add(Biomes.EXTREME_HILLS);
		BIOME_AIR.add(Biomes.ICE_MOUNTAINS);
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
		BIOME_EARTH.add(Biomes.PLAINS);
		BIOME_EARTH.add(Biomes.JUNGLE_EDGE);
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
		BIOME_FIREFLOWER.add(Biomes.DESERT);
		BIOME_FIREFLOWER.add(Biomes.MESA);
		BIOME_FIREFLOWER.add(Biomes.DESERT_HILLS);
		BIOME_FIREFLOWER.add(Biomes.MESA_CLEAR_ROCK);
		BIOME_FIREFLOWER.add(Biomes.MESA_ROCK);
	}
	
	public short margin = (short) ((float) LTConfigs.aura_max / 7.5f);
	
	public static HashMap<String, AtmosphereChunk> AuraHM = new HashMap<>();
	public static ArrayList<Long> Monoliths = new ArrayList<Long>();
	
	public static void addMonolith(BlockPos pos)
	{
		pos = new BlockPos(pos.getX(), 0, pos.getZ());
		long p = pos.toLong();
		if(!Monoliths.contains(p))
			Monoliths.add(p);
	}
	
	public static double getDistanceSqToClosestMonolith(BlockPos pos)
	{
		pos = new BlockPos(pos.getX(), 0, pos.getZ());
		double d = Double.POSITIVE_INFINITY;
		for(int i = 0; i < Monoliths.size(); ++i)
		{
			BlockPos monolith = BlockPos.fromLong(Monoliths.get(i));
			double sq = pos.distanceSq(monolith);
			if(sq < d)
				d = sq;
		}
		return d;
	}
	
	public static BlockPos getClosestMonolithPos(BlockPos pos)
	{
		pos = new BlockPos(pos.getX(), 0, pos.getZ());
		BlockPos closest = null;
		double d = Double.POSITIVE_INFINITY;
		for(int i = 0; i < Monoliths.size(); ++i)
		{
			BlockPos monolith = BlockPos.fromLong(Monoliths.get(i));
			double sq = pos.distanceSq(monolith);
			if(sq < d)
			{
				d = sq;
				closest = monolith;
			}
		}
		return closest;
	}
	
	@SubscribeEvent
	public void worldTick(WorldTickEvent evt)
	{
		if(!evt.world.isRemote)
			try
			{
				updateAura(evt.world);
			} catch(ConcurrentModificationException cme)
			{
			}
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
	public void playerJoined(PlayerLoggedInEvent evt)
	{
		EntityPlayer player = evt.player;
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
			HCNetwork.manager.sendTo(new PacketReloadCR(), (EntityPlayerMP) player);
	}
	
	@SubscribeEvent
	public void playerTick(PlayerTickEvent evt)
	{
		try
		{
			EntityPlayer player = evt.player;
			EntityPlayerMP mp = WorldUtil.cast(player, EntityPlayerMP.class);
			
			AtmosphereChunk chunk = getAuraChunkFromBlockCoords(player.world, player.getPosition());
			if(mp != null && !player.world.isRemote && chunk != null)
			{
				boolean changedChunks = false;
				
				BlockPos n = new BlockPos(player.posX, player.posY, player.posZ);
				BlockPos o = new BlockPos(player.prevPosX, player.prevPosY, player.prevPosZ);
				
				Chunk nchunk = player.world.getChunkFromBlockCoords(n);
				Chunk ochunk = player.world.getChunkFromBlockCoords(o);
				
				changedChunks = nchunk.x != ochunk.x || nchunk.z != ochunk.z;
				
				if(mp.ticksExisted % 20 == 0 || changedChunks)
				{
					HCNetwork.manager.sendTo(new PacketUpdateClientAura(chunk, mp), mp);
					HCNetwork.manager.sendTo(ResearchSystem.getPacketFor(mp), mp);
				}
			}
		} catch(Throwable err)
		{
		}
	}
	
	public static int getCrystalByBiome(World world, BlockPos pos, int darkAmount)
	{
		int type;
		int c0 = 1;
		int c1 = 1;
		int c2 = 1;
		int c3 = 1;
		int c4 = 1;
		int c5 = darkAmount;
		Biome bio = world.getBiome(pos);
		if(AuraTicker.BIOME_MAGIC.contains(bio))
			++c0;
		if(AuraTicker.BIOME_AIR.contains(bio))
			++c1;
		if(AuraTicker.BIOME_WATER.contains(bio))
			++c2;
		if(AuraTicker.BIOME_EARTH.contains(bio))
			++c3;
		if(AuraTicker.BIOME_FIRE.contains(bio))
			++c4;
		if(darkAmount > 0 && AuraTicker.BIOME_TAINT.contains(bio))
			++c5;
		if((type = world.rand.nextInt(c0 + c1 + c2 + c3 + c4 + c5)) < c0)
			type = 0;
		else if(type < c0 + c1)
			type = 1;
		else if(type < c0 + c1 + c2)
			type = 2;
		else if(type < c0 + c1 + c2 + c3)
			type = 3;
		else if(type < c0 + c1 + c2 + c3 + c4)
			type = 4;
		else if(type < c0 + c1 + c2 + c3 + c4 + c5)
			type = 5;
		return type;
	}
	
	public static AtmosphereChunk getAuraChunkFromBlockCoords(WorldLocation loc)
	{
		return getAuraChunkFromBlockCoords(loc.getWorld(), loc.getPos());
	}
	
	public static AtmosphereChunk getAuraChunkFromBlockCoords(World world, BlockPos pos)
	{
		Chunk c = world.getChunkFromBlockCoords(pos);
		return getAuraChunkFromChunkCoords(world, c.x, c.z);
	}
	
	public static AtmosphereChunk getAuraChunkFromBlockCoords(World world, int x, int z)
	{
		return getAuraChunkFromBlockCoords(world, new BlockPos(x, 0, z));
	}
	
	public static AtmosphereChunk getAuraChunkFromChunkCoords(World world, int chunkX, int chunkZ)
	{
		AtmosphereChunk chunk = AuraHM.get(asKey(chunkX, chunkZ, world.provider.getDimension()));
		if(chunk == null)
		{
			GenerateAura(world, new SecureRandom(), chunkX, chunkZ);
			return AuraHM.get(asKey(chunkX, chunkZ, world.provider.getDimension()));
		}
		return chunk;
	}
	
	public static AtmosphereChunk getAuraChunkFromChunkCoords_NoGen(World world, int chunkX, int chunkZ)
	{
		return AuraHM.get(asKey(chunkX, chunkZ, world.provider.getDimension()));
	}
	
	public void updateAura(World world) throws ConcurrentModificationException
	{
		if(world.isRemote)
			return;
		
		Collection<AtmosphereChunk> c = AuraHM.values();
		boolean noupdates = true;
		int counter = AuraHM.size() / 200;
		for(AtmosphereChunk ac2 : c)
		{
			if(ac2.updated || ac2.dimension != world.provider.getDimension())
				continue;
			
			MinecraftForge.EVENT_BUS.post(new AuraEvent.Update(ac2, world));
			
			ac2.previousVis = ac2.vis;
			ac2.previousTaint = ac2.taint;
			ac2.previousRadiation = ac2.radiation;
			
			AuraAttachments.attach(ac2);
			noupdates = false;
			ac2.updated = true;
			
			if(ac2.goodVibes > 100)
				ac2.goodVibes = 100;
			if(ac2.badVibes > 100)
				ac2.badVibes = 100;
			
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
			
			if(world.rand.nextInt(100) < ac2.goodVibes && ac2.goodVibes > 0)
			{
				ac2.vis = (short) (ac2.vis + 1);
				ac2.goodVibes = (short) (ac2.goodVibes - Math.max(1, ac2.vis / (LTConfigs.aura_max / 10)));
			}
			
			if(world.rand.nextInt(100) < ac2.badVibes && ac2.badVibes > 0)
			{
				ac2.taint = (short) (ac2.taint + 1);
				ac2.badVibes = (short) (ac2.badVibes - Math.max(1, ac2.taint / (LTConfigs.aura_max / 10)));
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
				
				AtmosphereChunk nc = getAuraChunkFromChunkCoords_NoGen(world, auraX, auraZ);
				
				if(nc == null)
					continue;
				
				if(nc.vis > ac2.vis + this.margin && ac2.vis < LTConfigs.aura_max && nc.vis > 0)
				{
					val = (int) Math.max(1, (float) (nc.vis - ac2.vis) / (float) LTConfigs.aura_max * 10F);
					ac2.vis += val;
					nc.vis -= val;
				}
				
				if((float) nc.taint > (float) ac2.taint + (float) this.margin * 0.75f && ac2.taint < LTConfigs.aura_max && nc.taint > 0)
				{
					val = (int) Math.max(1, (float) (nc.taint - ac2.taint) / (float) LTConfigs.aura_max * 10F);
					ac2.taint += val;
					nc.taint -= val;
				}
				
				if(world.rand.nextInt(8) == 0)
					equalizeRadiation(ac2, nc);
				
				if(nc.boost <= ac2.boost || nc.boost <= 50 || ac2.boost >= 100)
					continue;
				
				ac2.boost++;
				nc.boost--;
			}
			
			ac2.vis = (short) MathHelper.clip(Math.abs(ac2.vis), 0, LTConfigs.aura_max);
			ac2.taint = (short) MathHelper.clip(Math.abs(ac2.taint), 0, LTConfigs.aura_max);
			ac2.goodVibes = (short) MathHelper.clip(Math.abs(ac2.goodVibes), 0, 100);
			ac2.badVibes = (short) MathHelper.clip(Math.abs(ac2.badVibes), 0, 100);
			ac2.radiation = (float) MathHelper.clip(Math.abs(ac2.radiation), 0, LTConfigs.aura_radMax);
			
			if(world.isBlockLoaded(ChunkUtils.getChunkPos(ac2.x, ac2.z, 8, 127, 8)))
			{
				if(ac2.isTainted())
					taintifyChunk(world, ac2);
				else
					purifyChunk(world, ac2);
			}
			
			{
				float rad = ac2.radiation;
				if(rad - (LTConfigs.aura_radMax / 2F + LTConfigs.aura_radMax / 12F) >= .0001F)
				{
					float maxEat = (rad - (LTConfigs.aura_radMax / 2F + LTConfigs.aura_radMax / 12F)) * (1000F / 3F);
					float eat = 0F;
					
					short vibes = ac2.badVibes;
					short taint = ac2.taint;
					short vis = ac2.vis;
					
					short taintAccept = (short) MathHelper.clip(LTConfigs.aura_max - taint, 0, 100);
					short visDrain = (short) MathHelper.clip(vis, 0, 100);
					
					eat += ((ac2.badVibes = 100) - vibes) * 100;
					eat += taintAccept + visDrain;
					
					if(eat > 0F)
					{
						float eatFact = maxEat / eat;
						
						ac2.vis -= visDrain * eatFact * 66.666;
						ac2.taint += taintAccept * eatFact * 66.666;
						ac2.badVibes = 100;
						
						ac2.radiation -= .02F;
					}
				}
			}
			
			if(ac2.isTainted())
			{
				int c2 = 12 + world.rand.nextInt(24);
				for(int i = 0; i < c2; ++i)
				{
					int z;
					int x = (ac2.x << 4) + world.rand.nextInt(16);
					int y = world.getHeight(x, z = (ac2.z << 4) + world.rand.nextInt(16)) > 0 ? world.getHeight(x, z) : 255;
					
					HCNetwork.manager.sendToAllAround(new PacketFXWisp2(x + world.rand.nextFloat(), y, z + world.rand.nextFloat(), x + world.rand.nextFloat(), y + 2 + world.rand.nextFloat(), z + world.rand.nextFloat(), .8F, 5), new WorldLocation(world, new BlockPos(x, y, z)).getPointWithRad(32));
				}
			}
			
			if(--counter > 0)
				continue;
			
			break;
		}
		
		if(noupdates && AuraHM.size() > 0)
		{
			for(AtmosphereChunk ac2 : c)
			{
				if(ac2.dimension != world.provider.getDimension())
					continue;
				ac2.updated = false;
			}
		}
	}
	
	public static void equalizeRadiation(AtmosphereChunk a, AtmosphereChunk b)
	{
		float diff = Math.abs(a.radiation - b.radiation);
		float maxShare = .0001F;
		
		if(diff >= maxShare)
		{
			if(a.radiation > b.radiation)
			{
				a.radiation -= maxShare;
				b.radiation += maxShare;
			} else if(b.radiation > a.radiation)
			{
				b.radiation -= maxShare;
				a.radiation += maxShare;
			}
		}
	}
	
	public static void GenerateAura(World world, Random random, int x, int z)
	{
		int tchance;
		int upper = LTConfigs.aura_max / 3;
		int lower = LTConfigs.aura_max / 5;
		boolean extraTaint = false;
		Biome bio = world.getBiomeProvider().getBiome(new BlockPos(x, 0, z));
		
		if(BIOME_LOWAURA.contains(bio))
		{
			upper = LTConfigs.aura_max / 8;
			lower = LTConfigs.aura_max / 20;
			if(bio == Biomes.HELL)
				extraTaint = true;
		} else if(BIOME_HIGHAURA.contains(bio))
		{
			upper = (int) ((float) LTConfigs.aura_max * 0.6f);
			lower = LTConfigs.aura_max / 3;
		} else if(BIOME_EXTREMEAURA.contains(bio))
		{
			upper = (int) ((float) LTConfigs.aura_max * 0.7f);
			lower = LTConfigs.aura_max / 2;
		} else if(BIOME_HIGHAURATAINTED.contains(bio))
		{
			upper = (int) ((float) LTConfigs.aura_max * 0.5f);
			lower = LTConfigs.aura_max / 3;
			extraTaint = true;
		}
		
		short auraStrength = (short) (lower + random.nextInt(upper - lower));
		short auraTaint = (short) (auraStrength / 3);
		int n = tchance = LTConfigs.taint_spawn == 2 ? 300 : 2200;
		if(LTConfigs.taint_spawn > 0 && random.nextInt(n) == 0)
		{
			auraStrength = (short) ((lower + random.nextInt(upper - lower)) / 2);
			auraTaint = LTConfigs.taint_spawn == 2 ? (short) ((float) LTConfigs.aura_max * 0.8f + (float) random.nextInt((int) ((float) LTConfigs.aura_max * 0.2f))) : (short) ((float) LTConfigs.aura_max * 0.5f + (float) random.nextInt((int) ((float) LTConfigs.aura_max * 0.2f)));
			GenerateTaintedArea(world, random, x - 8, z - 8, auraTaint);
		}
		if(extraTaint)
			auraTaint = (short) ((double) auraTaint * 1.5);
		AtmosphereChunk ac = new AtmosphereChunk();
		ac.vis = auraStrength;
		ac.taint = auraTaint;
		ac.x = x;
		ac.z = z;
		ac.dimension = world.provider.getDimension();
		ac.radiation = LTConfigs.aura_radMax / 2F;
		MinecraftForge.EVENT_BUS.post(new AuraEvent.Generate(ac, world, random));
		AddAuraToList(ac);
	}
	
	public static void SaveAuraData()
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(getAuraSaveFile().getAbsolutePath());
			GZIPOutputStream gzos = new GZIPOutputStream(fos);
			ObjectOutputStream out = new ObjectOutputStream(gzos);
			out.writeObject(AuraHM);
			out.writeObject(Monoliths);
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
			ArrayList monoliths = (ArrayList) in.readObject();
			in.close();
			AuraTicker.AuraHM = loaded;
			AuraTicker.Monoliths = monoliths;
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
				ArrayList monoliths = (ArrayList) in.readObject();
				in.close();
				AuraTicker.AuraHM = loaded;
				AuraTicker.Monoliths = monoliths;
				LostThaumaturgy.LOG.info("Converted " + AuraHM.size() + " legacy auras.");
			} catch(Exception e2)
			{
			}
		} catch(Exception e)
		{
			// empty catch block
		}
	}
	
	private static File getLegacyAuraSaveFile()
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		return new File((server.isDedicatedServer() ? "" : "saves" + File.separator) + server.getFolderName(), "pengu-lt.au");
	}
	
	public static File getAuraSaveFile() throws Exception
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		File lt = new File((server.isDedicatedServer() ? "" : "saves" + File.separator) + server.getFolderName(), "pengu-lt");
		if(!lt.isDirectory())
			lt.mkdir();
		return new File(lt, "aura.lt");
	}
	
	public static void GenerateTaintedArea(World world, Random random, int x, int z, short auraTaint)
	{
		for(int a = 0; a < 500; ++a)
		{
			int xx = x + random.nextInt(31) - random.nextInt(31);
			int zz = z + random.nextInt(31) - random.nextInt(31);
			increaseTaintedPlants(world, xx, world.getHeight(xx, zz), zz);
			
			Chunk c = world.getChunkFromBlockCoords(new BlockPos(x, 0, z));
			
			AtmosphereChunk ac = AuraHM.get(asKey(x, z, world.provider.getDimension()));
			
			if(ac == null || (float) ac.taint >= (float) auraTaint * 0.8f)
				continue;
			
			ac.taint = (short) ((float) auraTaint * (0.8f + random.nextFloat() * 0.25f));
		}
	}
	
	public static boolean taintifyChunk(World world, AtmosphereChunk ac)
	{
		int z;
		int x = (ac.x << 4) + world.rand.nextInt(16);
		int y = world.rand.nextInt(world.getHeight(x, z = (ac.z << 4) + world.rand.nextInt(16)) > 0 ? world.getHeight(x, z) : 256);
		if(increaseTaintedPlants(world, x, y, z))
			return true;
		return false;
	}
	
	public static boolean purifyChunk(World world, AtmosphereChunk ac)
	{
		int z;
		int x = (ac.x << 4) + world.rand.nextInt(16);
		int y = world.rand.nextInt(world.getHeight(x, z = (ac.z << 4) + world.rand.nextInt(16)) > 0 ? world.getHeight(x, z) : 256);
		if(decreaseTaintedPlants(world, x, y, z))
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
		Chunk c = world.getChunkFromBlockCoords(new BlockPos(x, y, z));
		for(int xx = -1; xx <= 1; ++xx)
		{
			for(int yy = -1; yy <= 1; ++yy)
			{
				for(int zz = -1; zz <= 1; ++zz)
				{
					BlockPos pos = new BlockPos(x + xx, y + yy, z + zz);
					TaintRegistry.cureBlock(world, pos);
				}
			}
		}
		return false;
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
					BlockPos pos = new BlockPos(x + a, y + b, z + c);
					if(b + y < 0 || b + y >= world.getHeight() || !world.isBlockLoaded(pos))
						continue;
					for(int q = 0; q < states.length; ++q)
						if(states[q].getBlock().equals(world.getBlockState(pos).getBlock()))
							return true;
				}
			}
		}
		return false;
	}
	
	public static boolean increaseTaintedPlants(World world, int x, int y, int z)
	{
		// TODO: silverwood and greatwood
		if(isNear(world, x, y, z, 3, new IBlockState[] { BlocksLT.SILVERWOOD_LOG.getDefaultState() }))
			return false;
		
		Chunk c = world.getChunkFromBlockCoords(new BlockPos(x, y, z));
		short taint = 0;
		AtmosphereChunk ac = (AtmosphereChunk) AuraHM.get(asKey(c.x, c.z, world.provider.getDimension()));
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
						
						if(world.rand.nextBoolean())
							for(int q = 0; q < world.rand.nextInt(25); ++q)
								HCNetwork.manager.sendToAllAround(new PacketFXWisp_AuraTicker_taintExplosion((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f, (float) x + 0.5f + (world.rand.nextFloat() - world.rand.nextFloat()) * 2.0f, (float) y + 0.5f + (world.rand.nextFloat() - world.rand.nextFloat()) * 2.0f, (float) z + 0.5f + (world.rand.nextFloat() - world.rand.nextFloat()) * 2.0f, 1.0f, 5), new TargetPoint(world.provider.getDimension(), x, y, z, 50));
					}
				}
			}
		}
		return false;
	}
	
	public static void taintExplosion(World w, int x, int y, int z)
	{
		w.createExplosion(null, x + 0.5f, y + 0.5f, z + 0.5f, 1.0f, false);
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
			int at = (int) Math.ceil(ic.getTaintedVis() * 3);
			Chunk c = world.getChunkFromBlockCoords(pos);
			AtmosphereChunk ac = (AtmosphereChunk) AuraHM.get(asKey(c.x, c.z, world.provider.getDimension()));
			if(ac != null)
			{
				ac.taint += at;
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
	
	public static void AddAuraToList(AtmosphereChunk ac)
	{
		LoadAuraData();
		AtmosphereChunk current = (AtmosphereChunk) AuraHM.get(asKey(ac.x, ac.z, ac.dimension));
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
	
	public static boolean decreaseClosestAura(World world, double posX, double posZ, int i)
	{
		AtmosphereChunk si = getAuraChunkFromBlockCoords(world, (int) posX, (int) posZ);
		if(si.vis >= i)
		{
			si.vis -= i;
			return true;
		} else
			for(int x = -1; x < 2; ++x)
				for(int z = -1; z < 2; ++z)
				{
					si = getAuraChunkFromBlockCoords(world, (int) posX + x * 16, (int) posZ + z * 16);
					if(si.vis >= i)
					{
						si.vis -= i;
						return true;
					}
				}
		return false;
	}
}