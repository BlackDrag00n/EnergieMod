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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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
		
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer payer)
    {
		ExtendedPlayerEnergie prop = ExtendedPlayerEnergie.get(payer);
		
        if (!payer.capabilities.isCreativeMode)
        {
            --stack.stackSize;
        }

        // L'item a été bu
        if (!world.isRemote)
        {
        	//par3EntityPlayer.clearActivePotions();
        	prop.addEnergie(EnergieGagnerGourde); // On a bu donc on remonte l'energie
        }

        return stack.stackSize <= 0 ? new ItemStack(PyKEnergieMod.itemGourdeVide) : stack;
    }
	
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 32;
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.drink;
    }	
	
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
    	player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        return stack;
    }
    
    @SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconregister)
	{
		this.itemIcon = iconregister.registerIcon(PyKEnergieMod.MODID + ":itemgourde");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}