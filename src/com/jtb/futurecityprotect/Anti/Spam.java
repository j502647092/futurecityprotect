/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtb.futurecityprotect.Anti;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.jtb.futurecityprotect.Main;

/**
 * 
 * @author Administrator
 */
public class Spam implements Listener {

	private final HashMap<String, Long> lT;
	private final HashMap<String, String> lC;
	String AntiMsg;
	Main plugin;

	public Spam(Main main) {
		plugin = main;
		this.lT = new HashMap<String, Long>();
		this.lC = new HashMap<String, String>();
	}

	@EventHandler(ignoreCancelled = true)
	public void PlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String n = p.getName();
		String s = e.getMessage();
		if (p.hasPermission("fcp.ignore.spam")) {
			return;
		}
		if (AntiChat(n, s)) {
			p.sendMessage(plugin.servername + "§6[§4刷屏检测§6] §c" + AntiMsg);
			e.setCancelled(true);
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

	public void SaveMsg(String n, String s) {
		lC.put(n, s);
		lT.put(n, System.currentTimeMillis() / 1000);
	}
}
