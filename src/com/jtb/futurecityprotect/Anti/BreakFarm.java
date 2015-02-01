package com.jtb.futurecityprotect.Anti;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.jtb.futurecityprotect.Main;

public class BreakFarm implements Listener {

	Main plugin;

	public BreakFarm(Main main) {
		plugin = main;
	}

	@EventHandler
	public void soilChangePlayer(PlayerInteractEvent event) {
		if (event.isCancelled()) {
			return;
		}
		if (event.getAction().equals(Action.PHYSICAL)
				&& event.getClickedBlock().getType().equals(Material.SOIL)) {
			event.getPlayer().sendMessage(plugin.servername
					+ "§6[§4保护系统§6]  §c服务器已开启耕地保护，请不要踩庄稼！");
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void soilChangeEntity(EntityInteractEvent event) {
		if (event.isCancelled()) {
			return;
		}
		if (event.getEntityType() != EntityType.PLAYER
				&& event.getBlock().getType().equals(Material.SOIL)) {
			event.setCancelled(true);
		}
	}
}
