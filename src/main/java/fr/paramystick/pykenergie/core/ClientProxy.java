package fr.paramystick.pykenergie.core;

import fr.paramystick.pykenergie.gui.PyKEnergieModGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
	public void registerRender()
	{
		System.out.println("[Energie Mod] Methode executé côter CLIENT");
		// Coté client seulement l'affichage
		MinecraftForge.EVENT_BUS.register(new PyKEnergieModGui(Minecraft.getMinecraft()));
	}
}
