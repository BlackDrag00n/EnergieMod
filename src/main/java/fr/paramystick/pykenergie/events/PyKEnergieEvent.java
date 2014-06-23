package fr.paramystick.pykenergie.events;

import net.java.games.input.Keyboard;
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
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
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
	public int timerCompteurMax = 60;
	
	public float EnergiePerdu = 0.01f; 		//0.00001f	[Fonctionnel]
	public float EnergiePerduCourir = 1f; 	// 0.01f	[Fonctionnel]
	public float EnergiePerduSauter = 5f; 	// 0.1f		[A faire]
	public float EnergiePerduNager = 10f;	// 0.1f		[Fonctionnel]
	public float EnergiePerduMiner = 20f;	// 0.1f		[A faire]
	public float EnergiePerduAttaquer = 50f;// 0.1f		[A faire]
	public float EnergieGagnerDormir = 0.2f;// 0.2f		[Fonctionnel]
	
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
	
	// Vérifier si le joueur est en mouvement dans l'eau (en train de nager)
	public static boolean isPlayerMoving(EntityPlayer player)
	{
	    return player.motionX != 0 || player.motionZ != 0;
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
		
		if (timerEnergie == timerCompteurMax)
			timerEnergie = 0;
		else
			timerEnergie++;
		
		// Si le joueur ne [cour] [nage] pas
		if(!event.player.isSprinting() && !(event.player.isInWater() && isPlayerMoving(event.player)))
		{
			if (timerEnergie == timerCompteurMax)
			{
				prop.removeEnergie(EnergiePerdu);
			}
		}
		
		// Si le joueur cours
		if(event.player.isSprinting())
		{
			prop.removeEnergie(EnergiePerduCourir);
		}
		
		// Si le joueur Nage
		if(event.player.isInWater() && isPlayerMoving(event.player))
		{
			if (timerEnergie == timerCompteurMax)
			{
				prop.removeEnergie(EnergiePerduNager);
			}
		}
		
		// Si le joueur est allongé
		if(event.player.isPlayerSleeping())
		{
			prop.addEnergie(EnergieGagnerDormir);
			
			if(event.player.isPlayerFullyAsleep())
				prop.setEnergie(100f);
		}
		
		
		// Effet si energie faible
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
