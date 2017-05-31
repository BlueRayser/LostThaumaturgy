package com.pengu.lostthaumaturgy.intr.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;

import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.block.BlockOreCrystal;
import com.pengu.lostthaumaturgy.init.BlocksLT;
import com.pengu.lostthaumaturgy.tile.TileCrystalOre;
import com.pengu.lostthaumaturgy.tile.TileLyingItem;
import com.pengu.lostthaumaturgy.tile.TileSingularityJar;
import com.pengu.lostthaumaturgy.tile.TileTaintedSoil;
import com.pengu.lostthaumaturgy.tile.TileVisPump;
import com.pengu.lostthaumaturgy.tile.monolith.TileCrystalReceptacle;
import com.pengu.lostthaumaturgy.tile.monolith.TileExtraRoom;
import com.pengu.lostthaumaturgy.tile.monolith.TileMonolith;

public class WailaLTProvider implements IWailaDataProvider
{
	public static void register(IWailaRegistrar reg)
	{
		WailaLTProvider provider = new WailaLTProvider();
		
		for(ResourceLocation r : Block.REGISTRY.getKeys())
		{
			if(r.getResourceDomain().equals(LTInfo.MOD_ID))
			{
				Block b = Block.REGISTRY.getObject(r);
				reg.registerBodyProvider(provider, b.getClass());
				reg.registerStackProvider(provider, b.getClass());
			}
		}
	}
	
	@Override
	public List<String> getWailaBody(ItemStack arg0, List<String> tooltip, IWailaDataAccessor acc, IWailaConfigHandler arg3)
	{
		TileEntity tile = acc.getTileEntity();
		
		soil: if(tile instanceof TileTaintedSoil)
		{
			TileTaintedSoil soil = (TileTaintedSoil) tile;
			try
			{
				BlockSnapshot s = soil.getSnapshot();
				Block block = Block.REGISTRY.getObject(s.getRegistryName());
				
				if(tooltip.contains("Tainted:"))
					break soil;
				tooltip.add("Tainted:");
				tooltip.add(block.getLocalizedName());
			} catch(Throwable err)
			{
				tooltip.add("Creative Mode placed soil.");
			}
		}
		
		if(tile instanceof TileSingularityJar)
		{
			TileSingularityJar jar = (TileSingularityJar) tile;
			String l = "XP: " + jar.storedXP.get();
			if(!tooltip.contains(l))
				tooltip.add(l);
		}
		
		return tooltip;
	}
	
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP arg0, TileEntity arg1, NBTTagCompound arg2, World arg3, BlockPos arg4)
	{
		return null;
	}
	
	@Override
	public List<String> getWailaHead(ItemStack arg0, List<String> tooltip, IWailaDataAccessor arg2, IWailaConfigHandler arg3)
	{
		return tooltip;
	}
	
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor acc, IWailaConfigHandler arg1)
	{
		Block block = acc.getBlock();
		if(block != null && block.getRegistryName().getResourceDomain().equals(LTInfo.MOD_ID))
		{
			if(block == BlocksLT.VIS_PURIFIER)
				return new ItemStack(BlocksLT.VIS_PURIFIER);
			
			if(block == BlocksLT.SILVERWOOD_LEAVES || block == BlocksLT.SILVERWOOD_LOG || block == BlocksLT.SILVERWOOD_STAIRS)
				return new ItemStack(block);
			
			if(acc.getTileEntity() != null)
			{
				ItemStack stack = Minecraft.getMinecraft().storeTEInStack(acc.getStack(), acc.getTileEntity());
				
				if(block instanceof BlockOreCrystal && acc.getTileEntity() instanceof TileCrystalOre)
				{
					TileCrystalOre ore = (TileCrystalOre) acc.getTileEntity();
					stack.setCount(ore.crystals.get());
					stack.setTagInfo("hash", new NBTTagLong(ore.getWorld().getSeed() + (ore.getWorld().provider.getDimension() + 2) + ore.getPos().toLong() + ((BlockOreCrystal) block).getCrystalColor()));
				} else
				
				if(acc.getTileEntity() instanceof TileVisPump)
				{
					TileVisPump pump = (TileVisPump) acc.getTileEntity();
					stack.setTagInfo("frames", new NBTTagInt(pump.ticksExisted));
					stack.setTagInfo("enabled", new NBTTagByte((byte) (!pump.gettingPower() ? 1 : 0)));
				}
				
				if(acc.getTileEntity() instanceof TileLyingItem)
				{
					TileLyingItem tile = (TileLyingItem) acc.getTileEntity();
					return tile.lying.get().copy();
				}
				
				if(acc.getTileEntity() instanceof TileMonolith || acc.getTileEntity() instanceof TileCrystalReceptacle || acc.getTileEntity() instanceof TileExtraRoom)
					return new ItemStack(BlocksLT.ELDRITCH_BLOCK);
				
				stack.removeSubCompound("display");
				return stack;
			}
		}
		
		return acc.getStack();
	}
	
	@Override
	public List<String> getWailaTail(ItemStack arg0, List<String> tooltip, IWailaDataAccessor arg2, IWailaConfigHandler arg3)
	{
		return tooltip;
	}
}