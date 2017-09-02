package com.pengu.lostthaumaturgy.api.research;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;

import com.google.common.io.Files;
import com.pengu.lostthaumaturgy.LostThaumaturgy;
import com.pengu.lostthaumaturgy.api.event.PlayerDataEvent;

public class ResearchManager
{
	public static void loadPlayerData(EntityPlayer player, File file1, File file2, boolean legacy)
	{
		try
		{
			FileInputStream fileinputstream;
			NBTTagCompound data = null;
			if(file1 != null && file1.exists())
			{
				try
				{
					fileinputstream = new FileInputStream(file1);
					data = CompressedStreamTools.readCompressed((InputStream) fileinputstream);
					fileinputstream.close();
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			if(file1 == null || !file1.exists() || data == null || data.hasNoTags())
			{
				LostThaumaturgy.LOG.warn("LT data not found for " + player.getName() + ". Trying to load backup data.");
				if(file2 != null && file2.exists())
				{
					try
					{
						fileinputstream = new FileInputStream(file2);
						data = CompressedStreamTools.readCompressed((InputStream) fileinputstream);
						fileinputstream.close();
					} catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			
			if(data != null)
			{
				ArrayList<String> comp = ResearchSystem.COMPLETED.getOrDefault(player, new ArrayList<>());
				NBTTagList list = data.getTagList("Research", NBT.TAG_STRING);
				for(int i = 0; i < list.tagCount(); ++i)
					comp.add(list.getStringTagAt(i));
				
				/** Give all auto-unlock researches */
				Collection<ResearchItem> items = new ArrayList<ResearchItem>();
				ResearchCategories.researchCategories.values().forEach(cl -> items.addAll(cl.research.values()));
				items.stream().filter(r -> r.isAutoUnlock()).forEach(r ->
				{
					if(!comp.contains(r.key) && canUnlockResearch(player.getName(), r))
						comp.add(r.key);
				});
				
				ResearchSystem.COMPLETED.put(player.getName(), comp);
				MinecraftForge.EVENT_BUS.post(new PlayerDataEvent.Read(player, data));
			}
		} catch(Exception exception1)
		{
			exception1.printStackTrace();
			LostThaumaturgy.LOG.error("Error loading LT data");
		}
	}
	
	public static boolean savePlayerData(EntityPlayer player, File file1, File file2)
	{
		boolean success;
		success = true;
		try
		{
			NBTTagCompound data = new NBTTagCompound();
			
			NBTTagList list = new NBTTagList();
			for(String r : ResearchSystem.getResearchForPlayer(player.getName()))
				list.appendTag(new NBTTagString(r));
			data.setTag("Research", list);
			
			MinecraftForge.EVENT_BUS.post(new PlayerDataEvent.Write(player, data));
			
			if(file1 != null && file1.exists())
			{
				try
				{
					Files.copy(file1, file2);
				} catch(Exception e)
				{
					LostThaumaturgy.LOG.error("Could not backup old research file for player " + player.getName());
				}
			}
			try
			{
				if(file1 != null)
				{
					FileOutputStream fileoutputstream = new FileOutputStream(file1);
					CompressedStreamTools.writeCompressed((NBTTagCompound) data, (OutputStream) fileoutputstream);
					fileoutputstream.close();
				}
			} catch(Exception e)
			{
				LostThaumaturgy.LOG.error("Could not save research file for player " + player.getName());
				if(file1.exists())
				{
					try
					{
						file1.delete();
					} catch(Exception e2)
					{
						// empty catch block
					}
				}
				success = false;
			}
		} catch(Exception exception1)
		{
			exception1.printStackTrace();
			LostThaumaturgy.LOG.error("Error saving data");
			success = false;
		}
		return success;
	}
	
	public static boolean isResearchComplete(String player, String string)
	{
		ArrayList<String> researches = ResearchSystem.getResearchForPlayer(player);
		return researches != null && researches.contains(string);
	}
	
	@Nullable
	public static ResearchItem getById(String string)
	{
		Collection<ResearchItem> items = new ArrayList<ResearchItem>();
		ResearchCategories.researchCategories.values().forEach(cl -> items.addAll(cl.research.values()));
		Optional<ResearchItem> found = items.stream().filter(ri -> ri.key.equals(string)).findAny();
		return found.isPresent() ? found.get() : null;
	}
	
	@Nullable
	public static ResearchItem chooseRandomUnresearched(ItemStack baseStack, EntityPlayer initiator, int attempts)
	{
		Collection<ResearchItem> items = new ArrayList<ResearchItem>();
		ResearchCategories.researchCategories.values().forEach(cl -> items.addAll(cl.research.values()));
		
		ArrayList<ResearchItem> newResearches = new ArrayList<>(items);
		newResearches.removeIf(t -> isResearchComplete(initiator.getName(), t.key));
		
		if(!newResearches.isEmpty())
			do
			{
				ResearchItem r = newResearches.get(initiator.getRNG().nextInt(newResearches.size()));
				float gen = initiator.getRNG().nextInt(100000) / 1000F;
				if(gen < r.getComplexity() || !r.canObtainFrom(baseStack, initiator))
					continue;
				return r;
			} while(attempts-- > 0);
		
		return null;
	}
	
	public static boolean canUnlockResearch(String player, ResearchItem res)
	{
		String[] arr$;
		int len$;
		int i$;
		String pt;
		ResearchItem parent;
		
		if(res.parents != null && res.parents.length > 0)
		{
			arr$ = res.parents;
			len$ = arr$.length;
			
			for(i$ = 0; i$ < len$; ++i$)
			{
				pt = arr$[i$];
				parent = ResearchCategories.getResearch(pt);
				if(parent != null && !isResearchComplete(player, parent.key))
					return false;
			}
		}
		
		if(res.parentsHidden != null && res.parentsHidden.length > 0)
		{
			arr$ = res.parentsHidden;
			len$ = arr$.length;
			
			for(i$ = 0; i$ < len$; ++i$)
			{
				pt = arr$[i$];
				parent = ResearchCategories.getResearch(pt);
				if(parent != null && !isResearchComplete(player, parent.key))
					return false;
			}
		}
		
		return true;
	}
}