package fr.paramystick.pykenergie.core;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fr.minecraftforgefrance.ffmtlibs.network.PacketManager;
import fr.paramystick.pykenergie.blocs.BlockCafetiere;
import fr.paramystick.pykenergie.events.PyKEnergieEvent;
import fr.paramystick.pykenergie.gui.PyKEnergieModTabs;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.paramystick.pykenergie.items.ItemCafeBouteille;
import fr.paramystick.pykenergie.items.ItemCafeSeau;
import fr.paramystick.pykenergie.items.ItemGourdeEau;
import fr.paramystick.pykenergie.items.ItemGourdeVide;
import fr.paramystick.pykenergie.items.ItemLogo;

@Mod(modid = "pykenergiemod", name = "PyK Energie Mod", version = "0.0.1", dependencies = "required-after:ffmtlibs")
public class PyKEnergieMod
{
	/**
	 * L'instance du mod
	 */
	@Instance("pykenergiemod")
	public static PyKEnergieMod instance;
    public static final String MODID = "pykenergiemod";
    
    /**
     * Déclaration des ITEMS
     */
    public static Item itemLogo; // Logo du mod
    public static Item itemGourdeVide; // Gourde Vide
    public static Item itemGourdeEau; // Gourde d'eau
    public static Item itemCafeBouteille; // Bouteille de café
    public static Item itemCafeSeau; // Seau de café
    
    /**
     * Déclaration des BLOCKS
     */
    public static Block blockCafetiere;
    
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

	    /**
	     * Pré-initialisation des ITEMS
	     */
		/* Ajout du logo onglet Creative */
		itemLogo = new ItemLogo().setUnlocalizedName("logo").setMaxStackSize(1).setCreativeTab(PyKEnergieModTabs);
		GameRegistry.registerItem(itemLogo, "item_logo");
		/* --- */
		
		/* Ajout de la Gourde */
		itemGourdeEau = new ItemGourdeEau().setUnlocalizedName("gourdeeau").setMaxStackSize(1).setCreativeTab(PyKEnergieModTabs);
		GameRegistry.registerItem(itemGourdeEau, "item_gourde_eau");
		/* --- */
		
		/* Ajout de la Gourde Vide*/
		itemGourdeVide = new ItemGourdeVide().setUnlocalizedName("gourdevide").setMaxStackSize(1).setCreativeTab(PyKEnergieModTabs);
		GameRegistry.registerItem(itemGourdeVide, "item_gourde_vide");
		/* --- */
		
		/* Ajout de la Bouteille de café */
		itemCafeBouteille = new ItemCafeBouteille().setUnlocalizedName("cafebouteille").setMaxStackSize(1).setCreativeTab(PyKEnergieModTabs);
		GameRegistry.registerItem(itemCafeBouteille, "item_cafe_bouteille");
		/* --- */
		
		/* Ajout du seau de café */
		itemCafeSeau = new ItemCafeSeau().setUnlocalizedName("cafeseau").setMaxStackSize(1).setCreativeTab(PyKEnergieModTabs);
		GameRegistry.registerItem(itemCafeSeau, "item_cafe_seau");
		/* --- */

	    /**
	     * Pré-initialisation des BLOCKS
	     */
		/* Ajout de la cafetiere */
		blockCafetiere = new BlockCafetiere(Material.rock).setBlockName("cafetiere").setBlockTextureName(MODID + ":block_cafetiere").setCreativeTab(PyKEnergieModTabs);
		GameRegistry.registerBlock(blockCafetiere, "block_cafetiere");
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
		// Tracker du joueur
		FMLCommonHandler.instance().bus().register(new PyKEnergieEvent());
		MinecraftForge.EVENT_BUS.register(new PyKEnergieEvent());
		
		//Recette de craft
		/* Item Gourde Vide */	GameRegistry.addRecipe(new ItemStack(itemGourdeVide), new Object[]{"XYX", "Y#Y", "XYX", 'X', Items.string, 'Y', Items.leather});
		/* Item Gourde Eau */	GameRegistry.addShapelessRecipe(new ItemStack(itemGourdeEau), new Object[]{new ItemStack(PyKEnergieMod.itemGourdeVide, 1), new ItemStack(Items.potionitem, 1, 0), new ItemStack(Items.potionitem, 1, 0), new ItemStack(Items.potionitem, 1, 0)});
		/* Item Seau Café */	GameRegistry.addShapelessRecipe(new ItemStack(itemCafeSeau), new Object[]{new ItemStack(Items.bucket, 1), new ItemStack(PyKEnergieMod.itemCafeBouteille, 1), new ItemStack(PyKEnergieMod.itemCafeBouteille, 1), new ItemStack(PyKEnergieMod.itemCafeBouteille, 1)});
		}

    /**
     * Permet de principalement d'interagir avec les autres mods.
     */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
}
