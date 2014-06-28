package fr.paramystick.pykenergie.events;

import net.java.games.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BlockEvent;
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
	public int timerCompteurMax = 20; // 20 = Toutes les secondes
	
	public float EnergiePerdu = 0.00001f; 		// - 0.00001f	[Fonctionnel]
	
	public float EnergiePerduSauter = 0.03f; 	// - 0.03f		[Fonctionnel]
	
	public float EnergiePerduCourir = 0.05f; 	// - 0.05f		[Fonctionnel]
	public float EnergiePerduAttaquer = 10f;	// - 0.05f		[Fonctionnel] (Triche Valeur/2 car bug 2x energie enlever, innexplicable)
	public float EnergiePerduMiner = 20f;		// - 0.05f		[A faire]
	
	public float EnergiePerduNager = 0.1f;		// - 0.1f		[Fonctionnel]
	public float EnergiePerduAttaque = 1f;		// - 0.1f		[Fonctionnel]
	
	public float EnergieGagnerDormir = 0.2f;	// + 0.2f		[Fonctionnel]
	
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
		
		// Compteur atteint on vérifie (Timer d'energie) - Coté serveur seulement (si sur serveur)
		if (!event.player.worldObj.isRemote && (timerEnergie == timerCompteurMax))
		{
			// Joueur sans activité (ne cour pas et ne nage pas)
			if(!event.player.isSprinting() && !(event.player.isInWater() && isPlayerMoving(event.player)))
				prop.removeEnergie(EnergiePerdu);
			
			// Joueur cours
			if(event.player.isSprinting())
				prop.removeEnergie(EnergiePerduCourir);
			
			// Joueur nage
			if(event.player.isInWater() && isPlayerMoving(event.player))
				prop.removeEnergie(EnergiePerduNager);
			
			// Joueur allongé
			if(event.player.isPlayerSleeping())
			{
				prop.addEnergie(EnergieGagnerDormir);
				event.player.addChatMessage(new ChatComponentText("[DEBUG] addEnergie: "+EnergieGagnerDormir));
				
				if(event.player.isPlayerFullyAsleep())
					prop.setEnergie(100f);
			}
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
	
	// Joueur saute
	@SubscribeEvent
	public void onSaut(LivingJumpEvent event)
	{
		// C'est un joueur
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			ExtendedPlayerEnergie prop = ExtendedPlayerEnergie.get(player);
			
			if(!player.worldObj.isRemote)
				prop.removeEnergie(EnergiePerduSauter);
		}
	}
	
	// Joueur attaquer ou attaque
	@SubscribeEvent
	public void onAttaque(LivingAttackEvent event)
	{
	  // Le joueur attaquant = perd 5 d'energie
	  if (event.source.getEntity() != null && event.source.getEntity() instanceof EntityPlayer)
	  {
	    EntityPlayer player = (EntityPlayer) event.source.getEntity();
	    ExtendedPlayerEnergie prop = ExtendedPlayerEnergie.get(player);

	    if(!player.worldObj.isRemote)
	      prop.removeEnergie(EnergiePerduAttaque);
	  }

	  // Un mob attaque le joueur = le joueur attaquer perd 2 d'energie
	  if (event.source.getEntity() != null && event.entity instanceof EntityPlayer)
	  {
	    EntityPlayer player = (EntityPlayer) event.entity;
	    ExtendedPlayerEnergie prop = ExtendedPlayerEnergie.get(player);

	    if(!player.worldObj.isRemote)
	      prop.removeEnergie(EnergiePerduAttaquer/2); //(Triche Valeur/2 car bug 2x energie enlever, innexplicable)
	  }
	}
	
	// Joueur Mine
	@SubscribeEvent
	public void onMinage(BlockEvent.BreakEvent event)
	{		
		EntityPlayer player = (EntityPlayer) event.getPlayer();
		ExtendedPlayerEnergie prop = ExtendedPlayerEnergie.get(player);
			
		if(!player.worldObj.isRemote)
			prop.removeEnergie(EnergiePerduMiner);
	}
}
