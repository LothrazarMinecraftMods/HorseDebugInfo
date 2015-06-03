package com.lothrazar.samsf3;

import java.util.ArrayList; 

import org.apache.logging.log4j.Logger;   

import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
  
@Mod(modid = ModMain.MODID, version = ModMain.VERSION,	name = ModMain.NAME, useMetadata = true )  
public class ModMain
{
	public static final String MODID = "samscontent";
	public static final String TEXTURE_LOCATION = MODID + ":";
	public static final String VERSION = "1.8-1.4.0";
	public static final String NAME = "Builder's Powerups";

	@Instance(value = MODID)
	public static ModMain instance;
	@SidedProxy(clientSide="com.lothrazar.samscontent.proxy.ClientProxy", serverSide="com.lothrazar.samscontent.proxy.CommonProxy")
  
	public static Logger logger; 
	public static ConfigRegistry cfg;
   

	public static String lang(String name)
	{
		return StatCollector.translateToLocal(name);
	}
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{ 
		logger = event.getModLog();  
		
		cfg = new ConfigRegistry(new Configuration(event.getSuggestedConfigurationFile()));
	  
     
		 
		this.registerEventHandlers(); 


		 
	}
        
	@EventHandler
	public void onInit(FMLInitializationEvent event)
	{       


	}
	 
	private void registerEventHandlers() 
	{ 
    	ArrayList<Object> handlers = new ArrayList<Object>();
  

    	handlers.add(new DebugScreenText()          );  //This one can stay  
     	handlers.add(instance                         ); 



     	for(Object h : handlers)
     		if(h != null)
	     	{ 
	    		FMLCommonHandler.instance().bus().register(h); 
	    		MinecraftForge.EVENT_BUS.register(h); 
	    		MinecraftForge.TERRAIN_GEN_BUS.register(h);
	    		MinecraftForge.ORE_GEN_BUS.register(h); 
	     	} 
	}
	
}
