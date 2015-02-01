package com.jtb.futurecityprotect;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.jtb.futurecityprotect.Main;

/**
 * 
 * @author Administrator
 */
public class Tpr implements CommandExecutor {

	Main plugin;
	boolean AllowTpr;

	Tpr(Main instance) {
		plugin = instance;
	}


	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("tpr")) {
			if (sender instanceof Player) {
				Player p = Bukkit.getServer().getPlayer(sender.getName());
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("on")) {
						AllowTpr = true;
						plugin.getConfig().set("AllowTpr.Enable", AllowTpr);
						return true;
					}
					if (args[0].equalsIgnoreCase("off")) {
						AllowTpr = false;
						plugin.getConfig().set("AllowTpr.Enable", AllowTpr);
						return true;
					}
				}
				AllowTpr = plugin.getConfig().getBoolean("AllowTpr.Enable");
				if (!AllowTpr) {
					sender.sendMessage("§c管理员未开启随机传送！");
					return true;
				}
				if (!p.hasPermission("tpr.use")) {
					sender.sendMessage("§c当前世界没有随机传送的权限！");
					return true;
				}
				RandomTP(args, p);
				return true;
			}
			sender.sendMessage("§c控制台无法使用此命令！");
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public void RandomTP(String[] limit, Player p) {
		Random rr = new Random();
		int lr;
		if (limit.length != 1) {
			lr = 500;
			p.sendMessage("§6[§b随机传送§6] §5未指定随机范围，使用默认值500!");
			p.sendMessage("§6[§b随机传送§6] §c正确使用方法/tpr [随机传送范围]!");
		} else {
			lr = Integer.parseInt(limit[0]);
			int lrLimit = plugin.getConfig().getInt("RandomTP.Limit");
			if (lr > lrLimit) {
				lr = lrLimit;
				p.sendMessage("§6[§b随机传送§6] §c超过最大允许传送范围，已限制为" + lrLimit + "!");
			}
		}
		Location l = p.getLocation();
		World world = l.getWorld();
		int x = rr.nextInt(lr);
		int z = rr.nextInt(lr);
		int xf = rr.nextInt(x);
		int yf = rr.nextInt(z);
		if (xf % 2 != 0) {
			x = -x;
		}
		if (yf % 2 != 0) {
			z = -z;
		}
		int y = world.getHighestBlockYAt(x, z);
		Location trl = new Location(world, x, y - 1, z);
		int blockid = trl.getBlock().getType().getId();
		for (String protectid : plugin.getConfig().getStringList(
				"RandomTP.ProtectBlock")) {
			if (blockid == Integer.valueOf(protectid)) {
				trl.getBlock().setType(Material.GLASS);
				p.sendMessage("§6[§b随机传送§6] §3传送保护,下方是大海,岩浆或仙人掌,已自动生成玻璃 !");
			}
		}
		Location nrl = new Location(world, x, y + 2, z);
		p.teleport(nrl);
		p.sendMessage("§6[§b随机传送§6] §a您被传送至 §5世界：" + world.getName()
				+ " §d坐标： X:" + x + " Z:" + z + " !");
	}
}
