package fr.paramystick.energie.items;

import fr.paramystick.energie.core.Energie;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemGourde extends Item
{
	
	@Override
	public void registerIcons(IIconRegister iconregister)
	{
		
		this.itemIcon = iconregister.registerIcon(Energie.MODID + ":itemgourde");

	}
	
}