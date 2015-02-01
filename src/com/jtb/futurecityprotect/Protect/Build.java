package com.jtb.futurecityprotect.Protect;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.jtb.futurecityprotect.Main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class Build implements Listener {

	Main plugin;

	public Build(Main main) {
		plugin = main;
	}

	@EventHandler(ignoreCancelled = true)
	public void blockStopBreak(BlockBreakEvent event) {
		if (CancelEvent(event.getPlayer(), event.getBlock(), false)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void blockStopplace(BlockPlaceEvent event) {
		if (CancelEvent(event.getPlayer(), event.getBlock(), true)) {
			event.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	public boolean CancelEvent(Player p, Block b, boolean place) {
		Location loc = b.getLocation();
		ClaimedResidence res = Residence.getResidenceManager().getByLoc(loc);
		if (res == null && !(p.hasPermission("fcp.allow.build"))) {
			int blockid = b.getTypeId();
			World blockw = b.getWorld();
			int blockx = b.getX();
			int blocky = b.getY();
			int blockz = b.getZ();
			for (String allowid : plugin.getConfig()
					.getStringList("CityBuild.AllowList")) {
				if (blockid == Integer.valueOf(allowid)) {
					if ((blockid == 54 || blockid == 61 || blockid == 324)
							&& place) {
						p.getPlayer()
								.sendMessage(plugin.servername + "§6[§4保护系统§6] §c未在领地内建筑，请确保您放置的方块安全（圈地后无提示）！");
						return false;
					}
					int maxheight = plugin.getConfig().getInt("CityBuild.MaxHeight");
					if (blocky > maxheight && blockid == 3) {
						p.getPlayer()
								.sendMessage(plugin.servername + "§6[§4保护系统§6] §c未在领地内建筑，请不要用§e泥土§c建造高于100的建筑！");
						Bukkit.broadcast(plugin.servername + "§6[§4建筑保护§6] §6玩家§r：§a"
								+ p.getName() + " §e未在领地内§c用泥土建筑高度超过 §5"
								+ maxheight + "§c 的建筑物，！", "fcp.notice");
						Bukkit.broadcast(plugin.servername + "§6[§4建筑保护§6] §5坐标定位§r：§a世界"
										+ blockw.getName() + " §dX:" + blockx
										+ " §dZ:" + blockz + "  §4请管理到目标地点检查！",
								"fcp.notice");
						return true;
					}
					return false;
				}
			}
			p.getPlayer().sendMessage(plugin.servername + "§6[§4保护系统§6]  §4城市世界除以下方块外禁止放置/破坏！");
			p.getPlayer()
					.sendMessage(plugin.servername + "§6[§4保护系统§6]  §a允许的方块:§e箱子,§5熔炉,§a工作台,§6木板(树木),§d泥土,§9木门");
			p.getPlayer()
					.sendMessage(plugin.servername + "§6[§4保护系统§6]  §c如需解除限制请圈地,§6工具：木锄§c,§b价格：0.3元/方块§c！");
			return true;
		}
		return false;
	}
}
