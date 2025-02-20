package com.BoniiKwiiDz.AncientDays;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EntityZombie;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntity;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.PacketPlayOutPosition;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import net.minecraft.server.v1_12_R1.PlayerInteractManager;
import net.minecraft.server.v1_12_R1.WorldServer;

public class Warden {
	public int ward_x = 0;
	public int ward_y = 0;
	public int ward_z = 0;
	public float ward_yaw = 0;
	public float ward_pitch = 0;
	//Player focused_on;
	EntityPlayer npc;
	public HashMap<Player, Integer>  NearbyPlayers = new HashMap<>(); int near_players = 0;
	public HashMap<Player, Boolean>  playercnt = new HashMap<>();
	
	public int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	public Warden(int loc_x, int loc_y, int loc_z, Player who) {
		System.out.println("making npc...");
		CraftPlayer craftPlayer = (CraftPlayer) who;
		MinecraftServer server = (MinecraftServer) craftPlayer.getHandle().server;
		WorldServer world_mc = (WorldServer) craftPlayer.getHandle().world;
		System.out.println("creating entity...");
		npc = new EntityPlayer(server,world_mc,new GameProfile(UUID.randomUUID(),"Warden_npc"), new PlayerInteractManager(world_mc));
	    
		System.out.println("envioronment defined... adding properties...");
	    
		int is_furry = getRandomNumber(1, 10);
		
		String texture = "";
		if(is_furry <= 6) {
		  texture = "ewogICJ0aW1lc3RhbXAiIDogMTY1Njk1MDIwNzU3MSwKICAicHJvZmlsZUlkIiA6ICI0M2NmNWJkNjUyMDM0YzU5ODVjMDIwYWI3NDE0OGQxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJrYW1pbDQ0NSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iMDNlMzYxNDExMjdiMjE2MDUyOTg5YjU1NzdlNTZlYjJlOTcwMDdhNjEwOTgzZTQ3MDcwY2Y0ZGFmMjUwMmE5IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
		}else {
		  texture = "ewogICJ0aW1lc3RhbXAiIDogMTY1NzE1OTYyMTE2NCwKICAicHJvZmlsZUlkIiA6ICI1ODkwNjAyNDYyMzE0ZGFjODM0NWQ3YjI4MmExZDI4ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJXeW5uY3JhZnRHYW1pbmciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODgwZWQ1N2JhZWVlODgxMjcxNjk3YmZhMGU3NzBkOTg2MzY2MWY5MmZjOTU0YThkNGRiYzMwMDI5YmY4M2JjOCIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
		}
		String signature = "";
		if(is_furry <= 6) {
		  signature = "GSUXCx0nPl88U3JdRLox8CD4pwgc7tEb1sZ2/b9k74FDil6JbRkalmQQFsoHlGjp2pbLPKNf+oPgUEUEmZMacobZjYgA/qa6Ou9uC7iUmm9KSRnJC6XxxrEr+1rljhWg4enBuiRftl7+7haeFDhhrIq7TLXAg73SWXY56l6Wfvuy7wDzAz175+0S4Ywhy1DPYewkA/t8ITuVe0d08dr2GcS5LXgnylirU1FXThtQPPcpxJ3p/m5jPPnOMcdgNuh8i8kG1k3dlL2FpmTry8DDHtLBCLLDMqi4pwHTq/5tYMyuN7XRupL0ItWTa3JksamSLDBGua4Wm233toNNnHA7uizR3DjCTw43DZSHBS6p4s6wuMOj2/nXHrintplk8VdxpM8gP29lK+ecXl/x/GYVdj6I5ligw0e2tyXj9n80DF3G5qAJCWyLellIAqvH/4FGr7PMqGzcGF66l0waGRn4z9KwqYhFGm1lAogWlEuOgP4vex3NQengQzM5QRkGQW89Z34eKohDlUgt/D/2643qqmRRtUIPTBYKBXzQX5pbOuK79UcKvFp/q+hgkr0K1h1ZrNfiMk2c0Ml/KJ0yeid4JBn7lYt7LLGSCP16BMSy1A1UcNDXPFSHlScV3TS8O3dXWWpqnOJaK/9Ym3LvrbD4Xms4KVw5p05dWMsy5OX+LYI=";
		}else {
		  signature = "REegROgZx7iCzhwqyftAEY0Bd2oJk18An245Uuz668U+7sgb34uAcaMoCjf/gF8v44AP7d/H94ESOfXOQ//5j2c8eFhtvtT+yDGop3jB427Zy28te/ZnrzTKmR1HvTyAMgKVSB3dx4EooaIcZaE91jHrhNI78bfcdkhzzw/a0H+jUkR7dQY4MwDzv/HNvCZ4tCK75/UdKmrQexQF8zG3Ag98x9RPUEbFIdxg1gBnYJ3aa6qk1B9iiyiilDmehCgKsIWvhCL5vJpkJIUb/yqxALPspMiLby/iEoY8Z9bwiMFDsn8xemyhOtTMjBX3knZsbiZ5FJwF7bAOUe38Zuz6Qz5/MeuGTA/T91Y26AD9ggGz9L2QOslp1BYiIK3wQjGD2HXV0ROz8JZlmNupzNNDamlizJA5cwgH3JoGQUnfj/SBkqRBA2dDEw2xnl5tAOXn2oCeNjU9K6+Ue3iJ9TUaoR5fX2OHwKkIBObBh/L/SeQjSMHhSU1o+K128xi7WZS0SgywBdYrUu1/phF3EdjZCr9N3vyVUTPXuQ1IBmYqCjV/CfGvf2l0y33OFFZN/0KKED2v8itdM/Kk+7tBMjhMoO5DPmoODpZjnqKPOgMy9j2RfnxuDT/1rff944MiCos2nKrWrEgRNqQkMMi8+AmHEzevEXWTjGl2iBkoip5QlG0=";
		}
		
		npc.getProfile().getProperties().put("textures", new Property("textures",texture, signature));
		
	    npc.setInvulnerable(false);
		
		ward_x = loc_x;
		ward_y = loc_y;
		ward_z = loc_z;
		System.out.println("npc made...");
	}
	
