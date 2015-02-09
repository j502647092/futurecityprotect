package com.jtb.futurecityprotect.Protect;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.jtb.futurecityprotect.Main;

public class ClientCheck implements Listener {

	Main plugin;

	public ClientCheck(Main instance) {
		// TODO Auto-generated constructor stub
		plugin = instance;
	}
	
	@EventHandler
	public void onJoin(final PlayerJoinEvent e){
		Player p=e.getPlayer();
		p.getDisplayName();
	}
	
}
