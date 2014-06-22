package fr.paramystick.pykenergie.extendedplayer;

import fr.paramystick.pykenergie.core.CommonProxy;
import fr.paramystick.pykenergie.core.PyKEnergieMod;
import fr.paramystick.pykenergie.networks.PacketEnergie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedPlayerEnergie implements IExtendedEntityProperties
{
	//Identifiant à la propriété
	public final static String EXT_PROP_NAME = "extPlayPyKEnergie";
	private final EntityPlayer player;
	public float Energie, maxEnergie;
	//-----------------------------------------------------------------------------------------
	
	//Le constructeur. Il prend en paramètre le player auquel nous allons toucher.
	public ExtendedPlayerEnergie(EntityPlayer player)
	{
		this.player = player;
		this.Energie = 100;
		this.maxEnergie = 100;
	}
	//-----------------------------------------------------------------------------------------
	
	//méthodes concernant la sauvegarde et l'obtention de ces données
	public static final void register(EntityPlayer player)
	{
		player.registerExtendedProperties(ExtendedPlayerEnergie.EXT_PROP_NAME, new ExtendedPlayerEnergie(player));
	}

	public static final ExtendedPlayerEnergie get(EntityPlayer player)
	{
		return (ExtendedPlayerEnergie) player.getExtendedProperties(EXT_PROP_NAME);
	}
	//-----------------------------------------------------------------------------------------
	
	// Synchronisation client/serveur
	public final void sync() {
		PacketEnergie packetEnergie = new PacketEnergie(this.maxEnergie, this.Energie);
		//La ligne suivante dépend de votre manière d'envoyer les packets. Celle-ci vient de mon mod, je ne la changerais pas car je ne peux l'appliquer à votre mod, mais elle reste bonne pour un exemple.
		PyKEnergieMod.PyKEnergiepacketHandler.sendToServer(packetEnergie);

		if (!player.worldObj.isRemote) {
			EntityPlayerMP player1 = (EntityPlayerMP) player;
			//Ici, même chose que précédemment, sauf que le packet est envoyé au player.
			PyKEnergieMod.PyKEnergiepacketHandler.sendTo(packetEnergie, player1);
		}
	}
	//-----------------------------------------------------------------------------------------
	
	//Encore une méthode de sauvegarde
	private static String getSaveKey(EntityPlayer player)
	{
		return player.getDisplayName() + ":" + EXT_PROP_NAME;
	}
	//-----------------------------------------------------------------------------------------
	
	public static void saveProxyData(EntityPlayer player) {
		ExtendedPlayerEnergie playerData = ExtendedPlayerEnergie.get(player);
		NBTTagCompound savedData = new NBTTagCompound();
		
		playerData.saveNBTData(savedData);
		CommonProxy.storeEntityData(getSaveKey(player), savedData);
	}
		
	public static void loadProxyData(EntityPlayer player) {
		ExtendedPlayerEnergie playerData = ExtendedPlayerEnergie.get(player);
		NBTTagCompound savedData = CommonProxy.getEntityData(getSaveKey(player));
		
		if (savedData != null)
		{
			playerData.loadNBTData(savedData);
		}
		
		playerData.sync();
	}
	//-----------------------------------------------------------------------------------------
	
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		// TODO Auto-generated method stub
		NBTTagCompound properties = new NBTTagCompound();

		properties.setFloat("Energie", this.Energie);
		properties.setFloat("maxEnergie", this.maxEnergie);

		compound.setTag(EXT_PROP_NAME, properties);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		// TODO Auto-generated method stub
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
		this.Energie = properties.getLong("Energie");
		this.maxEnergie = properties.getLong("maxEnergie");

	}

	@Override
	public void init(Entity entity, World world)
	{
		// TODO Auto-generated method stub
		
	}
	//-----------------------------------------------------------------------------------------
	
	// Pour des commandes
	public boolean removeEnergie(float amount)
	{
		boolean sufficient = amount <= this.Energie;

		if (sufficient)
		{
			this.Energie -= amount;
			this.sync();
		} else {
			return false;
		}

		return sufficient;
	}

	public void addEnergie(float amount)
	{
		this.Energie += amount;
		this.sync();
	}

	public float getSoif()
	{
		return this.Energie;
	}

	public void setEnergie(float newEnergie)
	{
		this.Energie = newEnergie;
		this.sync();
	}
	
	//-----------------------------------------------------------------------------------------
}
