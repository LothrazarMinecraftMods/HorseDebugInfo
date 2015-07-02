package com.lothrazar.samsf3;

import java.util.ArrayList; 

import org.apache.logging.log4j.Logger;   

import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
  
@Mod(modid = ModScreenText.MODID, useMetadata = true )  
public class ModScreenText
{
	public static final String MODID = "samsf3";
	@Instance(value = MODID)
	public static ModScreenText instance;
	public static Logger logger; 
	public static ConfigText cfg;
   
	public static String lang(String name)
	{
		return StatCollector.translateToLocal(name);
	}
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{ 
		logger = event.getModLog();  
		
		cfg = new ConfigText(new Configuration(event.getSuggestedConfigurationFile()));
	   
	 	DebugScreenText h = new DebugScreenText();

    	FMLCommonHandler.instance().bus().register(h); 
    	MinecraftForge.EVENT_BUS.register(h); 
	}
        
	@EventHandler
	public void onInit(FMLInitializationEvent event)
	{    
	}
}
