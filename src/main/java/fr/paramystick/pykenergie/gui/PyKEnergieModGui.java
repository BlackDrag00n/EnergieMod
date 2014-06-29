package fr.paramystick.pykenergie.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.paramystick.pykenergie.extendedplayer.ExtendedPlayerEnergie;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.mco.McoClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class PyKEnergieModGui extends Gui
{	
	public Minecraft mc = FMLClientHandler.instance().getClient();
			
	public PyKEnergieModGui(Minecraft mc)
	{
		this.mc = mc;
	}
	
	public static final String PyKEnergieModguiResource = "textures/gui/status_Gui.png";

	// Résolution écran
	ScaledResolution scaleRes = new ScaledResolution(Minecraft.getMinecraft().gameSettings, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
	public int LargeurEcran = scaleRes.getScaledWidth();
	public int HauteurEcran = scaleRes.getScaledHeight();
	
	// Variable joueur (sera dynamique et va disparaitre d'ici)
	public int barFatigue;
	public float barFatigueText, EnergieFloat, maxEnergieFloat;

	// Taille de la barre (restera fixe pour toutes les barres)
	public static final int barHauteur = 8;
	public static final int barLongueur = 64;
	// Texture DES barres
	public int barBackgroundU = 0;
	public int barBackgroundV = 0;
	public int PointHauteur = 4;
	public int PointLargeur = 4;
	public int CurseurLargeur = 2;
	public int CurseurHauteur = 8;
	public int IconeHauteur = 16;
	public int IconeLargeur = 16;
	
	
	// Contour des barre
	public int barContour = 4; // bling bling = 5
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiRender(RenderGameOverlayEvent event)
	{
		// Variable statique position sur écran ..
		int xPos = 4;
		int yPos = 4;
		
		int Haut_Gauche_X = xPos;
		int Haut_Gauche_Y = yPos;
		int Haut_Droite_X = (LargeurEcran - xPos) - barLongueur;
		int Haut_Droite_Y = yPos;
		int Haut_Milieu_X = (LargeurEcran / 2) - (barLongueur / 2);
		int Haut_Milieu_Y = yPos;

		int Bas_Gauche_X = xPos;
		int Bas_Gauche_Y = (HauteurEcran - yPos);
		int Bas_Droite_X = (LargeurEcran - xPos) - barLongueur;
		int Bas_Droite_Y = (HauteurEcran - yPos);
		int Bas_Milieu_Gauche_X = (LargeurEcran / 2) - (barLongueur / 2) - barLongueur;
		int Bas_Milieu_Gauche_Y = (HauteurEcran - yPos) - 50;
		int Bas_Milieu_Droite_X = (LargeurEcran / 2) - (barLongueur / 2) + barLongueur;
		int Bas_Milieu_Droite_Y = (HauteurEcran - yPos) - 50;
		
		// Evite un bug d'affichage
		if((event.type != ElementType.EXPERIENCE && event.type != ElementType.JUMPBAR) || event.isCancelable())
		{
			return;
		}
		
		EnergieFloat = ExtendedPlayerEnergie.get(mc.thePlayer).Energie;
		maxEnergieFloat = ExtendedPlayerEnergie.get(mc.thePlayer).maxEnergie;
		
		// Initialisation graphique
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);		
		// Initialisation de l'image des barres
		this.mc.renderEngine.bindTexture(new ResourceLocation("pykenergiemod", PyKEnergieModguiResource));
		
		// Affichage de la barre de fatigue (Comme une batterie)
		if (!mc.thePlayer.capabilities.isCreativeMode && false) // Si le joueur n'est pas en mode créative
		{
			/**
			 * BARRE DE FATIGUE
			 */
			barFatigue = MathHelper.ceiling_float_int((EnergieFloat / 100f) * barLongueur); // Calcul en fonction des valeurs joueurs pour la barre de fatigue
			barFatigueText = (float)(Math.round(EnergieFloat * Math.pow(10,2)) / Math.pow(10,2));;
			
			if(barFatigue > barLongueur) // Si les valeurs dépasse la longeur de la barre on bloque la barre a son maximum
			{
				barFatigue = barLongueur;
			} else if(barFatigue < 0) // Si la valeurs passe en négatif bloque la barre a son minimum
			{
				barFatigue = 0;
			}
			
			// Position de la barre de fatigue (réglage WaterPosY et WaterPosX depuis un CFG)
			int FatiguePosY = 10;
			int FatiguePosX = 0;
			int iconFatiguePosX = FatiguePosX + 0;
			int barFatiguePos = iconFatiguePosX + 18;
			int textFatiguePos = barFatiguePos + barLongueur + 2;
			// Texture de la barre de fatigue
			int barFatigueTextureU = 0;
			int barFatigueTextureV = 8;
			int barFatigueCursorU = 35;
			int barFatigueCursorV = 64;
			int barFatiguePointU = 4;
			int barFatiguePointV = 64;
			int barFatigueIconeU = 32;
			int barFatigueIconeV = 80;
			
			// Avec Curseur
			/* Background de la barre */	//this.drawTexturedModalRect(barFatiguePos, FatiguePosY, barBackgroundU, barFatigueTextureV, barLongueur, barHauteur);
			/* Curseur de la barre */		//this.drawTexturedModalRect(barFatiguePos + barFatigue, FatiguePosY, barFatigueCursorU, barFatigueCursorV, CurseurLargeur, CurseurHauteur);

			// Sans Curseur
			/* Background de la barre */	this.drawTexturedModalRect(barFatiguePos, FatiguePosY, barBackgroundU, barBackgroundV, barLongueur, barHauteur); 
			/* Texture de la barre */		this.drawTexturedModalRect(barFatiguePos, FatiguePosY, barFatigueTextureU, barFatigueTextureV, barFatigue, barHauteur);
			
			/* Point de la barre */			this.drawTexturedModalRect(barFatiguePos - 2, FatiguePosY + 2, barFatiguePointU, barFatiguePointV, PointLargeur, PointHauteur);
			/* Contour de la barre */		this.drawTexturedModalRect(barFatiguePos, FatiguePosY, 0, barHauteur * barContour, barLongueur, barHauteur);
			
			/* Icone de l'energie */		this.drawTexturedModalRect(iconFatiguePosX, FatiguePosY - 4, barFatigueIconeU, barFatigueIconeV, IconeLargeur, IconeHauteur);
			
			// Affichage du texte de la barre de fatigue
			Minecraft.getMinecraft().fontRenderer.drawString(barFatigueText + "% (EnergieFloat:"+EnergieFloat+"/"+maxEnergieFloat+")(barFatigue:"+barFatigue+"/"+barLongueur+")", textFatiguePos, FatiguePosY, 16777215);
		}
		

		// Affichage de la barre de fatigue (Comme la barre de faim)
		if (!mc.thePlayer.capabilities.isCreativeMode && true) // Si le joueur n'est pas en mode créative
		{
			/**
			 * BARRE DE FATIGUE
			 */
			barFatigue = MathHelper.ceiling_float_int((EnergieFloat / 100f) * 20); // Calcul en fonction des valeurs joueurs pour la barre de fatigue
			barFatigueText = (float)(Math.round(EnergieFloat * Math.pow(10,2)) / Math.pow(10,2));;
			
			if(barFatigue > 20) // Si les valeurs dépasse la longeur de la barre on bloque la barre a son maximum
			{
				barFatigue = 20;
			} else if(barFatigue < 0) // Si la valeurs passe en négatif bloque la barre a son minimum
			{
				barFatigue = 0;
			}
			
			/**
			 * Barre Energie (comme la faim)
			 */
			int FatiguePosX = Haut_Gauche_X;
			int FatiguePosY = Haut_Gauche_Y;
			int FatigueEspace = 8;
			int FatigueBarFondU = 0;
			int FatigueBarFondV = 100;
			int FatigueBarPleineU = 19;
			int FatigueBarPleineV = 100;
			int FatigueBarDemiU = 28;
			int FatigueBarDemiV = 100;
					
			/* Background de la barre */	this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*0), FatiguePosY, FatigueBarFondU, FatigueBarFondV, 9, 9);
			/* Background de la barre */	this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*1), FatiguePosY, FatigueBarFondU, FatigueBarFondV, 9, 9);
			/* Background de la barre */	this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*2), FatiguePosY, FatigueBarFondU, FatigueBarFondV, 9, 9);
			/* Background de la barre */	this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*3), FatiguePosY, FatigueBarFondU, FatigueBarFondV, 9, 9);
			/* Background de la barre */	this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*4), FatiguePosY, FatigueBarFondU, FatigueBarFondV, 9, 9);
			/* Background de la barre */	this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*5), FatiguePosY, FatigueBarFondU, FatigueBarFondV, 9, 9);
			/* Background de la barre */	this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*6), FatiguePosY, FatigueBarFondU, FatigueBarFondV, 9, 9);
			/* Background de la barre */	this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*7), FatiguePosY, FatigueBarFondU, FatigueBarFondV, 9, 9);
			/* Background de la barre */	this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*8), FatiguePosY, FatigueBarFondU, FatigueBarFondV, 9, 9);
			/* Background de la barre */	this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*9), FatiguePosY, FatigueBarFondU, FatigueBarFondV, 9, 9);
			
			if (barFatigue == 1)
				/* Barre Demi */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*0)+1, FatiguePosY, FatigueBarDemiU, FatigueBarDemiV, 9, 9);
			else if (barFatigue >= 2)
				/* Barre Pleine */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*0)+1, FatiguePosY, FatigueBarPleineU, FatigueBarPleineV, 9, 9);
			if (barFatigue == 3)
				/* Barre Demi */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*1)+1, FatiguePosY, FatigueBarDemiU, FatigueBarDemiV, 9, 9);
			else if (barFatigue >= 4)
				/* Barre Pleine */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*1)+1, FatiguePosY, FatigueBarPleineU, FatigueBarPleineV, 9, 9);
			if (barFatigue == 5)
				/* Barre Demi */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*2)+1, FatiguePosY, FatigueBarDemiU, FatigueBarDemiV, 9, 9);
			else if (barFatigue >= 6)
				/* Barre Pleine */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*2)+1, FatiguePosY, FatigueBarPleineU, FatigueBarPleineV, 9, 9);
			if (barFatigue == 7)
				/* Barre Demi */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*3)+1, FatiguePosY, FatigueBarDemiU, FatigueBarDemiV, 9, 9);
			else if (barFatigue >= 8)
				/* Barre Pleine */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*3)+1, FatiguePosY, FatigueBarPleineU, FatigueBarPleineV, 9, 9);
			if (barFatigue == 9)
				/* Barre Demi */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*4)+1, FatiguePosY, FatigueBarDemiU, FatigueBarDemiV, 9, 9);
			else if (barFatigue >= 10)
				/* Barre Pleine */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*4)+1, FatiguePosY, FatigueBarPleineU, FatigueBarPleineV, 9, 9);
			if (barFatigue == 11)
				/* Barre Demi */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*5)+1, FatiguePosY, FatigueBarDemiU, FatigueBarDemiV, 9, 9);
			else if (barFatigue >= 12)
				/* Barre Pleine */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*5)+1, FatiguePosY, FatigueBarPleineU, FatigueBarPleineV, 9, 9);
			if (barFatigue == 13)
				/* Barre Demi */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*6)+1, FatiguePosY, FatigueBarDemiU, FatigueBarDemiV, 9, 9);
			else if (barFatigue >= 14)
				/* Barre Pleine */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*6)+1, FatiguePosY, FatigueBarPleineU, FatigueBarPleineV, 9, 9);
			if (barFatigue == 15)
				/* Barre Demi */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*7)+1, FatiguePosY, FatigueBarDemiU, FatigueBarDemiV, 9, 9);
			else if (barFatigue >= 16)
				/* Barre Pleine */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*7)+1, FatiguePosY, FatigueBarPleineU, FatigueBarPleineV, 9, 9);
			if (barFatigue == 17)
				/* Barre Demi */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*8)+1, FatiguePosY, FatigueBarDemiU, FatigueBarDemiV, 9, 9);
			else if (barFatigue >= 18)
				/* Barre Pleine */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*8)+1, FatiguePosY, FatigueBarPleineU, FatigueBarPleineV, 9, 9);
			if (barFatigue == 19)
				/* Barre Demi */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*9)+1, FatiguePosY, FatigueBarDemiU, FatigueBarDemiV, 9, 9);
			else if (barFatigue >= 20)
				/* Barre Pleine */		this.drawTexturedModalRect(FatiguePosX+(FatigueEspace*9)+1, FatiguePosY, FatigueBarPleineU, FatigueBarPleineV, 9, 9);
			
			Minecraft.getMinecraft().fontRenderer.drawString(barFatigueText + "% (EnergieFloat:"+EnergieFloat+"/"+maxEnergieFloat+")(barFatigue:"+barFatigue+"/20)", FatiguePosX+(FatigueEspace*9)+FatigueEspace+yPos, FatiguePosY, 16777215);
		}
	}
}