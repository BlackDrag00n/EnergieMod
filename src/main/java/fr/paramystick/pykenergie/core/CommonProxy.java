package fr.paramystick.pykenergie.core;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

public class CommonProxy
{
	private static final Map<String, NBTTagCompound> extendedEntityData = new HashMap<String, NBTTagCompound>();
	
	public static void storeEntityData(String name, NBTTagCompound compound)
	{
		extendedEntityData.put(name, compound);
	}

	public static NBTTagCompound getEntityData(String name)
	{
		return extendedEntityData.remove(name);
	}
	
	public void registerRender()
	{
		System.out.println("[Energie Mod] Methode executé côter SERVEUR");
	}
}