	public void handle_connection(Player p) {
		System.out.println("connecting npc...");
		CraftPlayer craftPlayer = (CraftPlayer) p;
		PlayerConnection connection = ((CraftPlayer)p).getHandle().playerConnection;
		npc.setLocation(ward_x, ward_y, ward_z,0,0);
		connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc)); 
	    connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
	    connection.sendPacket(new PacketPlayOutEntityTeleport(npc));
	    connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte)(ward_yaw * 256 / 360)));
        connection.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte)(ward_yaw * 256 / 360), (byte)(ward_pitch * 256 / 360), true));
        //connection.sendPacket(new PacketPlayOutEntityTeleport(npc.getId(),ward_x,ward_y,ward_z,ward_yaw,ward_pitch,true));
        System.out.println("npc connected...");  
	}
	
	public void report_location(Player p) {
        CraftPlayer craftplayer = (CraftPlayer) p;
        PlayerConnection connection = ((CraftPlayer)p).getHandle().playerConnection;
		npc.setLocation(ward_x, ward_y, ward_z,0,0);
		connection.sendPacket(new PacketPlayOutEntityTeleport(npc));
		connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
		connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte)(ward_yaw * 256 / 360)));
        connection.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte)(ward_yaw * 256 / 360), (byte)(ward_pitch * 256 / 360), true));
		//connection.sendPacket(new PacketPlayOutPosition(ward_x,ward_y,ward_z,0, 0, null,0));
		//look_towards(p, com.BoniiKwiiDz.AncientDays.Main.classOnline_u, );
	}
	
	public void look_towards(Player p, HashMap<Integer, Player> map, int len) {
		// GENERATE CONNECTION FOR PLAYER
		for(int i = 0; i < len; i++) {
		System.out.println("player: " + map.get(i).getDisplayName() + " length: " + len + " ran: " + i );
		PlayerConnection connection = ((CraftPlayer)map.get(i)).getHandle().playerConnection;
		
		// CALCULATE PLAYER'S POSITION
		Location npc_loc = npc.getBukkitEntity().getLocation();
		
		npc_loc.setDirection(p.getLocation().subtract(npc_loc).toVector());
		
		float yaw = npc_loc.getYaw();
		float ptc = npc_loc.getPitch();
		ward_yaw = yaw;
		ward_pitch = ptc;
		// MAKE THE MOVE
		connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte)(yaw * 256 / 360)));
        connection.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte)(yaw * 256 / 360), (byte)(ptc * 256 / 360), true));
        
        //focused_on = p;
		}
		//System.out.println("For warden Yaw: " + ward_yaw + " pitch: " + ward_pitch);
	}
	
	public void put_player(Player p) {
		NearbyPlayers.put(p,near_players);
		near_players++;
	}
	
	public void pop_player(Player p) {
		NearbyPlayers.remove(p);
		near_players--;
	}
	public void player_died(Player p) {
		pop_player(p);
		playercnt.put(p,false);
	}
	
	public void search_near(HashMap<Integer, Player>map, int len) {
		for(int i = 0; i < len; i++) {
			//System.out.println("debugging for player: " + map.get(i));
			if((NearbyPlayers.containsKey(map.get(i)))==false) {
			  //System.out.println("user is not the list");
			  int lplayer_x = map.get(i).getLocation().getBlockX();
			  int lplayer_y = map.get(i).getLocation().getBlockY();
			  int lplayer_z = map.get(i).getLocation().getBlockZ();
			 // System.out.println("fetched players info...");
			  if(
			    (lplayer_x>ward_x-16 && lplayer_x<ward_x+16)&&
			    (lplayer_y>ward_y-16 && lplayer_y<ward_y+16)&&
			    (lplayer_z>ward_z-16 && lplayer_z<ward_z+16)
			  ) {
			//	System.out.println("player is inside danger range");
			//	System.out.println("[is on the map? "+ (NearbyPlayers.containsKey(map.get(i)))+ " has verified? " + playercnt.get(map.get(i)) + " ]");
				if((!(NearbyPlayers.containsKey(map.get(i))))||(playercnt.get(map.get(i))==false)) {
					put_player(map.get(i));
					playercnt.put(map.get(i),true);
					handle_connection(map.get(i));
				}
				//System.out.println("on list there is: " + NearbyPlayers.get(i));
			  }else {
			   if((NearbyPlayers.containsKey(map.get(i)))) {
				  pop_player(map.get(i));
				  playercnt.put(map.get(i),false);
			   }
			  }
			}
		}
	}
	
	public void walk_distance(Player p, HashMap<Integer, Player>map, int len, int _x, int _y, int _z) {
		for(int i = 0; i < len; i++) {
			System.out.println("player: " + map.get(i).getDisplayName() + " length: " + len + " ran: " + i );
			PlayerConnection connection = ((CraftPlayer)map.get(i)).getHandle().playerConnection;
			connection.sendPacket(new PacketPlayOutEntity.PacketPlayOutRelEntityMove(npc.getId(), (short)(_x * 4096), (short)(_y * 4096), (short)(_z * 4096), true));
		}
		ward_x = npc.getBukkitEntity().getLocation().getBlockX()+(_x);
		ward_y = npc.getBukkitEntity().getLocation().getBlockY()+(_y);
		ward_z = npc.getBukkitEntity().getLocation().getBlockZ()+(_z);
		
		//System.out.println("warden moved to x: " + ward_x + " y: " + ward_y + " z: " + ward_z + " point");
		npc.setLocation(ward_x, ward_y, ward_z,0,0);
	}
}
