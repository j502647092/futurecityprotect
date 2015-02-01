package com.jtb.futurecityprotect.Protect;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;
import com.jtb.futurecityprotect.Main;

public class ResFly implements Listener {

	HashMap<Player, Player> fall = new HashMap<Player, Player>();
	Main plugin;

	public ResFly(Main main) {
		plugin = main;
	}

	@EventHandler(ignoreCancelled = true)
	public void PlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		ClaimedResidence resto = Residence.getResidenceManager().getByLoc(
				event.getTo());
		if (permCheck(player)) {
			if (!resPermCheck(resto, player)) {
				ChangePlayerFly(player, false);
			} else {
				ChangePlayerFly(player, true);
			}
		}

	}

	@EventHandler
	public void onFallDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getCause() == DamageCause.FALL) {
				if (p == fall.get(p)) {
					fall.remove(p);
					e.setCancelled(true);
					p.sendMessage(plugin.servername + "§6[§4保护系统§6]  §a飞行保护，摔落取消！");
				}
			}
		}
	}

	void ChangePlayerFly(Player p, boolean fly) {
		if (p.getAllowFlight() && !fly) {
			if (p.isFlying())
				fall.put(p, p);
			p.setAllowFlight(false);
			p.sendMessage(plugin.servername
					+ "§6[§4保护系统§6]  §c当前区域禁止飞行，已关闭您的飞行模式！");
		}
		if (!p.getAllowFlight() && fly) {
			if (p == fall.get(p))
				fall.remove(p);
			p.setAllowFlight(true);
			p.sendMessage(plugin.servername
					+ "§6[§4保护系统§6]  §a当前区域允许飞行，已开启您的飞行模式！");
		}
	}

	boolean permCheck(Player p) {
		if (p.hasPermission("fcp.ignore.resfly") || p.isOp()) {
			return false;
		}
		return true;
	}

	boolean resPermCheck(ClaimedResidence res, Player p) {
		if (res == null) {
			return false;
		}
		ResidencePermissions perms = res.getPermissions();
		if (perms.playerHas(p.getName(), "fly", false)) {
			return true;
		}
		return false;
	}
}