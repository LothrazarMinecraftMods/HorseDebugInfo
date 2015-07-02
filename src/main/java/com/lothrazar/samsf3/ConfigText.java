package com.lothrazar.samsf3;

import net.minecraftforge.common.config.Configuration;

public class ConfigText
{ 
	private Configuration instance;
	private String category = "";
	public boolean debugGameruleInfo;
	//public boolean debugSlime;
	public boolean debugHorseInfo;
	public boolean debugVillageInfo;
	
	public Configuration instance()
	{
		return instance;
	}
	
	public ConfigText(Configuration c)
	{
		instance = c; 
		instance.load();
		
		category = ModScreenText.MODID;
		
		debugGameruleInfo = instance.getBoolean("gamerule_info_sneaking",category, true,
    			"If you are sneaking, the right side shows all the game rules, on or off. " );
		 
		//reducedDebugImproved = instance.getBoolean("reducedDebugInfo_improved",category, true,
    	//		"If this gamerule is turned on, then much more useless information is cleared away (with coordinates still hidden), but some is added back in such as the biome name. " );
		
		//debugSlime = instance.getBoolean("slime",category, true,
    	//		"Shows if you are standing in a slime chunk." );
		
		debugHorseInfo = instance.getBoolean("horse",category, true,
    			"Shows info on any horse ridden including speed, jump height, species.");
		
		debugVillageInfo = instance.getBoolean("village",category, true,
    			"Shows info on any village you are standing in.");
		
		if(instance.hasChanged()){ instance.save(); }
	} 
}
