/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtb.futurecityprotect.Notice;

import java.util.HashMap;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.jtb.futurecityprotect.Main;

/**
 * 
 * @author Administrator
 */
public class Multikills implements Listener {

	public static Main plugin;
	public HashMap<Player, Integer> kill = new HashMap<Player, Integer>();
	public String prefix;

	public Multikills(Main main) {
		plugin = main;
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		Entity gk = p.getKiller();
		prefix = plugin.getmessage("Multikills.prefix");
		String message = null;
		if (gk instanceof Player) {
			if (kill.containsKey(p)) {
				if (kill.get(p) > 2) {
					message = getmsg("Multikills.shutdown", (Player) gk, p,
							null);
					Bukkit.broadcastMessage(message);
				}
				kill.remove(p);
			}
		}
		switch (p.getLastDamageCause().getCause()) {
		case ENTITY_ATTACK:
			if (gk instanceof Player) {
				message = resetmsg(e, (Player) gk, p);
			}
			break;
		case PROJECTILE:
			EntityDamageEvent ki = p.getLastDamageCause();
			if (ki instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent kie = (EntityDamageByEntityEvent) ki;
				Entity damager = kie.getDamager();
				Projectile pro = (Projectile) damager;
				if (pro.getShooter() instanceof Player) {
					message = resetmsg(e, (Player) pro.getShooter(), p);
				}
			}
			break;
		case DROWNING:
			message = getmsg("Multikills.drown", null, p, null);
			break;
		case STARVATION:
			message = getmsg("Multikills.starve", null, p, null);
			break;
		case SUFFOCATION:
			message = getmsg("Multikills.suffocate", null, p, null);
			break;
		case SUICIDE:
			message = getmsg("Multikills.suicide", null, p, null);
			break;
		case FIRE_TICK:
			message = getmsg("Multikills.firetick", null, p, null);
			break;
		case FALL:
			message = getmsg("Multikills.fall", null, p, null);
			break;
		case LAVA:
			message = getmsg("Multikills.lava", null, p, null);
			break;
		case WITHER:
			break;
		case ENTITY_EXPLOSION:
			break;
		case LIGHTNING:
			break;
		case THORNS:
			message = getmsg("Multikills.thorns", (Player) gk, p, null);
			multikill((Player) gk, p);
			break;
		}
		e.setDeathMessage(message);
	}

	public String resetmsg(PlayerDeathEvent e, Player gk, Player p) {
		Player k = p.getKiller();
		String inhandtype = k.getItemInHand().getType().name();
		ItemStack IIH = k.getItemInHand();
		ItemMeta IM = IIH.getItemMeta();
		String inhandname = null;
		String message = null;
		if (IM.hasDisplayName()) {
			inhandname = IM.getDisplayName();
		} else {
			inhandname = plugin.itemConfig.getString(inhandtype);// "ItemName."
		}
		if (inhandname == null) {
			inhandname = inhandtype;
			plugin.itemConfig.set(inhandtype, inhandtype);
		}
		message = getmsg("Multikills.player", k, p, inhandname);
		multikill(k, p);
		return message;
	}

	public void multikill(Player k, Player v) {
		Integer kills = put(k);
		String message = null;
		if (kills > 1) {
			if (kills < 9) {
				message = prefix + " "
						+ getmsg("Multikills." + kills, k, v, null);
				sendmsg(message);
			} else {
				message = prefix + " " + getmsg("Multikills.more", k, v, null);
				sendmsg(message);
			}
		}
	}

	public void sendmsg(String msg) {
		if (plugin.BossBar
				&& plugin.getConfig().getBoolean("Multikills.BossBar")) {
			BarAPI.setMessage(msg, 5);
		} else {
			Bukkit.broadcastMessage(msg);
		}
	}

	public String getmsg(String path, Player k, Player v, String inhanditem) {
		String message = plugin.getmessage(path);
		if (k != null)
			message = message.replace("%killer%", " " + k.getDisplayName()
					+ " ");
		if (v != null)
			message = message.replace("%victim%", " " + v.getDisplayName()
					+ " ");
		if (inhanditem != null)
			message = message.replace("%itemname%", " " + inhanditem + " ");
		return message;
	}

	public Integer put(Player p) {
		if (kill.containsKey(p)) {
			int i = kill.remove(p);
			kill.put(p, i + 1);
			return i + 1;
		} else {
			kill.put(p, 1);
			return 1;
		}
	}
}