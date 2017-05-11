package com.pengu.lostthaumaturgy.api.tiles;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;

public interface IInfuser
{
	@Nullable
	EntityPlayer getInitiator();
}