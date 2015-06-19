package com.lothrazar.samsf3;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;  
import java.util.Calendar;
import java.util.Date;
import java.util.Random;   

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft; 
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;
 
public class DebugScreenText
{   
	 public Date addDays(Date baseDate, int daysToAdd) 
	 {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(baseDate);
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
        return calendar.getTime();
	}
	 
	 public static String posToString(BlockPos position) 
		{ 
			return "["+ position.getX() + ", "+position.getY()+", "+position.getZ()+"]";
		} 
	@SubscribeEvent
	public void onRenderTextOverlay(RenderGameOverlayEvent.Text event)
	{  
		World world = Minecraft.getMinecraft().getIntegratedServer().getEntityWorld();
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer; 
 
		if(Minecraft.getMinecraft().gameSettings.showDebugInfo)
		{
			 
			if(world.getGameRules().getGameRuleBooleanValue("reducedDebugInfo") )
			{ 
				event.right.clear();
				event.left.clear();
				BlockPos pos = player.getPosition();
				int blockLight = world.getLightFromNeighbors(pos) + 1;
				
				String biome = ModScreenText.lang("debug.biome")+world.getBiomeGenForCoords(player.getPosition()).biomeName;

				event.left.add(biome);
				 
				event.left.add(posToString(pos));
				
				if(player.isSneaking()) 
				{ 
					event.left.add(ModScreenText.lang("debug.light")+blockLight);
					
					event.left.add("FPS: "+Minecraft.getDebugFPS());
				} 
			}
			
			addDateTimeInfo(event, world);
			
		 	if(ModScreenText.cfg.debugSlime && player.dimension == 0)
		 	{ 
		 		addSlimeChunkInfo(event, player, world); 
		 	}
		 	
		 	if(ModScreenText.cfg.debugVillageInfo && world.villageCollectionObj != null)
		 	{   
		 		addVillageInfo(event, player, world);	 
			}
		 	
		 	if(ModScreenText.cfg.debugHorseInfo && player.ridingEntity != null && player.ridingEntity instanceof EntityHorse)
		 	{ 
		 		addHorseInfo(event, player);   
		 	} 
	 
		 	if( player.isSneaking()//(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		 			&& ModScreenText.cfg.debugGameruleInfo)  
		 	{ 
				addGameruleInfo(event, world); 
			}
		}  
	}
 
	private void addGameruleInfo(RenderGameOverlayEvent.Text event, World world) 
	{ 
		GameRules rules = world.getWorldInfo().getGameRulesInstance();
		
		ArrayList<String> ruleNames = new ArrayList<String>();
		ruleNames.add("commandBlockOutput");
		ruleNames.add("doDaylightCycle");
		ruleNames.add("doEntityDrops");
		ruleNames.add("doFireTick");
		ruleNames.add("doMobLoot");
		ruleNames.add("doMobSpawning");
		ruleNames.add("doTileDrops");
		ruleNames.add("keepInventory");
		ruleNames.add("mobGriefing");
		ruleNames.add("naturalRegeneration");
		ruleNames.add("reducedDebugInfo");
		ruleNames.add("sendCommandFeedback");
		ruleNames.add("showDeathMessages"); 
 
		String name;
		for(int i = 0; i < ruleNames.size(); i++)
		{
			name = ruleNames.get(i);
			if(rules.getGameRuleBooleanValue(name))
			{ 
				event.right.add(EnumChatFormatting.GREEN + name); 
			}
			else
			{ 
				event.right.add(EnumChatFormatting.RED + name); 
			}
		}
	}
	public static class Horse
	{
		public static final int variant_white = 0;
		public static final int variant_creamy = 1;
		public static final int variant_chestnut = 2;
		public static final int variant_brown = 3;
		public static final int variant_black = 4;
		public static final int variant_gray = 5;
		public static final int variant_brown_dark = 6; 
		
		public static final int type_standard = 0;
		public static final int type_donkey = 1;
		public static final int type_mule = 2;
		public static final int type_zombie = 3;
		public static final int type_skeleton = 4;
	}
 
	private void addHorseInfo(RenderGameOverlayEvent.Text event,	EntityPlayerSP player) 
	{
		EntityHorse horse = (EntityHorse)player.ridingEntity;
		   
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
	
		//http://minecraft.gamepedia.com/Item_id#Horse_Variants
		String variant = "";
		 
		int var = horse.getHorseVariant();

		String spots = null;
		
		if(var >= 1024) spots = "Black Dots ";
		else if(var >= 768) spots = "White Dots";
		else if(var >= 512) spots = "White Field";
		else if(var >= 256) spots = "White Patches";
		
		while(var - 256 > 0)
		{
			var -= 256;
		}
		
		switch( var )
		{
			
			case Horse.variant_white: variant = "White";break; 
			case Horse.variant_creamy: variant = "Creamy";break;
			case Horse.variant_chestnut: variant = "Chestnut";break;
			case Horse.variant_brown: variant = "Brown";break;
			case Horse.variant_black: variant = "Black";break;
			case Horse.variant_gray: variant = "Gray";break;
			case Horse.variant_brown_dark: variant = "Dark Brown";break; 
		}
		
		//if its not a horse, variant wont matter
		String type = "";
		switch( horse.getHorseType())
		{
			case Horse.type_standard: type = variant + " Horse";break;
			case Horse.type_donkey: type = "Donkey";break;
			case Horse.type_mule: type = "Mule";break;
			case Horse.type_zombie: type = "Zombie Horse";break;
			case Horse.type_skeleton: type = "Skeleton Horse";break;
		}

		if(spots != null) type += " ("+spots+")";

		//event.left.add("");
		event.left.add(ModScreenText.lang("debug.horsetype")+"  "+type); 

		DecimalFormat df = new DecimalFormat("0.0000");
		
		event.left.add(ModScreenText.lang("debug.horsespeed")+"  "+ df.format(speed)   ); 
		
		df = new DecimalFormat("0.0");
		
		event.left.add(ModScreenText.lang("debug.horsejump") +"  "+ df.format(jumpHeight) );
	}

	private void addVillageInfo(RenderGameOverlayEvent.Text event,	EntityPlayerSP player, World world) 
	{
		 int playerX = MathHelper.floor_double(player.posX);
		// int playerY = MathHelper.floor_double(player.posY);
		 int playerZ = MathHelper.floor_double(player.posZ);
		 
		 int dX,dZ;
		 
		 int range = 10;

		 Village closest = world.villageCollectionObj.getNearestVillage(player.getPosition(), range);
		 
		 if(closest != null)
		 { 
		    int doors = closest.getNumVillageDoors();
		    int villagers = closest.getNumVillagers();
		     
		    int rep = closest.getReputationForPlayer(player.getName());
  
		    event.left.add(ModScreenText.lang("debug.villagepop")+"  "+String.format("%d",villagers));
		    event.left.add(ModScreenText.lang("debug.villagerep")+"  "+String.format("%d",rep));
		    event.left.add(ModScreenText.lang("debug.villagedoors")+"  "+String.format("%d",doors));
 
		    dX = playerX - closest.getCenter().getX();
		    dZ = playerZ - closest.getCenter().getZ();
		    
		    int dist = MathHelper.floor_double(Math.sqrt( dX*dX + dZ*dZ));

		    event.left.add(ModScreenText.lang("debug.villagedistcenter")+"  "+String.format("%d", dist)); 
		 }
	}

	public static final long ticksPerDay = 24000 ;
	private void addSlimeChunkInfo(RenderGameOverlayEvent.Text event,	EntityPlayerSP player, World world)
	{
		long seed =  world.getSeed();
    
		
		
		Chunk in = world.getChunkFromBlockCoords(player.getPosition());

		//formula source : http://minecraft.gamepedia.com/Slime
		Random rnd = new Random(seed +
		        (long) (in.xPosition * in.xPosition * 0x4c1906) +
		        (long) (in.xPosition * 0x5ac0db) + 
		        (long) (in.zPosition * in.zPosition) * 0x4307a7L +
		        (long) (in.zPosition * 0x5f24f) ^ 0x3ad8025f);
		
		boolean isSlimeChunk = (rnd.nextInt(10) == 0);
    
		if(isSlimeChunk)
		{
			event.left.add(ModScreenText.lang("debug.slimechunk")); 
		}
	}
 
	private void addDateTimeInfo(RenderGameOverlayEvent.Text event, World world) 
	{
		long time = world.getWorldTime(); 
	 
		int days = MathHelper.floor_double( time / ticksPerDay);
		 
		long remainder = time % ticksPerDay;
		
		String detail = "";
	
		if(remainder < 5000) detail = ModScreenText.lang("debug.morning");
		else if(remainder < 7000) detail = ModScreenText.lang("debug.midday");//midd ay is exactly 6k, so go one on each side
		else if(remainder < 12000) detail = ModScreenText.lang("debug.afternoon");
		else detail = ModScreenText.lang("debug.moonphase") + " " + world.getMoonPhase();

		// a 365 day calendar. Day Zero is January 1st of year zero?``
		 //http://stackoverflow.com/questions/8263220/calendar-set-year-issue
		//event.left.add("Day "+days +" ("+detail+")");  
		 
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy");
		
		Calendar c1 = Calendar.getInstance(); // TODAY AD
		c1.set(Calendar.YEAR, 0);             // August  16th,    0 AD
		c1.set(Calendar.DAY_OF_YEAR, 1);      // January  1st,    0 AD
		c1.set(Calendar.YEAR, 1000);          // January  1st, 2001 AD
		Date start = c1.getTime();     // prints the expected date
		 
		Date curr = addDays(start,days);
 
		event.left.add(ModScreenText.lang("debug.days") + days +", "+detail+", "+sdf.format(curr));
	}
}
