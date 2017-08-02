package com.pengu.lostthaumaturgy.api.restorer;

import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.item.Item;

import com.pengu.hammercore.utils.IndexedMap;
import com.pengu.hammercore.utils.NPEUtils;

public class RestorerManager
{
	private static final Map<Item, ICustomRepairable> custom = new IndexedMap<>();
	private static final DefaultRepairGetter getter = new DefaultRepairGetter();
	
	public static void bindCustomRepairable(Item item, ICustomRepairable repairable)
	{
		NPEUtils.checkNotNull(item, "item can not be null!");
		NPEUtils.checkNotNull(repairable, "repairable can not be null!");
		custom.put(item, repairable);
	}
	
	@Nonnull
	public static ICustomRepairable findCustomRepairable(Item item)
	{
		NPEUtils.checkNotNull(item, "item can not be null!");
		ICustomRepairable r = custom.get(item);
		if(r != null)
			return r;
		return getter;
	}
	
	public static Map<Item, ICustomRepairable> getCustomRepairables()
	{
		return custom;
	}
}