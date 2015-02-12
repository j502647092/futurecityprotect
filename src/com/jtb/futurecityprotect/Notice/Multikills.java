/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtb.futurecityprotect.Notice;

import java.util.HashMap;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
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
 * @author 蒋天蓓
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
		EntityDamageEvent ki = p.getLastDamageCause();
		Entity gk = p.getKiller();
		// Entity Ek = p.getKiller();
		prefix = plugin.getmessage("Multikills.prefix");
		String message = null;
		if (gk instanceof Player) {
			if (kill.containsKey(p)) {
				if (kill.get(p) > 2) {
					message = getmsg("shutdown", (Player) gk, p, null);
					Bukkit.broadcastMessage(message);
				}
				kill.remove(p);
			}
		}
		switch (p.getLastDamageCause().getCause()) {
		case ENTITY_ATTACK:
			if (gk instanceof Player) {
				message = resetmsg(e, (Player) gk, p);
				break;
			}
			if (ki instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent kie = (EntityDamageByEntityEvent) ki;
				gk = kie.getDamager();
			}
			if (gk instanceof Creeper) {
				message = getmsg("creeper", null, p, null);
				break;
			}
			if (gk instanceof Zombie) {
				message = getmsg("zombie", null, p, null);
				break;
			}
			if (gk instanceof Spider) {
				message = getmsg("spider", null, p, null);
				break;
			}
			if (gk instanceof CaveSpider) {
				message = getmsg("spider", null, p, null);
				break;
			}
			if (gk instanceof Enderman) {
				message = getmsg("enderman", null, p, null);
				break;
			}
			if (gk instanceof Silverfish) {
				message = getmsg("silverfish", null, p, null);
				break;
			}
			if (gk instanceof MagmaCube) {
				message = getmsg("lavaslime", null, p, null);
				break;
			}
			if (gk instanceof Slime) {
				message = getmsg("slime", null, p, null);
				break;
			}
			if (gk instanceof WitherSkull) {
				message = getmsg("witherskull", null, p, null);
				break;
			}
			if (gk instanceof Skeleton) {
				message = getmsg("skeleton", null, p, null);
				break;
			}
			if (gk instanceof Wolf) {
				message = getmsg("wolf", null, p, null);
				break;
			}
			if (gk instanceof PigZombie) {
				message = getmsg("pigzombie", null, p, null);
				break;
			}
			if (gk instanceof IronGolem) {
				message = getmsg("irongolem", null, p, null);
				break;
			}
			if (gk instanceof Giant) {
				message = getmsg("giant", null, p, null);
				break;
			}
			if (gk instanceof Wither) {
				message = getmsg("wither", null, p, null);
				break;
			}
			if (gk instanceof Blaze) {
				message = getmsg("blaze", null, p, null);
				break;
			}
			if (gk instanceof Ghast) {
				message = getmsg("ghast", null, p, null);
				break;
			}
			if (gk instanceof EnderDragon) {
				message = getmsg("enderdragon", null, p, null);
				break;
			}
			break;
		case PROJECTILE:
			if (gk instanceof Player) {
				message = resetmsg(e, (Player) gk, p);
				break;
			}
			if (gk instanceof Skeleton) {
				message = getmsg("skeleton", null, p, null);
				break;
			}
			if (gk instanceof Ghast) {
				message = getmsg("ghast", null, p, null);
				break;
			}
			// if (ki instanceof EntityDamageByEntityEvent) {
			// EntityDamageByEntityEvent kie = (EntityDamageByEntityEvent) ki;
			// Entity damager = kie.getDamager();
			// Projectile pro = (Projectile) damager;
			// if (pro.getShooter() instanceof Player) {
			// message = resetmsg(e, (Player) pro.getShooter(), p);
			// break;
			// }
			// if (pro.getShooter() instanceof Skeleton) {
			// message = getmsg("skeleton", null, p, null);
			// break;
			// }
			// if (damager instanceof Ghast
			// || pro.getShooter() instanceof Ghast) {
			// message = getmsg("ghast", null, p, null);
			// break;
			// }
			// }
			break;
		case DROWNING:
			message = getmsg("drown", null, p, null);
			break;
		case STARVATION:
			message = getmsg("hunger", null, p, null);
			break;
		case SUFFOCATION:
			message = getmsg("suffocate", null, p, null);
			break;
		case SUICIDE:
			message = getmsg("suicide", null, p, null);
			break;
		case FIRE_TICK:
			message = getmsg("firetick", null, p, null);
			break;
		case FALL:
			message = getmsg("fall", null, p, null);
			break;
		case LAVA:
			message = getmsg("lava", null, p, null);
			break;
		case WITHER:
			message = getmsg("witherstate", null, p, null);
			break;
		case ENTITY_EXPLOSION:
			if (gk instanceof EnderCrystal) {
				message = getmsg("endercrystal", null, p, null);
				break;
			}
			if (ki.equals(EntityType.ENDER_CRYSTAL)) {
				message = getmsg("endercrystal", null, p, null);
				break;
			}
			if(ki.getEntityType()==EntityType.ENDER_CRYSTAL){
				message = getmsg("endercrystal", null, p, null);
				break;
			}
			if (gk instanceof Creeper) {
				message = getmsg("creeper", null, p, null);
				break;
			}
			if (gk instanceof Player) {
				message = getmsg("endercrystal", null, p, null);
				break;
			}
			break;
		case LIGHTNING:
			message = getmsg("lighting", null, p, null);
			break;
		case THORNS:
			message = getmsg("thorns", (Player) gk, p, null);
			multikill((Player) gk, p);
			break;
		}
		if (message != null)
			e.setDeathMessage(message);
	}

	public String resetmsg(PlayerDeathEvent e, Player gk, Player p) {
		Player k = p.getKiller();
		String inhandname = null;
		String message = null;
		ItemStack IIH = k.getItemInHand();
		String inhandtype = IIH.getType().name();
		if (IIH != null) {
			ItemMeta IM = IIH.getItemMeta();
			if (IIH.getType() != Material.AIR) {
				if (IM.hasDisplayName()) {
					inhandname = IM.getDisplayName();
				}
			}
			if (inhandname == null) {
				inhandname = plugin.itemConfig.getString(inhandtype);// "ItemName."
			}
			if (inhandname == null) {
				inhandname = inhandtype;
				plugin.itemConfig.set(inhandtype, inhandtype);
			}
		}
		message = getmsg("player", k, p, inhandname);
		multikill(k, p);
		return message;
	}

	public void multikill(Player k, Player v) {
		Integer kills = put(k);
		String message = null;
		if (kills > 1) {
			if (kills < 9) {
				message = prefix + " " + getmsg(kills + "kills", k, v, null);
				sendmsg(message);
			} else {
				message = prefix + " " + getmsg("morekills", k, v, null);
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

	public String getmsg(String type, Player k, Player v, String inhanditem) {
		String message = plugin.getmessage("Multikills." + type);
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