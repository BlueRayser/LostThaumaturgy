package com.pengu.lostthaumaturgy.tile.monolith;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.net.HCNetwork;
import com.mrdimka.hammercore.tile.TileSyncableTickable;
import com.pengu.hammercore.utils.ListUtils;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.api.event.FillVoidChestEvent;
import com.pengu.lostthaumaturgy.block.monolith.BlockMonolithOpener;
import com.pengu.lostthaumaturgy.custom.aura.AuraTicker;
import com.pengu.lostthaumaturgy.custom.aura.SIAuraChunk;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.net.wisp.PacketMonolithWisp;
import com.pengu.lostthaumaturgy.tile.TileVoidChest;

public class TileMonolithOpener extends TileSyncableTickable
{
	public final Random rand = new Random(0L);
	
	@Override
	public void tick()
	{
		boolean opened = true;
		
		Chunk our = world.getChunkFromBlockCoords(pos);
		for(EnumFacing f : EnumFacing.VALUES)
		{
			if(f.getAxis() == Axis.Y)
				continue;
			BlockPos o = pos.offset(f);
			
			Chunk neighbor = world.getChunkFromBlockCoords(o);
			if(our != neighbor)
			{
				world.setBlockToAir(pos);
				return;
			}
			
			TileCrystalReceptacle recep = WorldUtil.cast(world.getTileEntity(o), TileCrystalReceptacle.class);
			if(recep == null)
			{
				BlockMonolithOpener.buildMonolith(world, pos);
				return;
			}
			
			if(!recep.INSERTED.get())
				opened = false;
		}
		
		if(opened)
		{
			SIAuraChunk si = AuraTicker.getAuraChunkFromBlockCoords(world, pos);
			if(si != null)
			{
				si.badVibes += 105;
				si.taint += 125;
			}
			world.setBlockToAir(pos);
			world.setBlockToAir(pos.down());
			world.setBlockState(pos.east(), BlocksLT.ELDRITCH_BLOCK.getDefaultState());
			world.setBlockState(pos.west(), BlocksLT.ELDRITCH_BLOCK.getDefaultState());
			world.setBlockState(pos.south(), BlocksLT.ELDRITCH_BLOCK.getDefaultState());
			world.setBlockState(pos.north(), BlocksLT.ELDRITCH_BLOCK.getDefaultState());
			
			for(int y = 0; y < 50; ++y)
			{
				int yc = -y;
				
				for(int x = -1; x < 2; ++x)
					for(int z = -1; z < 2; ++z)
					{
						BlockPos p = pos.add(x, yc, z);
						if(x == z && z == 0)
							world.setBlockToAir(p);
						else
							world.setBlockState(p, BlocksLT.ELDRITCH_BLOCK.getDefaultState());
					}
			}
			
			world.setBlockToAir(pos.down(49));
			world.setBlockToAir(pos.down(50));
			world.setBlockToAir(pos.down(51));
			
			for(int y = -5; y < 1; ++y)
				for(int x = -6; x < 7; ++x)
					for(int z = -6; z < 7; ++z)
					{
						BlockPos p = pos.add(x, y - 50, z);
						
						boolean wall = x == -6 || z == -6 || x == 6 || z == 6;
						wall |= y == -1 && (x == -5 || z == -5 || x == 5 || z == 5);
						wall |= y < -1 && (x == -5 || z == -5 || x == 5 || z == 5) && (x < -1 || x > 1) && (z < -1 || z > 1);
						wall |= y < 0 && (x == z || x == -z) && (x == -4 || x == 4);
						wall |= y == -4 && ((x == -4 && z == -2) || (x == -4 && z == 2) || (x == -4 && z == -3) || (x == -4 && z == 3) || (x == 4 && z == -2) || (x == 4 && z == 2) || (x == 4 && z == -3) || (x == 4 && z == 3));
						wall |= y == -4 && ((z == -4 && x == -2) || (z == -4 && x == 2) || (z == -4 && x == -3) || (z == -4 && x == 3) || (z == 4 && x == -2) || (z == 4 && x == 2) || (z == 4 && x == -3) || (z == 4 && x == 3));
						
						if(x == z && z == 0 && y == 0 && !wall)
							;
						else
							world.setBlockState(p, (y == -5 || y == 0 || wall ? BlocksLT.ELDRITCH_BLOCK : Blocks.AIR).getDefaultState());
					}
			
			List<BlockPos> positions = ListUtils.randomizeList(Arrays.asList(pos.add(-2, -54, -2), pos.add(-2, -54, 2), pos.add(2, -54, -2), pos.add(2, -54, 2)), world.rand);
			int chests = world.rand.nextInt(4) + 1;
			for(int i = 0; i < chests && !positions.isEmpty(); ++i)
			{
				BlockPos p = positions.remove(0);
				world.setBlockState(p, BlocksLT.VOID_CHEST.getDefaultState());
				TileVoidChest chest = WorldUtil.cast(world.getTileEntity(p), TileVoidChest.class);
				if(chest == null)
					world.setTileEntity(p, chest = new TileVoidChest());
				MinecraftForge.EVENT_BUS.post(new FillVoidChestEvent(world, p, chest));
			}
			
			for(EnumFacing f : EnumFacing.VALUES)
			{
				if(f.getAxis() == Axis.Y)
					continue;
				BlockPos p = pos.add(f.getFrontOffsetX() * 6, -53, f.getFrontOffsetZ() * 6);
				world.setBlockState(p.offset(f), BlocksLT.ELDRITCH_BLOCK.getDefaultState());
				world.setBlockState(p, BlocksLT.MONOLITH_EXTRA_ROOM.getDefaultState());
				TileExtraRoom room = WorldUtil.cast(world.getTileEntity(p), TileExtraRoom.class);
				if(room == null)
					world.setTileEntity(p, room = new TileExtraRoom());
				room.orientation.set(f.ordinal());
			}
			
			HammerCore.audioProxy.playSoundAt(world, LTInfo.MOD_ID + ":rumble", pos, 4F, 1F, SoundCategory.BLOCKS);
			HCNetwork.getManager("particles").sendToAllAround(new PacketMonolithWisp(pos), getSyncPoint(48));
		}
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
	}
}