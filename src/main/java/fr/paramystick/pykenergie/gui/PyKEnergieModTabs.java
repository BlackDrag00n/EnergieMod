package fr.paramystick.pykenergie.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.paramystick.pykenergie.core.PyKEnergieMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class PyKEnergieModTabs extends CreativeTabs
{
	public PyKEnergieModTabs(int id, String lable)
	{
		super(lable);
		// TODO Auto-generated constructor stub
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		// TODO Auto-generated method stub
		return PyKEnergieMod.itemLogo;
	}
}
