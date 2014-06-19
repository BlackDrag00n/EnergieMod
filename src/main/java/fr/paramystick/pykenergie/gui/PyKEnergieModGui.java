package fr.paramystick.pykenergie.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class PyKEnergieModGui extends Gui
{	
	public Minecraft mc;

	public static final String PyKEnergieModguiResource = "textures/gui/status_Gui.png";

	// Variable joueur (sera dynamique et va disparaitre d'ici)
	public static final float fatigue = 65.25f;

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
	
	public PyKEnergieModGui(Minecraft mc)
	{
		this.mc = mc;
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiRender(RenderGameOverlayEvent event)
	{
		// Evite un bug d'affichage
		if((event.type != ElementType.EXPERIENCE && event.type != ElementType.JUMPBAR) || event.isCancelable())
		{
			return;
		}
		
		// Initialisation graphique
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);		
		// Initialisation de l'image des barres
		this.mc.renderEngine.bindTexture(new ResourceLocation("pykenergiemod", PyKEnergieModguiResource));

		/**
		 * BARRE DE FATIGUE
		 */
		int barFatigue = MathHelper.ceiling_float_int((fatigue / 100f) * barLongueur); // Calcul en fonction des valeurs joueurs pour la barre de fatigue
		
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
		// Affichage de la barre de fatigue
		//this.drawTexturedModalRect(barFatiguePos, FatiguePosY, barBackgroundU, barFatigueTextureV, barLongueur, barHauteur); // Background de la barre
		//this.drawTexturedModalRect(barFatiguePos + barFatigue, FatiguePosY, barFatigueCursorU, barFatigueCursorV, CurseurLargeur, CurseurHauteur); // Curseur de la barre
		this.drawTexturedModalRect(barFatiguePos, FatiguePosY, barBackgroundU, barBackgroundV, barLongueur, barHauteur); // Background de la barre
		this.drawTexturedModalRect(barFatiguePos, FatiguePosY, barFatigueTextureU, barFatigueTextureV, barFatigue, barHauteur); // Texture de la barre

		this.drawTexturedModalRect(barFatiguePos - 2, FatiguePosY + 2, barFatiguePointU, barFatiguePointV, PointLargeur, PointHauteur); // Point de la barre
		this.drawTexturedModalRect(barFatiguePos, FatiguePosY, 0, barHauteur * barContour, barLongueur, barHauteur); // Contour de la barre
		this.drawTexturedModalRect(iconFatiguePosX, FatiguePosY - 4, barFatigueIconeU, barFatigueIconeV, IconeLargeur, IconeHauteur); // Icone de l'energie
		// Affichage du texte de la barre de fatigue
		Minecraft.getMinecraft().fontRenderer.drawString(fatigue + "% (barFatigue:"+barFatigue+"/"+barLongueur+")", textFatiguePos, FatiguePosY, 16777215);
		
	}
}