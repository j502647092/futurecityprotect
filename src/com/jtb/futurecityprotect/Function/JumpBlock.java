package com.jtb.futurecityprotect.Function;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;

import com.jtb.futurecityprotect.Main;

public class JumpBlock implements Listener, CommandExecutor {

	HashMap<Player, Player> fall = new HashMap<Player, Player>();
	Main plugin;

	public JumpBlock(Main main) {
		plugin = main;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("set")) {
				plugin.getConfig().set("JumpBlock.BEDROCK",
						Double.valueOf(args[1]));
				sender.sendMessage(plugin.servername + "  §a基岩的跳板弹跳倍数已设置为 "
						+ plugin.getConfig().getDouble("JumpBlock.BEDROCK")
						+ "倍！");
				return true;
			}
			return false;
		}
		return false;
	}

	@EventHandler()
	public void onJump(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location gb = e.getTo();
		if (gb.getBlock().getType() == Material.GOLD_PLATE) {
			Block loc = gb.add(0.0D, -1.0D, 0.0D).getBlock();
			Block loc_1 = gb.add(0.0D, -1.0D, 0.0D).getBlock();
			Block loc_2 = gb.add(0.0D, -1.0D, 0.0D).getBlock();
			if (loc_1.getType() == Material.GLASS
					&& loc_2.getType() == Material.LAPIS_BLOCK) {
				if (!p.hasPermission("jumpblock.use")) {
					p.sendMessage(plugin.servername
							+ plugin.getmessage("no-permission"));
					return;
				}
				switch (loc.getType()) {
				case IRON_BLOCK:
					fall.put(p, p);
					p.setVelocity(p.getVelocity().setY(1));
					break;
				case GOLD_BLOCK:
					fall.put(p, p);
					p.setVelocity(p.getVelocity().setY(1.5));
					break;
				case DIAMOND_BLOCK:
					fall.put(p, p);
					p.setVelocity(p.getVelocity().setY(2));
					break;
				case EMERALD_BLOCK:
					fall.put(p, p);
					p.setVelocity(p.getVelocity().setY(2.5));
					break;
				case BEDROCK:
					fall.put(p, p);
					p.setVelocity(p.getVelocity().setY(
							plugin.getConfig().getDouble("JumpBlock.BEDROCK")));
					break;
				}
			}
		}
	}

	@EventHandler()
	public void onFallDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getCause() == DamageCause.FALL) {
				if (p == fall.get(p)) {
					Location gb = p.getLocation();
					Block loc = gb.add(0.0D, -1.0D, 0.0D).getBlock();
					Block loc_1 = gb.add(0.0D, -1.0D, 0.0D).getBlock();
					Block loc_2 = gb.add(0.0D, -1.0D, 0.0D).getBlock();
					if (!(loc_1.getType() == Material.GLASS
							&& loc_2.getType() == Material.LAPIS_BLOCK && (loc
							.getType() == Material.IRON_BLOCK
							|| loc.getType() == Material.GOLD_BLOCK
							|| loc.getType() == Material.DIAMOND_BLOCK
							|| loc.getType() == Material.EMERALD_BLOCK || loc
								.getType() == Material.BEDROCK))) {
						fall.remove(p);
					}
					e.setCancelled(true);
					// p.sendMessage(plugin.servername +
					// "§6[§4保护系统§6]  §a跳板保护，摔落取消！");
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onCreate(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (e.getBlockPlaced().getType() == Material.GOLD_PLATE) {
			Location gb = e.getBlockPlaced().getLocation();
			Block loc = gb.add(0.0D, -1.0D, 0.0D).getBlock();
			Block loc_1 = gb.add(0.0D, -1.0D, 0.0D).getBlock();
			Block loc_2 = gb.add(0.0D, -1.0D, 0.0D).getBlock();
			if (loc_1.getType() == Material.GLASS
					&& loc_2.getType() == Material.LAPIS_BLOCK
					&& (loc.getType() == Material.IRON_BLOCK
							|| loc.getType() == Material.GOLD_BLOCK
							|| loc.getType() == Material.DIAMOND_BLOCK
							|| loc.getType() == Material.EMERALD_BLOCK || loc
							.getType() == Material.BEDROCK)) {
				if (p.hasPermission("jumpblock.create")) {
					p.sendMessage(plugin.servername + " "
							+ plugin.getmessage("createjumpblock"));
				} else {
					e.setCancelled(true);
					p.sendMessage(plugin.servername + " "
							+ plugin.getmessage("no-permission"));
				}
			}
		}
	}
}
