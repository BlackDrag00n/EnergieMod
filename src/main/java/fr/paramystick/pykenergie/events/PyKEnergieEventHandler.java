package fr.paramystick.pykenergie.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import fr.paramystick.pykenergie.core.CommonProxy;
import fr.paramystick.pykenergie.extendedplayer.ExtendedPlayerEnergie;

public class PyKEnergieEventHandler
{
	private CommonProxy proxy;


	/** Event lors de la construction d'entité. */
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
		// Money
		if (event.entity instanceof EntityPlayer && ExtendedPlayerEnergie.get((EntityPlayer) event.entity) == null)
		{
			ExtendedPlayerEnergie.register((EntityPlayer) event.entity);
		}
	}
	

	/** Event lors de la mort d'entité. Méthode permettant de conserver les données après la mort. Dans le cas du mana, elle n'est pas nécessaire si vous souhaitez que le joueur reparte avec 0 de mana. */
	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event)
	{
		if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer)
		{
			NBTTagCompound playerData = new NBTTagCompound();
			proxy.storeEntityData(((EntityPlayer) event.entity).getDisplayName(), playerData);
			
			// Money
			((ExtendedPlayerEnergie) (event.entity.getExtendedProperties(ExtendedPlayerEnergie.EXT_PROP_NAME))).saveNBTData(playerData);
			ExtendedPlayerEnergie.saveProxyData((EntityPlayer) event.entity);
			
		} else {
	
		}
	}

	
	/** Event lorsque l'entité join le serveur. */
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer)
		{
			NBTTagCompound playerData = proxy.getEntityData(((EntityPlayer) event.entity).getDisplayName());
			
			if (playerData != null)
			{
				// Money
				((ExtendedPlayerEnergie) (event.entity.getExtendedProperties(ExtendedPlayerEnergie.EXT_PROP_NAME))).loadNBTData(playerData);
			}

			// Money
			((ExtendedPlayerEnergie) (event.entity.getExtendedProperties(ExtendedPlayerEnergie.EXT_PROP_NAME))).sync();
			
		}
	}
}