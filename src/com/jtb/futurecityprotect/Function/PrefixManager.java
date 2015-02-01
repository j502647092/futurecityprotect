package com.jtb.futurecityprotect.Function;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.jtb.futurecityprotect.Main;

public class PrefixManager implements CommandExecutor {

	Main plugin;
	FileConfiguration pconfig;

	public PrefixManager(Main main) {
		plugin = main;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		ArrayList<String> playerprefixlist;
		if (cmd.getName().equalsIgnoreCase("prefix")) {
			if (args.length <= 0) {
				help(sender);
				return true;
			}
			if (args[0].equalsIgnoreCase("list")) {
				if (args.length == 1 || args.length == 2) {
					String playername;
					if (args.length == 1) {
						playername = sender.getName();
					} else {
						playername = args[1];
					}
					playerprefixlist = getprefix(playername);
					sender.sendMessage("§6玩家：§a" + playername + " §6目前拥有以下称号：");
					sender.sendMessage("§6[§4序号§6]  §6[§b称号§6]");
					for (String prefix : playerprefixlist) {
						int prefixid = playerprefixlist.indexOf(prefix);
						sender.sendMessage("  §6[§4" + prefixid + "§6]   §r"
								+ prefix.replaceAll("&", "§"));
					}
					sender.sendMessage("§6/prefix set §6[§4序号§6] 使用已有称号！");
					return true;
				}
				help(sender);
				return true;
			}
			if (args[0].equalsIgnoreCase("add")) {
				if (!sender.hasPermission("prefix.add")) {
					sender.sendMessage(plugin.servername + " "
							+ plugin.getmessage("no-permission"));
					return true;
				}
				if (args.length == 3) {
					Player p = Bukkit.getPlayer(args[1]);
					if (p != null) {
						playerprefixlist = getprefix(args[1]);
						for (String prefix : playerprefixlist) {
							if (prefix.equalsIgnoreCase(args[2])) {
								sender.sendMessage("§c称号：" + args[2]
										+ " §c已存在！");
								return true;
							}
						}
						playerprefixlist.add(args[2]);
						pconfig.set("prefixs", playerprefixlist);
						plugin.SavePlayerConfig(args[1], pconfig);
						sender.sendMessage("§b玩家： §a" + args[1] + "§b 的称号 §r"
								+ args[2].replaceAll("&", "§") + " §a已添加！");
						p.sendMessage("§b获得新的的称号 §r" + args[2].replaceAll("&", "§") + " §a！");
						p.sendMessage("§6使用：/prefix list 查看已拥有称号！");
						return true;
					} else {
						sender.sendMessage("§c玩家不存在或不在线！");
						return true;
					}
				}
				help(sender);
				return true;
			}
			if (args[0].equalsIgnoreCase("set")) {
				if (args.length == 2) {
					int prefixid = Integer.parseInt(args[1]);
					playerprefixlist = getprefix(sender.getName());
					if (playerprefixlist.size() == 0) {
						sender.sendMessage("§c您沒有可选称号！");
						return true;
					}
					if (prefixid >= 0 && prefixid < playerprefixlist.size()) {
						String prefixstr = playerprefixlist.get(Integer
								.parseInt(args[1]));
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								"fcp prefix " + sender.getName() + " "
										+ prefixstr);
						sender.sendMessage("§6您的称号已设置为 §r"
								+ prefixstr.replaceAll("&", "§"));
						return true;
					} else {
						sender.sendMessage("§c错误的称号序号，请选择[0-"
								+ (playerprefixlist.size() - 1) + "]！");
						return true;
					}
				}
			}
			if (args[0].equalsIgnoreCase("del")) {
				if (!sender.hasPermission("prefix.del")) {
					sender.sendMessage(plugin.servername + " "
							+ plugin.getmessage("no-permission"));
					return true;
				}
				if (args.length == 3) {
					String playername = args[1];
					Player p = Bukkit.getPlayer(playername);
					if (p != null) {
						int prefixid = Integer.parseInt(args[2]);
						playerprefixlist = getprefix(playername);
						if (playerprefixlist.size() == 0) {
							sender.sendMessage("§c该玩家沒有可刪除的称号！");
							return true;
						}
						if (prefixid >= 0 && prefixid < playerprefixlist.size()) {
							String prefixstr = playerprefixlist.get(Integer
									.parseInt(args[2]));
							playerprefixlist.remove(prefixid);
							pconfig.set("prefixs", playerprefixlist);
							plugin.SavePlayerConfig(playername, pconfig);
							sender.sendMessage("§b玩家： §a" + playername
									+ "§b 的称号 §r"
									+ prefixstr.replaceAll("&", "§")
									+ " §c已删除！");
							return true;
						} else {
							sender.sendMessage("§c错误的称号序号，请选择[0-"
									+ (playerprefixlist.size() - 1) + "]！");
							return true;
						}
					} else {
						sender.sendMessage("§c玩家不存在或不在线！");
						return true;
					}
				}
			}
		}
		help(sender);
		return true;
	}

	public ArrayList<String> getprefix(String Player) {
		pconfig = getplayerconfig(Player);
		return (ArrayList<String>) pconfig.getStringList("prefixs");
	}

	public FileConfiguration getplayerconfig(String Player) {
		return plugin.GetPlayerConfig(Player);
	}

	public void help(CommandSender sender) {
		sender.sendMessage("§6===============" + plugin.servername
				+ "§6===================");
		sender.sendMessage("§a/prefix list <玩家名称> 查看已有称号");
		sender.sendMessage("§a/prefix set [称号序号] 使用已有称号");
		sender.sendMessage("§a/prefix add <玩家名称> 添加称号 §c(管理员指令)");
		sender.sendMessage("§a/prefix del <玩家名称> [称号序号] 删除称号 §c(管理员指令)");
	}

}
