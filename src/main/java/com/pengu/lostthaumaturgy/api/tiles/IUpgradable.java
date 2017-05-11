package com.pengu.lostthaumaturgy.api.tiles;

public interface IUpgradable
{
	public boolean canAcceptUpgrade(byte type);
	public boolean hasUpgrade(byte type);
	public int getUpgradeLimit();
	public byte[] getUpgrades();
	public boolean setUpgrade(byte type);
	public boolean clearUpgrade(int slot);
}