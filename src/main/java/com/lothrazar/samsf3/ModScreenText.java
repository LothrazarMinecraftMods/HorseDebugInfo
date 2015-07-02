package com.lothrazar.samsf3;

import java.text.DecimalFormat;

import org.apache.logging.log4j.Logger;   

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
  
@Mod(modid = ModScreenText.MODID, useMetadata = true )  
public class ModScreenText
{
	public static final String MODID = "samsf3";
	@Instance(value = MODID)
	public static ModScreenText instance;
	//public static Logger logger; 
   
	public static String lang(String name)
	{
		return StatCollector.translateToLocal(name);
	}
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{ 
		//logger = event.getModLog();  
	
    	FMLCommonHandler.instance().bus().register(instance); 
    	MinecraftForge.EVENT_BUS.register(instance); 
	}
        
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderTextOverlay(RenderGameOverlayEvent.Text event)
	{  
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer; 
	
		if(Minecraft.getMinecraft().gameSettings.showDebugInfo)
		{
		 	if(player.ridingEntity != null && player.ridingEntity instanceof EntityHorse)
		 	{ 
		 		addHorseInfo(event, player, (EntityHorse)player.ridingEntity);   
		 	} 
		}  
	}

	@SideOnly(Side.CLIENT)
	private void addHorseInfo(RenderGameOverlayEvent.Text event,	EntityPlayerSP player,EntityHorse horse) 
	{ 
		double speed =  horse.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue() ;

		double jump = horse.getHorseJumpStrength() ;
		//convert from scale factor to blocks
		double jumpHeight = 0;
		double gravity = 0.98;
		while (jump > 0)
		{
			jumpHeight += jump;
			jump -= 0.08;
			jump *= gravity;
		}
		
		DecimalFormat df = new DecimalFormat("0.0000");
		
		event.left.add(ModScreenText.lang("debug.horsespeed")+"  "+ df.format(speed)   ); 
		
		df = new DecimalFormat("0.0");
		
		event.left.add(ModScreenText.lang("debug.horsejump") +"  "+ df.format(jumpHeight) );
	}
}
