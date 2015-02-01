package com.jtb.futurecityprotect;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.jtb.futurecityprotect.Main;

public class Fcp implements CommandExecutor {

	boolean clientcheck;
	Main plugin;

	Fcp(Main instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("prefix")) {
			if (args.length == 2) {
				Player p = Bukkit.getServer().getPlayer(args[0]);
				if (p == null) {
					sender.sendMessage("当前玩家不在线！");
					return true;
				}
				String name = p.getName();
				String ctags = "ctags player " + name + " " + args[1];
				String manuaddv = "manuaddv " + name + " prefix " + args[1];
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
						ctags.replaceAll("&", "§"));
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
						manuaddv.replaceAll("&", "§"));
				sender.sendMessage("§6玩家：§a " + name + " §6的称号已修改为 §r"
						+ args[1].replaceAll("&", "§"));
				return true;
			}
			sender.sendMessage("/prefix 玩家名称 玩家称号        设置玩家列表和聊天称号");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("fcp")) {
			if (args.length == 0) {
				sender.sendMessage("§c========== " + plugin.servername
						+ "§6[§4保护系统§6] ==========");
				sender.sendMessage("§a/fcp kick [踢出类型] [玩家名] 踢出作弊玩家(配合专用服务器)");
				sender.sendMessage("§a/fcp reload 插件配置保存与重载");
				sender.sendMessage("§a/fcp setspawn 设置插件专用传送点");
				return true;
			}
			if (args[0].equalsIgnoreCase("setspawn")) {
				if (sender instanceof Player) {
					Player p = Bukkit.getPlayer(sender.getName());
					Location l = p.getLocation();
					World world = l.getWorld();
					double x = l.getBlockX();
					double y = l.getBlockY();
					double z = l.getBlockZ();
					plugin.getConfig().set("SafeNetherDoor.World", world);
					plugin.getConfig().set("SafeNetherDoor.X", x);
					plugin.getConfig().set("SafeNetherDoor.Y", y);
					plugin.getConfig().set("SafeNetherDoor.Z", z);
					sender.sendMessage(plugin.servername
							+ "§6[§4保护系统§6] §a保护系统专用传送点已设置在 世界：" + world
							+ " X：" + x + " Y：" + y + " Z：" + z + " ！");
					return true;
				} else {
					sender.sendMessage("§c控制台无法使用此命令！");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("reload")) {
				plugin.Reload(sender);
				return true;
			}
			if (args[0].equalsIgnoreCase("save")) {
				plugin.Save(sender);
				return true;
			}
			if (args[0].equalsIgnoreCase("tip")) {
				if (args.length == 2) {
					Player cp = Bukkit.getServer().getPlayer(args[1]);
					if (cp != null) {
						cp.sendMessage(plugin.servername
								+ "§6[§4保护系统§6] §a服务器已开启保护功能！");
						cp.sendMessage(plugin.servername
								+ "§6[§4保护系统§6] §c将会实时监控您的操作！");
						cp.sendMessage(plugin.servername
								+ "§6[§4保护系统§6] §b城市世界请圈地后建筑！");
						cp.sendMessage(plugin.servername
								+ "§6[§4保护系统§6] §e挖矿请带上火把或药水！");
						return true;
					}
					return true;
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("prefix")) {
				if (args.length == 3) {
					Player p = Bukkit.getServer().getPlayer(args[1]);
					if (p == null) {
						sender.sendMessage("当前玩家不在线！");
						return true;
					}
					String name = p.getName();
					String ctags = "ctags player " + name + " " + args[2];
					String manuaddv = "manuaddv " + name + " prefix " + args[2];
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
							ctags.replaceAll("&", "§"));
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
							manuaddv.replaceAll("&", "§"));
					sender.sendMessage("§6玩家：§a " + name + " §6的称号已修改为 §r"
							+ args[2].replaceAll("&", "§"));
					return true;
				}
				sender.sendMessage("/fcp prefix 玩家名称 玩家称号        设置玩家列表和聊天称号");
				return true;
			}

			if (args[0].equalsIgnoreCase("kick")) {
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("on")) {
						sender.sendMessage("§a服务器已开启客户端检测！");
						clientcheck = true;
						plugin.getConfig().set("ClientCheck.Enable",
								clientcheck);
						return true;
					}
					if (args[1].equalsIgnoreCase("off")) {
						sender.sendMessage("§c服务器已关闭客户端检测！");
						clientcheck = false;
						plugin.getConfig().set("ClientCheck.Enable",
								clientcheck);
						return true;
					}
				}
				if (args.length == 3) {
					clientcheck = plugin.getConfig().getBoolean(
							"ClientCheck.Enable");
					if (args[1].equalsIgnoreCase("unsafeclient")) {
						Player cp = Bukkit.getServer().getPlayer(args[2]);
						if (cp != null) {
							if (cp.hasPermission("fcp.allow.unsafeclient")
									|| cp.isOp()) {
								sender.sendMessage("§c玩家具有保护权限无法被踢出！");
								return true;
							}
							if (clientcheck) {
								cp.kickPlayer(plugin.servername
										+ "§6[§4保护系统§6] §c请使用官方客户端，或到群113763169申请登入！");
							} else {
								cp.sendMessage(plugin.servername
										+ "§6[§4保护系统§6] §4您未使用官方客户端，请到群113763169下载！");
							}
							return true;
						}
						sender.sendMessage("§c玩家不在线！");
						return true;
					}
					if (args[1].equalsIgnoreCase("cheatmod")) {
						Player cp = Bukkit.getServer().getPlayer(args[2]);
						if (cp != null) {
							if (cp.hasPermission("fcp.allow.cheat")
									|| cp.isOp()) {
								sender.sendMessage("§c玩家具有保护权限无法被踢出！");
								return true;
							}
							if (clientcheck) {
								cp.kickPlayer(plugin.servername
										+ "§6[§4保护系统§6] §c请勿使用作弊类MOD，若未使用请到群找管理！");
							}
							Bukkit.broadcast(
									plugin.servername
											+ "§6[§4保护系统§6] §4作弊警告：§6玩家：§a"
											+ cp.getName() + "§6使用§c作弊类MOD§6！",
									"fcp.notice");
							return true;
						}
						sender.sendMessage("§c玩家不在线！");
						return true;
					}
					if (args[1].equalsIgnoreCase("clientquit")) {
						Player cp = Bukkit.getServer().getPlayer(args[2]);
						if (cp != null) {
							if (cp.hasPermission("fcp.allow.noclient")
									|| cp.isOp()) {
								sender.sendMessage("§c玩家具有保护权限无法被踢出！");
								return true;
							}
							if (clientcheck) {
								cp.kickPlayer(plugin.servername
										+ "§6[§4保护系统§6] §6与客户端通信丢失！");
							}
							return true;
						}
						sender.sendMessage("§c玩家不在线！");
						return true;
					}
				}
				sender.sendMessage("§c==========" + plugin.servername
						+ "保护系统==========");
				sender.sendMessage("§a/fcp kick On 开启客户端检测");
				sender.sendMessage("§a/fcp kick Off 关闭客户端检测");
				sender.sendMessage("§a/fcp kick UnSafeClient [玩家名]");
				sender.sendMessage("§a/fcp kick CheatMod [玩家名]");
				sender.sendMessage("§a/fcp kick ClientQuit [玩家名]");
				return true;
			}
		}
		return false;
	}
}