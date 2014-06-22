package fr.paramystick.pykenergie.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.nbt.NBTTagCompound;
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

public class PyKPlayerTracker
{
	private CommonProxy proxy;
	public int timerEnergie = 0;

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
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		Minecraft mc = FMLClientHandler.instance().getClient();
		EntityPlayer player = mc.thePlayer;
		ExtendedPlayerEnergie prop = ExtendedPlayerEnergie.get(event.player);
		
		if(player.isSprinting())
		{
			prop.setEnergie(prop.Energie - 0.5f); // 0.01
		}
		else
		{
			if (timerEnergie == 60)
			{
				prop.setEnergie(prop.Energie - 0.00001f);
				timerEnergie = 0;
			}
			else
				timerEnergie++;
		}
		
		if(player.isPlayerSleeping())
		{
			prop.setEnergie(prop.Energie + 0.02f);
			
			if(player.getSleepTimer() >= 99)
			{
				prop.setEnergie(100f);
			}
			mc.thePlayer.addChatMessage(new ChatComponentText("[DEBUG] getSleepTimer: "+player.isPlayerFullyAsleep()));
		}
		
	}
	
	@SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlayerWakeUpEvent(PlayerSleepInBedEvent event)
	{
		Minecraft mc = FMLClientHandler.instance().getClient();
		EntityPlayer player = mc.thePlayer;
		ExtendedPlayerEnergie prop = ExtendedPlayerEnergie.get(player);
		
		if(player.isPlayerFullyAsleep())
		{
			prop.setEnergie(100.00f);
		}
	}
     
}
