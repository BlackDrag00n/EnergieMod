package fr.paramystick.pykenergie.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fr.minecraftforgefrance.ffmtlibs.network.PacketManager;
import fr.paramystick.pykenergie.events.PyKEnergieEventHandler;
import fr.paramystick.pykenergie.gui.PyKEnergieModTabs;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.paramystick.pykenergie.items.ItemGourde;
import fr.paramystick.pykenergie.items.ItemGourdeVide;

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
    public static Item itemGourdeVide; // Gourde Vide
    
    /**
     * Déclaration des proxy, on appel cette méthode dans la méthode init
     */
	@SidedProxy(clientSide = "fr.paramystick.pykenergie.core.ClientProxy", serverSide = "fr.paramystick.pykenergie.core.CommonProxy")
	public static CommonProxy proxy;

    /**
     * Envoie de packet
     */
	public static final PacketManager PyKEnergiepacketHandler = new PacketManager("fr.paramystick.pykenergie.networks", MODID, "pykenergiemod");

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
		itemGourde = new ItemGourde().setUnlocalizedName("gourde").setMaxStackSize(1).setCreativeTab(PyKEnergieModTabs);
		GameRegistry.registerItem(itemGourde, "item_gourde");
		/* --- */
		
		/* Ajout de la Gourde Vide*/
		itemGourdeVide = new ItemGourdeVide().setUnlocalizedName("gourdevide").setMaxStackSize(1).setCreativeTab(PyKEnergieModTabs);
		GameRegistry.registerItem(itemGourdeVide, "item_gourde_vide");
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
		MinecraftForge.EVENT_BUS.register(new PyKEnergieEventHandler());
		
		//Recette de craft
		GameRegistry.addRecipe(new ItemStack(itemGourdeVide), new Object[]{"XYX", "Y#Y", "XYX", 'X', Items.string, 'Y', Items.leather}); /* Item Gourde Vide */
		GameRegistry.addShapelessRecipe(new ItemStack(itemGourde), new Object[]{new ItemStack(PyKEnergieMod.itemGourdeVide, 1), new ItemStack(Items.potionitem, 1, 0), new ItemStack(Items.potionitem, 1, 0), new ItemStack(Items.potionitem, 1, 0)}); /* Item Gourde Vide */
		}

    /**
     * Permet de principalement d'interagir avec les autres mods.
     */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
}
