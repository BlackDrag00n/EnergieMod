package fr.paramystick.pykenergie.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.paramystick.pykenergie.core.CommonProxy;
import fr.paramystick.pykenergie.extendedplayer.ExtendedPlayerEnergie;

public class PyKEnergieEvent
{
	private CommonProxy proxy;
	public int timerEnergie = 0;

	/** Event lors de la construction d'entité. */
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
		// Energie
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
			
			// Energie
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
				// Energie
				((ExtendedPlayerEnergie) (event.entity.getExtendedProperties(ExtendedPlayerEnergie.EXT_PROP_NAME))).loadNBTData(playerData);
			}

			// Energie
			((ExtendedPlayerEnergie) (event.entity.getExtendedProperties(ExtendedPlayerEnergie.EXT_PROP_NAME))).sync();
			
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		ExtendedPlayerEnergie prop = ExtendedPlayerEnergie.get(event.player);
				
		if(event.player.isSprinting())
		{
			if(prop.getEnergie() >= 0)
				prop.setEnergie(prop.Energie - 0.01f); // 0.01f
			if(prop.getEnergie() < 0)
				prop.setEnergie(0);
		}
		else
		{
			if (timerEnergie == 60)
			{
				if(prop.getEnergie() >= 0)
					prop.setEnergie(prop.Energie - 0.00001f); //0.00001f
				if(prop.getEnergie() < 0)
					prop.setEnergie(0);
				timerEnergie = 0;
			}
			else
				timerEnergie++;
		}
		
		if(event.player.isPlayerSleeping())
		{
			if(prop.getEnergie() <= 100)
				prop.setEnergie(prop.Energie + 0.2f);
			if(prop.getEnergie() > 100)
				prop.setEnergie(100f);
			
			if(event.player.isPlayerFullyAsleep())
				prop.setEnergie(100f);
		}
		
		if(prop.getEnergie() <= 5)
			event.player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 19, 3, true));
		else if(prop.getEnergie() <= 10)
			event.player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 19, 2, true));
		else if(prop.getEnergie() <= 15)
			event.player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 19, 1, true));
		else if(prop.getEnergie() <= 20)
			event.player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 19, 0, true));

		//mc.thePlayer.addChatMessage(new ChatComponentText("[DEBUG] getEnergie: "+prop.getEnergie()));
	}     
}
