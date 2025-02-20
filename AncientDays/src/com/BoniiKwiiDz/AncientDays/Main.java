package com.BoniiKwiiDz.AncientDays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.BoniiKwiiDz.AncientDays.Warden;

import io.netty.handler.codec.redis.IntegerRedisMessage;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main extends JavaPlugin implements Listener{

Boolean hasCreated = false;
  public HashMap<Integer, Player>  Online_u = new HashMap<>(); int player_count = 0;
  HashMap<String, Integer> danger_x = new HashMap<>(); int danger_x_pos = 0;
  HashMap<String, Integer> danger_y = new HashMap<>(); int danger_y_pos = 0;
  HashMap<String, Integer> danger_z = new HashMap<>(); int danger_z_pos = 0;
  HashMap<Player,  Boolean>  Noisy_block = new HashMap<>();
  HashMap<Player,  Boolean>  Noisy_us = new HashMap<>();
  HashMap<Player,  Integer>  Must_die = new HashMap<>();
  HashMap<String, Warden> wardens = new HashMap<>();
  int warden_cnt;
  
  public void make_danger_point(int dx,int dy, int dz) {
	  danger_x.put(danger_x_pos+"pos", dx); danger_x_pos++;
	  danger_y.put(danger_y_pos+"pos", dy); danger_y_pos++;
	  danger_z.put(danger_z_pos+"pos", dz); danger_z_pos++;
  }
  
  public void place_segment(int seg_x, int seg_y, int seg_z, String file) {
	  File directory = new File("save_file");
		File file2read = new File(directory,file);
		if(!file2read.exists()) {
			System.out.println("Could not find file");
		}else {
		  try {
			Scanner scanf = new Scanner(file2read);
			while (scanf.hasNextLine()) {
				String blockr = scanf.nextLine();
				//sender.sendMessage(blockr);
				String[] cmdr = blockr.split(" ");
				int _sx = seg_x+Integer.parseInt(cmdr[0]);
				int _sy = seg_y+Integer.parseInt(cmdr[1]);
				int _sz = seg_z+Integer.parseInt(cmdr[2]);
				int _sr = Integer.parseInt(cmdr[3]);
				Bukkit.getServer().getWorlds().get(0).getBlockAt(_sx,_sy,_sz).setType(Material.getMaterial(cmdr[4]));
				Bukkit.getServer().getWorlds().get(0).getBlockAt(_sx,_sy,_sz).setData((byte)_sr);
			}
			scanf.close();
		  } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		 	e.printStackTrace();
		  }
		}
  }
  
  public int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
  }
  
  public void generate_maze(int _x, int _y, int _z) {
	  // chunks must get loaded first
	  for(int locx = 0; locx < 12; locx++) {
	   for(int locz = 0; locz < 12; locz++) {
	     if(!Bukkit.getServer().getWorlds().get(0).loadChunk((int)((_x/16)+locx),(int)((_z/16)+locz), true)) {
		   Bukkit.getServer().getWorlds().get(0).regenerateChunk((int)((_x/16)+locx),(int)((_z/16)+locz));
	     }
	   }
	  }
	  //-----------------------------	  
	  int[][] maze = new int[16][16];
	  int[] path_p = new int[8];
	  /*
	   * TILE1 TILE2 TILE3 + - + 
	   * TILE4 TILE5 TILE6 | O | · PIECES OF PATH
	   * TILE7 TILE8 TILE9 + - +
	   * */
	  for(int i = 0; i < 12; i++) {
		  int point_x = getRandomNumber(1,15); // x position
		  int point_y = getRandomNumber(1,15); // y position
		  if(maze[point_x+1][point_y]!=0 && maze[point_x-1][point_y]!=0 
		  && maze[point_x][point_y-1]!=0 && maze[point_x][point_y+1]!=0) {
			  point_x = getRandomNumber(1,15); // x position
			  point_y = getRandomNumber(1,15); // y position
		  }
		  int point_v1 = getRandomNumber(1,2); // vertical or hor
		  int point_d1 = getRandomNumber(3,5); // distance
		  int get_corruption = getRandomNumber(1,10);
		  int dist_ext = 0;
		  if(point_x >= 8 && point_v1 == 1) {
			  for(int j = 0; j < point_d1; j++) {
				  get_corruption = getRandomNumber(1,10);
				  if(get_corruption == 1 || get_corruption == 5) {
				    maze[point_x-dist_ext][point_y] = 8;
				  }else {
					maze[point_x-dist_ext][point_y] = 2;  
				  }
				  dist_ext++;
			  }
			  dist_ext = 0;
		  }else if(point_v1 == 1){
			  for(int j = 0; j < point_d1; j++) {
				  get_corruption = getRandomNumber(1,10);
				  if(get_corruption == 1 || get_corruption == 5) {
				    maze[point_x+dist_ext][point_y] = 8;
				  }else {
					maze[point_x+dist_ext][point_y] = 2;  
				  }
				  dist_ext++;
			  }
			  dist_ext = 0;
		  }
		  if(point_y >= 8 && point_v1 == 2) {
			  for(int j = 0; j < point_d1; j++) {
				  get_corruption = getRandomNumber(1,10);
				  if(get_corruption == 1 || get_corruption == 5) {
				    maze[point_x][point_y-dist_ext] = 4;
				  }else {
					maze[point_x][point_y-dist_ext] = 6;  
				  }
				  dist_ext++;
			  }
			  dist_ext = 0;
		  }else if (point_v1 == 2) {
			  for(int j = 0; j < point_d1; j++) {
				  get_corruption = getRandomNumber(1,10);
				  if(get_corruption == 1 || get_corruption == 5) {
				    maze[point_x][point_y+dist_ext] = 4;
				  }else {
					maze[point_x][point_y+dist_ext] = 6;  
				  }
				  dist_ext++;
			  }
			  dist_ext = 0; 
		  }
	  }
	  //System.out.println("GENERATED STRUCTURE");
	  for(int search_y = 1; search_y < 15; search_y++) {
		  for(int search_x = 1; search_x < 15; search_x++) {
			  if((search_x == 2 || search_x == 14)&&(search_y!=2||search_y!=14) ) {maze[search_x][search_y] = 4;}
			  if((search_y == 2 || search_y == 14)&&(search_x!=2||search_x!=14) ) {maze[search_x][search_y] = 8;}
			  maze[2][2] = 1;  maze[14][2] = 3;
			  maze[2][14] = 7; maze[14][14] = 9;
			  
			  if(maze[search_x+1][search_y]!=0 && maze[search_x-1][search_y]!=0 
			  && maze[search_x][search_y-1]!=0 && maze[search_x][search_y+1]!=0) {
				  maze[search_x][search_y] = 5;
			  }
			  
			  if(maze[search_x+1][search_y]!=0 && maze[search_x-1][search_y]==0 
			  && maze[search_x][search_y-1]!=0 && maze[search_x][search_y+1]==0) {
				  maze[search_x][search_y] = 3;
			  }
			  
			  if(maze[search_x+1][search_y]==0 && maze[search_x-1][search_y]!=0 
			  && maze[search_x][search_y-1]!=0 && maze[search_x][search_y+1]==0) {
				  maze[search_x][search_y] = 1;
			  }
			  
			  if(maze[search_x+1][search_y]!=0 && maze[search_x-1][search_y]==0 
			  && maze[search_x][search_y-1]==0 && maze[search_x][search_y+1]!=0) {
				  maze[search_x][search_y] = 9;
			  }
					  
			  if(maze[search_x+1][search_y]==0 && maze[search_x-1][search_y]!=0 
			  && maze[search_x][search_y-1]==0 && maze[search_x][search_y+1]!=0) {
				  maze[search_x][search_y] = 7;
			  }
		  }
	  }
	  for(int plx=1;plx<15;plx++) {
		  for(int ply=1;ply<15;ply++) {
			  if(maze[plx+1][ply]!=0 && maze[plx-1][ply]!=0 
			  && maze[plx][ply-1]!=0 && maze[plx][ply+1]!=0) {
				  int corrupt = getRandomNumber(1,10);
				  if(corrupt>3) {
				   maze[plx][ply] = 5;
				  }else {
				   maze[plx][ply] = getRandomNumber(12,16);
				  }
			  }
			  int towerG = getRandomNumber(1,20);
			  if(towerG==1||towerG==10) {
				  maze[plx][ply] = 10;
			  }
			  if(towerG==15||towerG==20) {
				  maze[plx][ply] = 15;
			  }
		  }
	  }
	  maze[0][0] = 11;
	  maze[0][15] = 11;
	  maze[15][15] = 11;
	  maze[15][0] = 11;
	  //System.out.println("MAKING INTERSECTIONS");
	  String dbg_str = "";
	  for(int plx=0; plx < 16; plx++) {
		for(int ply=0; ply < 16; ply++) {
			  
			   //dbg_str = dbg_str + maze[plx][ply] + ",";
			   int gen_x = (_x+(plx*6));
			   int gen_y = (_y);
			   int gen_z = (_z+(ply*6));
			   switch(maze[plx][ply]) {
			      case 1:
			    	place_segment(gen_x,gen_y,gen_z,"city2_rails0");  
			      break;
			      
			      case 2:
				    place_segment(gen_x,gen_y,gen_z,"city2_rails1");  
				  break;
				      
			      case 3:
				    place_segment(gen_x,gen_y,gen_z,"city2_rails2");  
				  break;
				  
			      case 4:
				   	place_segment(gen_x,gen_y,gen_z,"city2_rails3");  
				  break;
				      
				  case 5:
				    place_segment(gen_x,gen_y,gen_z,"city2_rails4");  
				  break;
					      
				  case 6:
				    place_segment(gen_x,gen_y,gen_z,"city2_rails5");  
				  break;
				  
				  case 7:
				   	place_segment(gen_x,gen_y,gen_z,"city2_rails6");  
				  break;
				      
				  case 8:
					place_segment(gen_x,gen_y,gen_z,"city2_rails7");  
				  break;
					      
				  case 9:
				    place_segment(gen_x,gen_y,gen_z,"city2_rails8");  
				  break;
				  
				  case 10:
					  place_segment(gen_x,gen_y,gen_z,"city1_tower1");
				  break;
				  
				  case 11:
					  place_segment(gen_x,gen_y,gen_z,"city1_fort1");
				  break;
				  
				  case 12:
					  place_segment(gen_x,gen_y,gen_z,"city1_ritual_warden1");
					  make_danger_point(gen_x,gen_y+1,gen_z);
				  break;
				  
				  case 13:
					  place_segment(gen_x,gen_y,gen_z,"city1_corruption2");
					  make_danger_point(gen_x,gen_y+1,gen_z);
				  break;
				  
				  case 14:
					  place_segment(gen_x,gen_y,gen_z,"city_simple_corruption1");
					  make_danger_point(gen_x,gen_y+1,gen_z);
				  break;
				  
				  case 15:
					  place_segment(gen_x,gen_y,gen_z,"city1_house1");
					  make_danger_point(gen_x,gen_y+1,gen_z);
				  break;
			   }
		  }
		  //System.out.println(dbg_str);
		  dbg_str = "";
	  }
	  for(int zz = 0; zz < 10; zz++) {
		  place_segment((_x+(8*6))+5,_y,(_z+(8*6))-10-(zz*6),"city1_bridge1");
	  }
	  
	  for(int x = _x; x < (_x+(16*6)); x++) {
		  for(int y = _y; y < (_y+(16)); y++) {
		   for(int z = _z; z < (_z+(16*6)); z++) {
			  if(y==_y) {
			   int floorwf = getRandomNumber(1,10);
			   if(floorwf > 5) {
				  Bukkit.getServer().getWorlds().get(0).getBlockAt(x,_y,z).setType(Material.OBSIDIAN);
			   }else if(floorwf==5){
				  Bukkit.getServer().getWorlds().get(0).getBlockAt(x,_y,z).setType(Material.DIRT);
			   }else {
				   Bukkit.getServer().getWorlds().get(0).getBlockAt(x,_y,z).setType(Material.STONE);
			   }
			  }else if(y > 10||y==_y) {
				  int ariwf = getRandomNumber(1,50);
				  if(ariwf == 1||ariwf == 25||ariwf == 50) {
					  Bukkit.getServer().getWorlds().get(0).getBlockAt(x,y,z).setType(Material.AIR);
				  }
			  }
		   }
	     }
	  }
	  place_segment((_x+(8*6))-10,_y,(_z+(8*6))-10,"city1_mainTower");
	  make_danger_point((_x+(8*6))-10,_y,(_z+(8*6))-10);
	  //System.out.print("maze generated");
	  // unload chunks
	  for(int locx = 0; locx < 12; locx++) {
		  for(int locz = 0; locz < 12; locz++) {
		     Bukkit.getServer().getWorlds().get(0).unloadChunk((int)((_x/16)+locx),(int)((_z/16)+locz));		     
	      }
	  }
	  System.gc();
  }
  @Override
  public void onEnable() {
	  getServer().getPluginManager().registerEvents(this, this);
	  File directory = new File("city_configs");
	  if(!directory.exists()) {
		  directory.mkdir();
	  }
	  File generatedInfo = new File(directory, "cfg.bon");
	  if(!generatedInfo.exists()) {
		FileWriter create;
		try {
			create = new FileWriter(generatedInfo);
			create.write("created:1");
			create.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	  }else {
		  try {
			Scanner scanf = new Scanner(generatedInfo);
			while(scanf.hasNextLine()) {
				if(scanf.nextLine().equalsIgnoreCase("created:1")) {
					hasCreated = true;
					System.out.println("MANY ANCIENT CITIES GENERATED WHERE DETECTED!!!!!!");
				}
			}
			scanf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  if(hasCreated==false) {
		  System.out.println("NO ANCIENT CITIES GENERATED WHERE DETECTED!!!!!!");
		  System.out.println("creating structures......");
		  System.out.println("*****************************************************");
		  System.out.println("*THIS MIGHT TAKE LONGER THE FIRST TIME GENERATING...*");
		  System.out.println("*****************************************************");
		  System.out.println("please be patient...");
		  for(int i = 0; i < 100; i ++) {
			  int sgx = getRandomNumber(-16000,16000);
			  int sgy = getRandomNumber(12,19);
			  int sgz = getRandomNumber(-16000,16000);
			  
			  generate_maze(sgx,sgy,sgz);
			  System.out.println("Generating cities "+i+"/100 [==============] "+(i)+"% completed...");
			  //System.out.println("Placed on:" + sgx+", "+sgy+", "+sgz);
		  }
		  System.out.println("structures were created....");
	  }
	  File directory_zone = new File("danger_zones");
	  if(!directory_zone.exists()) {
		  directory_zone.mkdir();
	  }
	  File danger_zone_file = new File(directory_zone,"regions");
	  if(hasCreated==false && !danger_zone_file.exists()) {
		try {
			FileWriter wrt = new FileWriter(danger_zone_file);
			System.out.println("FOUND: " + danger_x_pos + " DANGER ZONES");
			for(int j = 0; j < danger_x_pos; j++) {
				//System.out.println("WROTE: " + danger_x.get(j+"pos")+" "+danger_y.get(j+"pos")+" "+danger_z.get(j+"pos")+"\n");
				wrt.write(danger_x.get(j+"pos")+" "+danger_y.get(j+"pos")+" "+danger_z.get(j+"pos")+"\n");
			}
			wrt.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }else {
		try {
			Scanner scanf2 = new Scanner(danger_zone_file);
			while(scanf2.hasNextLine()) {
				String zone_fetch = scanf2.nextLine();
				String[] found_zones = zone_fetch.split(" ");
				make_danger_point(
						          Integer.parseInt(found_zones[0]),
						          Integer.parseInt(found_zones[1]),
						          Integer.parseInt(found_zones[2])
						         );
				//System.out.println("Fetched from file: ("+found_zones[0]+", "+found_zones[1]+", "+found_zones[2]+")");
			}
			scanf2.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
   System.out.println("Starting listening system...");
   new BukkitRunnable() {
	    public void run() {
	    	//System.out.println("FETCHED PLAYERS:");
	    	for(int w = 0; w < warden_cnt; w++) {
				   Warden tmp = wardens.get(w+"pos");
				   tmp.search_near(Online_u, player_count);
			}
	    	
	    	for(int i  = 0; i < player_count; i++) {
	    	  Player p = Online_u.get(i);
	    	  
	    	  boolean plrun = p.isSprinting();
	    	  boolean crawl =  p.isSneaking();
	    	  float falling = p.getFallDistance();
	    	  boolean danger = false;
	    	  if(crawl == false && plrun == true) {
	    	      Noisy_us.put(p, true);
	    	  }
	    		  
	    	  if(crawl == true) {
	    		  Noisy_us.put(p, false);
	    	  }
	    	  
	    	  if(falling>3) {
	    		  Noisy_us.put(p, true);
	    	  }
	    	  
	    	  for(int di = 0; di < danger_x_pos; di++) {
	    		  if(inDanger(di,p)) {
	    			  danger = true;
	    			  //System.out.println("PLAYER " + p.getDisplayName() + "IS IN DANGER! D:");
	    		  }
	    	  }
	    	  
	  		  if((Noisy_us.get(Online_u.get(i))==true||Noisy_block.get(Online_u.get(i))==true)&& danger ==true) {
	  			  //System.out.println("USER " + Online_u.get(i).getDisplayName() + " ITS NOISWY >n<");
	  			  Must_die.put(p,Must_die.get(p)-1);
	  			  Noisy_block.put(Online_u.get(i),false);
	  			  Noisy_us.put(Online_u.get(i),false);
	  		  }  
	  		  if(Must_die.get(p)<=0) {
	  			  //System.out.println("USER " + p.getDisplayName() + " MUST DIE! >:c");
	  			  p.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS,(15*20),1)));
	  			  p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 500.0f, -5.0f);
	  			  //p.setHealth(0);
	  			  
	  		  }
	  		  
	  	    }
	    	
	    }
   }.runTaskTimer(Main.getPlugin(Main.class),0L, 20L);
   
   new BukkitRunnable() {
	    public void run() {
	    	//System.out.println("FETCHED PLAYERS:");
	    	for(int i  = 0; i < player_count; i++) {
	    	  Player p = Online_u.get(i);
	    	  Must_die.put(p,3);
	  	    }
	    }
   }.runTaskTimer(Main.getPlugin(Main.class),10L, 200L);
  }
  
  public boolean inDanger(int id, Player p) {
	  int danger_x_max = danger_x.get(id+"pos")+32; int danger_x_min = danger_x.get(id+"pos")-32;
	  int danger_y_max = danger_y.get(id+"pos")+ 8; int danger_y_min = danger_y.get(id+"pos")- 8;
	  int danger_z_max = danger_z.get(id+"pos")+32; int danger_z_min = danger_z.get(id+"pos")-32;
	  if(
	     (p.getLocation().getBlockX()>danger_x_min && p.getLocation().getBlockX()<danger_x_max)&&
	     (p.getLocation().getBlockY()>danger_y_min && p.getLocation().getBlockY()<danger_y_max)&&
	     (p.getLocation().getBlockZ()>danger_z_min && p.getLocation().getBlockZ()<danger_z_max)
	  ) {
		 return true;
	  }else {
	     return false;
	  }
  }
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
   Player player = event.getPlayer();
   Noisy_block.put(player,true);
  }
  
  @EventHandler
  public void PlayerRespawn(PlayerRespawnEvent e) {
	  System.out.println("died");
	  Player p = e.getPlayer();
	  for(int w = 0; w < warden_cnt; w++) {
		  wardens.get(w+"pos").player_died(p);
	  }
  }
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
	 if(!e.getPlayer().getDisplayName().equalsIgnoreCase("Warden_npc")) {
	  Player p = e.getPlayer();
	  Noisy_block.put(p,false);
	  Must_die.put(p, 3);
	  Noisy_us.put(p,false);
	  Online_u.put(player_count, p);
	  player_count++;
	  for(int w = 0; w < warden_cnt; w++) {
		  Warden tmp = wardens.get(w+"pos");
		  tmp.handle_connection(p);
	  }
	  System.out.println("pushed: "+p+" on pos: "+player_count);
	 }else {
	  System.out.println("iswarden: 1");
	 }
  }
  
  @EventHandler
  public void onKick(PlayerKickEvent event){
	  handle_disconnect(event.getPlayer());
  }
  //And
  @EventHandler
  public void onLeave(PlayerQuitEvent event){
	  handle_disconnect(event.getPlayer());
  }
  
  public void handle_disconnect(Player p) {
	  Noisy_block.remove(p);
	  Online_u.remove(p);
	  Must_die.remove(p);
	  Noisy_us.remove(p);
	  player_count--;
  }
  
  @Override
  public void onDisable() {
	 
  }
  
  public void post_wloc() {
	  for(int i  = 0; i < player_count; i++) {
    	  Player pp = Online_u.get(i);
    	  
    	  for(int w = 0; w < warden_cnt;w++) {
    		  wardens.get(w+"pos").report_location(pp);
    	  }
	  }
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	  if(sender instanceof Player) {
		  Player p = (Player) sender;
		  if(label.equalsIgnoreCase("debugw")) {
			  if(args[0].equalsIgnoreCase("makecity")) {
				  if(args.length==4) {
					  int sgx = Integer.parseInt(args[1]);
					  int sgy = Integer.parseInt(args[2]);
					  int sgz = Integer.parseInt(args[3]);
					  
					  generate_maze(sgx,sgy,sgz);
					  
					  sender.sendMessage("Comand finished!");
				  }
			  }
			  if(args[0].equalsIgnoreCase("makewarden")) {
				  if(args.length==4) {
					  int sgx = Integer.parseInt(args[1]);
					  int sgy = Integer.parseInt(args[2]);
					  int sgz = Integer.parseInt(args[3]);
					  
					  wardens.put(warden_cnt+"pos", new Warden(sgx,sgy,sgz,p));
					  for(int pl = 0; pl < player_count; pl++) {
						  Warden aux = wardens.get(warden_cnt+"pos");
						  aux.handle_connection(Online_u.get(pl));
					  }
					  warden_cnt++;
					  post_wloc();
					  sender.sendMessage("Comand finished!");
				  }
			  }
			  
			  if(args[0].equalsIgnoreCase("lookat")) {
				  if(args.length==2) {
					  int wid = Integer.parseInt(args[1]);
					  for(int i = 0; i < player_count; i++) {
						System.out.println("to player: " + p + " look");
					    wardens.get(wid+"pos").look_towards(p,Online_u,player_count);
					  }
					  //post_wloc();
					  System.out.println("comand complete");
				  }
			  }
			  
			  if(args[0].equalsIgnoreCase("moveto")) {
				  if(args.length==5) {
					  int sgx = Integer.parseInt(args[1]);
					  int sgy = Integer.parseInt(args[2]);
					  int sgz = Integer.parseInt(args[3]);
					  int wid = Integer.parseInt(args[4]);
					  for(int i = 0; i < player_count; i++) {
							System.out.println("to player: " + p + " look");
						    wardens.get(wid+"pos").walk_distance(p,Online_u,player_count,sgx,sgy,sgz);
					  }
					  //post_wloc();
					  System.out.println("comand complete");  
				  }
			  }
			  
			  if(args[0].equalsIgnoreCase("reconnect")) {
				for(int w = 0; w < warden_cnt; w++) {
				   Warden tmp = wardens.get(w+"pos");
				   tmp.handle_connection(p);
				}
			  }
		  }
	  }
	  return false;
  }
}
