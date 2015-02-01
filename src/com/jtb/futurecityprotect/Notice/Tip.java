package com.jtb.futurecityprotect.Notice;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.jtb.futurecityprotect.Main;

public class Tip implements Listener {
	Main plugin;
	String json;
	String tellraw;

	public Tip(Main instance) {
		plugin = instance;
		this.json = "{\"text\": \"[未来都市][网络系统] 点击访问未来都市贴吧！\",\"color\":\"red\",\"clickEvent\":{\"action\":\"open_url\",\"value\": \"http://tieba.baidu.com/未来都市服务器\"},\"hoverEvent\": {\"action\":\"show_text\",\"value\": \"点击访问未来都市服务器官方贴吧\"}}";
	}

	@EventHandler
	public void onJoin(final PlayerJoinEvent e) {
		new BukkitRunnable() {
			public void run() {
				Player p = e.getPlayer();
				tellraw = "tellraw " + p.getName() + " " + json;
				p.sendMessage( plugin.servername + "§6[§4保护系统§6] §a服务器已开启保护功能！");
				p.sendMessage( plugin.servername + "§6[§4保护系统§6] §c将会实时监控您的操作！");
				p.sendMessage( plugin.servername + "§6[§4保护系统§6] §b城市世界请圈地后建筑！");
				p.sendMessage( plugin.servername + "§6[§4保护系统§6] §e挖矿请带上火把或药水！");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), tellraw);
			}
		}.runTaskLater(plugin, 10);
	}

}
