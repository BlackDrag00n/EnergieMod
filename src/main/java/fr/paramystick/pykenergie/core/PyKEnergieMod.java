package fr.paramystick.pykenergie.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fr.paramystick.pykenergie.gui.PyKEnergieModTabs;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.paramystick.pykenergie.items.ItemGourde;

@Mod(modid = "pykenergiemod", name = "PyK Energie Mod", version = "0.0.1", dependencies = "required-after:ffmtlibs")
public class PyKEnergieMod
{
	/**
	 * L'instance du mod
	 */
	@Instance("pykenergiemod")
	public static PyKEnergieMod instance;
    public static final String MODID = "pykenergiemod";
    public static Item itemGourde; // Gourde
    
    /**
     * Déclaration des proxy, on appel cette méthode dans la méthode init
     */
	@SidedProxy(clientSide = "fr.paramystick.pykenergie.core.ClientProxy", serverSide = "fr.paramystick.pykenergie.core.CommonProxy")
	public static CommonProxy proxy;

    /**
     * Déclaration des tables creatives
     */ 
	public static CreativeTabs PyKEnergieModTabs = new PyKEnergieModTabs(CreativeTabs.getNextID(), "PyKEnergieModTabs");
	
    /**
     * Elle va servir pour lire le fichier de configuration, enregistrer les blocs, les items, la plupart des choses qui se trouve dans le GameRegistry.
     */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		/* Ajout de la Gourde */
		itemGourde = new ItemGourde().setUnlocalizedName("gourde").setMaxStackSize(1);
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
		
		//Recette de craft
		/* Item_Gourde */ GameRegistry.addRecipe(new ItemStack(itemGourde), new Object[]{"XYX", "Y#Y", "XYX", 'X', Items.string,
			'Y', Items.leather});
	}

    /**
     * Permet de principalement d'interagir avec les autres mods.
     */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
}
