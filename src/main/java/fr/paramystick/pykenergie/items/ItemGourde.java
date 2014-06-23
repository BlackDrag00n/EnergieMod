package fr.paramystick.pykenergie.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.paramystick.pykenergie.core.PyKEnergieMod;
import fr.paramystick.pykenergie.extendedplayer.ExtendedPlayerEnergie;
import fr.paramystick.pykenergie.gui.PyKEnergieModTabs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemGourde extends Item
{
	public float EnergieGagnerGourde = 100f; // Variable qui définie de combien on remontre l'energie
	
	public ItemGourde()
	{
		super();
		this.setMaxStackSize(1);
		this.setCreativeTab(PyKEnergieMod.PyKEnergieModTabs);
	}
		
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
		ExtendedPlayerEnergie prop = ExtendedPlayerEnergie.get(par3EntityPlayer);
		
        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            --par1ItemStack.stackSize;
        }

        // L'item a été bu
        if (!par2World.isRemote)
        {
        	//par3EntityPlayer.clearActivePotions();
        	prop.addEnergie(EnergieGagnerGourde); // On a bu donc on remonte l'energie
        }

        return par1ItemStack.stackSize <= 0 ? new ItemStack(PyKEnergieMod.itemGourdeVide) : par1ItemStack;
    }
	
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }

    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }	
	
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        return par1ItemStack;
    }
    
    @SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconregister)
	{
		this.itemIcon = iconregister.registerIcon(PyKEnergieMod.MODID + ":itemgourde");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}