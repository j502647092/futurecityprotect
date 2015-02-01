package com.jtb.futurecityprotect.Protect;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.jtb.futurecityprotect.Main;

public class NetherDoor implements Listener {

	public FileConfiguration getConfig;
	public Location spawn;
	Main plugin;

	public NetherDoor(Main main) {
		plugin = main;
	}

	// public NetherDoor() {
	// throw new UnsupportedOperationException("Not yet implemented");
	// }
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(final PlayerJoinEvent e) {
		new BukkitRunnable() {

			public void run() {
				Player p = e.getPlayer();
				Location loc = p.getLocation();
				World world = loc.getWorld();
				double x = loc.getX();
				double y = loc.getY();
				double z = loc.getZ();
				Location loc_top = new Location(world, x, y + 1.0D, z);
				Location loc_bottom = new Location(world, x, y - 1.0D, z);
				Location loc_xleft = new Location(world, x + 1.0D, y, z);
				Location loc_xright = new Location(world, x - 1.0D, y, z);
				Location loc_zleft = new Location(world, x, y, z + 1.0D);
				Location loc_zright = new Location(world, x, y, z - 1.0D);
				if (loc_top.getBlock().getTypeId() == 90
						|| loc_bottom.getBlock().getTypeId() == 90
						|| loc_xleft.getBlock().getTypeId() == 90
						|| loc_xright.getBlock().getTypeId() == 90
						|| loc_zleft.getBlock().getTypeId() == 90
						|| loc_zright.getBlock().getTypeId() == 90) {
					p.sendMessage(plugin.servername
							+ "§6[§4保护系统§6]  §5为防止您卡在地狱门，现在将您传送回主城！");
					World lworld = Bukkit.getWorld((String) plugin.getConfig()
							.get("SafeNetherDoor.World", "world"));
					double lx = plugin.getConfig()
							.getInt("SafeNetherDoor.X", 0);
					double ly = plugin.getConfig()
							.getInt("SafeNetherDoor.Y", 0);
					double lz = plugin.getConfig()
							.getInt("SafeNetherDoor.Z", 0);
					spawn = new Location(lworld, lx, ly, lz);
					p.teleport(spawn);
				}
			}
		}.runTaskLater(plugin, 15);
	}
}
