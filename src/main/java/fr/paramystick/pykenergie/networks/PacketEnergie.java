package fr.paramystick.pykenergie.networks;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import fr.minecraftforgefrance.ffmtlibs.network.FFMTPacket;
import fr.paramystick.pykenergie.extendedplayer.ExtendedPlayerEnergie;
import io.netty.buffer.ByteBuf;

public class PacketEnergie extends FFMTPacket
{
	private long maxEnergie, Energie;
	
	public PacketEnergie()
	{
	
	}
	
	public PacketEnergie(long maxEnergie, long Energie)
	{
		this.maxEnergie = maxEnergie;
		this.Energie = Energie;
	}

	@Override
	public void writeData(ByteBuf buffer) throws IOException
	{
		buffer.writeLong(maxEnergie);
		buffer.writeLong(Energie);
	}

	@Override
	public void readData(ByteBuf buffer)
	{
		this.maxEnergie = buffer.readLong();
		this.Energie = buffer.readLong();
	}
	
	@Override
	public void handleClientSide(EntityPlayer player)
	{
		ExtendedPlayerEnergie props = ExtendedPlayerEnergie.get(player);
		props.maxEnergie = this.maxEnergie;
		props.Energie = this.Energie;
	}
	
	@Override
	public void handleServerSide(EntityPlayer player)
	{
		ExtendedPlayerEnergie props = ExtendedPlayerEnergie.get(player);
		props.maxEnergie = this.maxEnergie;
		props.Energie = this.Energie;
	}
}
