package fr.paramystick.pykenergie.items;

import fr.paramystick.pykenergie.core.PyKEnergieMod;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemLogo extends Item
{
	
	@Override
	public void registerIcons(IIconRegister iconregister)
	{
		this.itemIcon = iconregister.registerIcon(PyKEnergieMod.MODID + ":logo");
	}	

}
