package fr.paramystick.energie.core;

import net.minecraft.item.Item;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.paramystick.energie.items.ItemGourde;

@Mod(modid = "modenergie", name = "Energie Mod", version = "0.0.1", dependencies = "required-after:ffmtlibs")
public class Energie
{
	/**
	 * L'instance du mod
	 */
	@Instance("modenergie")
	public static Energie instance;
    public static final String MODID = "modenergie";
    public static Item itemGourde; // Gourde
    
    /**
     * Déclaration des proxy, on appel cette méthode dans la méthode init
     */
	@SidedProxy(clientSide = "fr.paramystick.energie.core.ClientProxy", serverSide = "fr.paramystick.energie.core.CommonProxy")
	public static CommonProxy proxy;
	
    /**
     * Elle va servir pour lire le fichier de configuration, enregistrer les blocs, les items, la plupart des choses qui se trouve dans le GameRegistry.
     */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		/* Ajout d'une Gourde */
		itemGourde = new ItemGourde().setUnlocalizedName("gourde");
		GameRegistry.registerItem(itemGourde, "item_gourde");
		/* --- */
	
	}

    /**
     * Toutes les choses secondaires iront ici, enregistrement des events, des rendus, des recettes, etc ...
     */
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		// Appel de la méthode pour afficher dans les logs si on est côté client ou serveur
		proxy.registerRender();
	}

    /**
     * Permet de principalement d'interagir avec les autres mods.
     */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	
	}
}
