package com.jtb.futurecityprotect.Anti;

import java.util.Collection;
import java.util.Iterator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;

import com.jtb.futurecityprotect.Main;

public class Nightvision implements Listener {

	Main plugin;

	public Nightvision(Main main) {
		plugin = main;
	}

	@EventHandler
	public void OnBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (p.hasPermission("fcp.allow.nightvision") || p.isOp()) {
			return;
		}
		int light = p.getLocation().getBlock().getLightLevel();
		if (light > 0) {
			return;
		}
		Block b = e.getBlock();
		Material blockid = b.getType();
		Location loc = b.getLocation();
		World World = loc.getWorld();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		Location loc_top = new Location(World, x, y + 1, z);
		if (loc_top.getBlock().getType() == Material.AIR) {
			return;
		}
		Collection<PotionEffect> effect = p.getActivePotionEffects();
		Iterator<PotionEffect> iterator = effect.iterator();
		Boolean flag = Boolean.valueOf(false);
		while (iterator.hasNext()) {
			PotionEffect element = (PotionEffect) iterator.next();
			if (element.toString().toUpperCase().contains("NIGHT_VISION")) {
				flag = Boolean.valueOf(true);
				break;
			}
		}
		if (!flag.booleanValue()) {
			if (blockid == Material.AIR) {
				return;
			}
			if (blockid == Material.STONE || blockid == Material.GOLD_ORE
					|| blockid == Material.IRON_ORE
					|| blockid == Material.COAL_ORE
					|| blockid == Material.DIAMOND_ORE
					|| blockid == Material.REDSTONE_ORE
					|| blockid == Material.LAPIS_ORE
					|| blockid == Material.EMERALD_ORE) {
				e.setCancelled(true);
				p.sendMessage(plugin.servername
						+ "§6[§4保护系统§6] §c为防止无限夜视作弊，以阻止挖矿，请插火把或用夜视药水！");
			}
		}
	}
}