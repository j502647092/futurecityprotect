/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtb.futurecityprotect.Anti;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.jtb.futurecityprotect.Main;

/**
 * 
 * @author 蒋天蓓
 */
public class Spam implements Runnable, Listener, CommandExecutor {

	private final HashMap<String, Long> lT;
	private final HashMap<String, String> lC;
	HashMap<Player, Integer> tc = new HashMap<Player, Integer>();
	HashMap<Player, Player> cc = new HashMap<Player, Player>();
	String AntiMsg;
	boolean adminchat;
	Main plugin;

	public Spam(Main main) {
		plugin = main;
		this.lT = new HashMap<String, Long>();
		this.lC = new HashMap<String, String>();
	}

	public void run() {
		tc.clear();
	}

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (command.getName().equalsIgnoreCase("spam")) {
			if (!sender.hasPermission("spam.admin")){
				sender.sendMessage(plugin.servername + " "
						+ plugin.getmessage("no-permission"));
				return true;
			}
			if (args.length == 2) {
				Player p = Bukkit.getPlayer(args[1]);
				if (args[0].equalsIgnoreCase("add")) {
					if (p != null) {
						cc.put(p, p);
						sender.sendMessage(plugin.servername + "  §c已禁止玩家 "
								+ p.getDisplayName() + " §c聊天！");
					} else {
						sender.sendMessage(plugin.servername + "  §c玩家不存在或不在线！");
					}
				}
				if (args[0].equalsIgnoreCase("del")) {
					if (p != null && cc.containsKey(p)) {
						cc.remove(p);
						sender.sendMessage(plugin.servername + "  §a已允许玩家 "
								+ p.getDisplayName() + " §a聊天！");
					} else {
						sender.sendMessage(plugin.servername
								+ "  §c玩家未被禁言或不存在！");
					}
				}
				if (args[0].equalsIgnoreCase("admin")) {
					if (args[1].equalsIgnoreCase("on")) {
						adminchat = true;
						sender.sendMessage(plugin.servername + "  §a服务器已开启管理员聊天！");
						return true;
					}
					if (args[1].equalsIgnoreCase("off")) {
						adminchat = false;
						sender.sendMessage(plugin.servername + "  §c服务器已关闭管理员聊天！");
						return true;
					}
				}
				return true;
			}
			return false;
		}
		return false;
	}

	@EventHandler(ignoreCancelled = true)
	public void PlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String n = p.getName();
		String s = e.getMessage();
		if (p.hasPermission("fcp.ignore.spam")) {
			return;
		}
		if (adminchat) {
			p.sendMessage(plugin.servername + "  §c服务器已开启管理员聊天！");
			e.setCancelled(true);
			return;
		}
		if (cc.containsKey(p)) {
			p.sendMessage(plugin.servername + "  §c您已被禁言，请联系管理员解禁！");
			e.setCancelled(true);
			return;
		}
		if (AntiChat(n, s)) {
			p.sendMessage(plugin.servername + "§6[§4刷屏检测§6] §c" + AntiMsg);
			e.setCancelled(true);
			put(p);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void PlayerCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		String n = p.getName();
		String s = e.getMessage();
		if (p.hasPermission("fcp.ignore.spam")) {
			return;
		}
		if (CommandWait(n, s)) {
			put(p);
			p.sendMessage(plugin.servername + "§6[§4命令检测§6] §c" + AntiMsg);
			e.setCancelled(true);
		}
	}

	public boolean AntiChat(String n, String s) {
		String lm = lC.get(n);
		Long lt = lT.get(n);
		long nt = System.currentTimeMillis() / 1000;
		if (lm == null || lt == null) {
			AntiMsg = "";
			SaveMsg(n, s);
			return false;
		}
		if (lt + plugin.getConfig().getLong("Spam.ChatWait", 1) > nt) {
			AntiMsg = "请不要短时间内发送大量消息！";
			return true;
		}
		if (lm.equals(s)) {
			if (lt + plugin.getConfig().getLong("Spam.SameChatWait", 5) > nt) {
				AntiMsg = "请不要短时间内发送重复的消息！";
				return true;
			}
		}
		AntiMsg = "";
		SaveMsg(n, s);
		return false;
	}

	public boolean CommandWait(String n, String s) {
		String lm = lC.get(n);
		Long lt = lT.get(n);
		long nt = System.currentTimeMillis() / 1000;
		if (lm == null || lt == null) {
			AntiMsg = "";
			SaveMsg(n, s);
			return false;
		}
		if (lt + plugin.getConfig().getLong("Spam.CommandWait", 1) > nt) {
			AntiMsg = "请不要短时间内发送大量命令";
			return true;
		}
		if (lm.equals(s)) {
			if (lt + plugin.getConfig().getLong("Spam.SameCommandWait", 3) > nt) {
				AntiMsg = "请不要短时间内发送相同的命令";
				return true;
			}
		}
		SaveMsg(n, s);
		return false;
	}

	public void put(Player p) {
		if (tc.containsKey(p)) {
			int t = tc.get(p);
			if (t > plugin.getConfig().getLong("Spam.KickCheck", 3)) {
				p.sendMessage(plugin.servername + "  §c您由于多次刷屏已被禁言，请联系管理解禁！");
				cc.put(p, p);
			} else {
				tc.put(p, t + 1);
			}
		} else {
			tc.put(p, 1);
		}
	}

	public void SaveMsg(String n, String s) {
		lC.put(n, s);
		lT.put(n, System.currentTimeMillis() / 1000);
	}

}
