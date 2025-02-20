package com.BoniiKwiiDz.ancientcraft;

import java.util.List;
import java.util.Scanner;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class Main  extends JavaPlugin{
	
	@Override
	public void onEnable()  {
		
	}

	@Override
	public void onDisable() {
		
	}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(sender instanceof Player) {
    	 if(label.equalsIgnoreCase("bonii")) {
    		Player player = (Player) sender;
    		if (args[0].equalsIgnoreCase("save")) {
    			if(args.length == 8) {
    				int loc_x1 = Integer.parseInt(args[1]);
    				int loc_y1 = Integer.parseInt(args[2]);
    				int loc_z1 = Integer.parseInt(args[3]);
    				
    				int loc_x2 = Integer.parseInt(args[4]);
    				int loc_y2 = Integer.parseInt(args[5]);
    				int loc_z2 = Integer.parseInt(args[6]);
    				
    				System.out.println("player: " + player + " requested: ");
    				System.out.println("from Loc X: " + loc_x1);
    				System.out.println("from Loc Y: " + loc_y1);
    				System.out.println("from Loc Z: " + loc_z1);
    				System.out.println("to Loc X: " + loc_x2);
    				System.out.println("to Loc Y: " + loc_y2);
    				System.out.println("to Loc Z: " + loc_z2);
    				try {
    				 File directory = new File("save_file");
    				 if(!directory.exists()) {
    					 directory.mkdir();
    				 }
    				 File wrl_segment = new File(directory,args[7]);
    				 FileWriter wrt = new FileWriter(wrl_segment);
    				 int lx,ly,lz,lr;
    				 int t_lx = 0;
    				 int t_ly = 0;
    				 int t_lz = 0;
    				 for(lz = loc_z1; lz <= loc_z2-1; lz++) {
    					for(ly = loc_y1; ly <= loc_y2; ly++) {
    						for(lx = loc_x1; lx <= loc_x2-1; lx++) {
    							Material blockf = player.getWorld().getBlockAt(lx,ly,lz).getType();
    						    lr = player.getWorld().getBlockAt(lx,ly,lz).getData();
    							System.out.println("Fetched block: " + blockf + " on: ("+lx+","+ly+","+lz+") with state: " + lr);
    							//player.getWorld().getBlockAt(lx,ly,lz).setType(Material.AIR);
    							System.out.println("SAVED INTO FILE FORMAT:");
    							System.out.print(lx + " " + ly + " " + lz + " " + lr + " " + blockf + "\n");
    							wrt.write(t_lx + " " + t_ly + " " + t_lz + " " + lr + " " + blockf + "\n"); 
    						 t_lx++;
    						}
    						t_lx=0;
    						t_ly++;
    					}
    					t_ly=0;
    					t_lz++;
    				 }
    				 t_lz=0;
    				 wrt.close();
    				 sender.sendMessage("successfully saved world segment");
    				}catch(IOException e) {
    					System.out.println("An error occurred.");
    				    e.printStackTrace();
    				}
    			}
    		}
    		if(args[0].equalsIgnoreCase("load")) {
    			if(args.length==5) {
    				File directory = new File("save_file");
    				File file2read = new File(directory,args[4]);
    				if(!file2read.exists()) {
    					sender.sendMessage("Could not find file");
    					return true;
    				}
    				try {
						Scanner scanf = new Scanner(file2read);
						while (scanf.hasNextLine()) {
							String blockr = scanf.nextLine();
							//sender.sendMessage(blockr);
							String[] cmdr = blockr.split(" ");
							int sx = Integer.parseInt(args[1])+Integer.parseInt(cmdr[0]);
							int sy = Integer.parseInt(args[2])+Integer.parseInt(cmdr[1]);
							int sz = Integer.parseInt(args[3])+Integer.parseInt(cmdr[2]);
							int sr = Integer.parseInt(cmdr[3]);
							player.getWorld().getBlockAt(sx,sy,sz).setType(Material.getMaterial(cmdr[4]));
							player.getWorld().getBlockAt(sx,sy,sz).setData((byte)sr);
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				sender.sendMessage("Structure Loaded successfully!");
    			}
    		}
    	 }
    	}
    	return false;
    }
}

