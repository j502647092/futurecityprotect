package com.jtb.futurecityprotect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.jtb.futurecityprotect.Anti.BreakFarm;
import com.jtb.futurecityprotect.Anti.HighRedstone;
import com.jtb.futurecityprotect.Anti.Nightvision;
import com.jtb.futurecityprotect.Anti.Spam;
import com.jtb.futurecityprotect.Function.JumpBlock;
import com.jtb.futurecityprotect.Function.PrefixManager;
import com.jtb.futurecityprotect.Notice.Multikills;
import com.jtb.futurecityprotect.Notice.Tip;
import com.jtb.futurecityprotect.Protect.Build;
import com.jtb.futurecityprotect.Protect.ClientCheck;
import com.jtb.futurecityprotect.Protect.Explosion;
import com.jtb.futurecityprotect.Protect.NetherDoor;
import com.jtb.futurecityprotect.Protect.ResFly;

public class Main extends JavaPlugin{
	public File itemfile;
	public FileConfiguration itemConfig;
	public File msgfile;
	public FileConfiguration msgConfig;
	public String servername;
	public boolean BossBar;
	public static Economy economy = null;

	public void onLoad() {
		saveDefaultConfig();
		itemfile = new File(this.getDataFolder(), "item.yml");
		itemConfig = Loadcfg(itemfile);
		msgfile = new File(this.getDataFolder(), "message.yml");
		msgConfig = Loadcfg(msgfile);
		servername = getmessage("servername");
	}

	public void onEnable() {
		getLogger().info("==========" + servername + "保护插件==========");
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
			getLogger().info("创建" + servername + "插件文件夹!");
		}

		PluginManager pm = Bukkit.getPluginManager();
		Plugin p = pm.getPlugin("BarAPI");
		if (p != null && p.isEnabled()) {
			BossBar = true;
			getLogger().info("发现BarAPI启用功能支持!");
		} else {
			BossBar = false;
		}
		if (!setupEconomy()){
			getLogger().info("未发现Vault经济插件，可能无法使用部分功能!");
		} else {
			getLogger().info("已发现Vault经济插件，启用经济功能部分支持!");
		}
		boolean ClientCheck = getConfig()
				.getBoolean("ClientCheck.Enable", true);
		if (ClientCheck) {
			Bukkit.getPluginManager().registerEvents(new ClientCheck(this), this);
			getLogger().info("客户端检测模块已加载!");
		}
		getCommand("fcp").setExecutor(new Fcp(this));
		boolean AllowTpr = getConfig().getBoolean("AllowTpr.Enable", true);
		if (AllowTpr) {
			getLogger().info("玩家随机传送模块已加载!");
		}
		getCommand("tpr").setExecutor(new Tpr(this));
		boolean Speaker = getConfig().getBoolean("Speaker.Enable", true);
		if (Speaker && BossBar) {
			getLogger().info("BOSS血条公告模块已加载!");
			getCommand("spk").setExecutor(new Spk(this));
		} else {
			getLogger().info("未找到BarAPI插件停止加载BOSS血条公告!");
		}
		if (getConfig().getBoolean("Tip.Enable", true)) {
			Bukkit.getPluginManager().registerEvents(new Tip(this), this);
			getLogger().info("保护插件提醒功能已加载!");
		}
		if (getConfig().getBoolean("CityBuild.Enable", true)) {
			Bukkit.getPluginManager().registerEvents(new Build(this), this);
			getLogger().info("城市世界建筑保护已加载!");
		}
		if (getConfig().getBoolean("ResFly.Enable", true)) {
			if (p != null && p.isEnabled()) {
				FlagPermissions.addFlag("fly");
				FlagPermissions.addResidenceOnlyFlag("fly");
				Bukkit.getPluginManager()
						.registerEvents(new ResFly(this), this);
				getLogger().info("玩家领地飞行控制已加载!");
			} else {
				getLogger().info("未找到领地插件停止加载领地飞行!");
			}
		}
		if (getConfig().getBoolean("Multikills.Enable", true)) {
			Bukkit.getPluginManager()
					.registerEvents(new Multikills(this), this);
			getLogger().info("玩家连杀死亡提示已加载!");
		}
		if (getConfig().getBoolean("PrefixManager.Enable", true)) {
			getCommand("prefix").setExecutor(new PrefixManager(this));
			getLogger().info("玩家称号管理系统已加载!");
		}
		if (getConfig().getBoolean("JumpBlock.Enable", true)) {
			Bukkit.getPluginManager().registerEvents(new JumpBlock(this), this);
			getLogger().info("游戏超级跳床方块已加载!");
			getCommand("jumpblock").setExecutor(new JumpBlock(this));
		}
		if (getConfig().getBoolean("SafeNetherDoor.Enable", true)) {
			Bukkit.getPluginManager()
					.registerEvents(new NetherDoor(this), this);
			getLogger().info("防止登录卡地狱门已加载!");
		}
		if (getConfig().getBoolean("Spam.Enable", true)) {
			Spam spam = new Spam(this);
			Bukkit.getPluginManager().registerEvents(spam, this);
			Bukkit.getScheduler().runTaskTimer(this, spam, 50, 50);
			getCommand("spam").setExecutor(spam);
			getLogger().info("防止玩家聊天刷屏已加载!");
		}
		if (getConfig().getBoolean("BreakFarm.Enable", true)) {
			Bukkit.getPluginManager().registerEvents(new BreakFarm(this), this);
			getLogger().info("防止玩家踩坏耕地已加载!");
		}
		if (getConfig().getBoolean("Explosion.Enable", true)) {
			Bukkit.getPluginManager().registerEvents(new Explosion(), this);
			getLogger().info("防止爆炸破坏地形已加载!");
		}
		if (getConfig().getBoolean("Nightvision.Enable", true)) {
			Bukkit.getPluginManager().registerEvents(new Nightvision(this),
					this);
			getLogger().info("防止无限夜视作弊已加载!");
		}
		if (getConfig().getBoolean("HighRedstone.Enable", true)) {
			HighRedstone redstone = new HighRedstone(this);
			Bukkit.getPluginManager().registerEvents(redstone, this);
			Bukkit.getScheduler().runTaskTimer(this, redstone, 20, 20);
			getLogger().info("防止玩家高频红石已加载!");
		}
		/*
		 * if (getConfig().getBoolean("PortMapping.Enable", true)) { Upnp
		 * upnp=new Upnp(); upnp.forwardPort(this,Bukkit.getPort());
		 * getLogger().info("路由端口自动映射已加载!"); }
		 */
		getLogger().info("==========" + servername + "保护插件==========");
	}

	public void onDisable() {
		getLogger().info(servername + "保护插件数据保存中。。。");
		Save(Bukkit.getConsoleSender());
		getLogger().info(servername + "保护插件已卸载!");
	}

	public void Save(CommandSender sender) {
		saveConfig();
		sender.sendMessage(Savecfg(itemConfig, itemfile));
		sender.sendMessage(Savecfg(msgConfig, msgfile));
	}

	public void Reload(CommandSender sender) throws FileNotFoundException, IOException, InvalidConfigurationException {
		saveDefaultConfig();
		reloadConfig();
		LoadConfig(itemConfig,itemfile);
		LoadConfig(msgConfig,msgfile);
		//itemConfig = Loadcfg(itemfile);
		//msgConfig = Loadcfg(msgfile);
		servername = getmessage("servername");
		sender.sendMessage(servername + "§6[§4保护系统§6] §a配置文件已重新载入！");
	}

	public FileConfiguration Loadcfg(File cfgfile) {
		if (!cfgfile.exists()) {
			saveResource(cfgfile.getName(), false);
			
			return YamlConfiguration.loadConfiguration(cfgfile);
		} else {
			return YamlConfiguration.loadConfiguration(cfgfile);
		}
	}

	public void LoadConfig(FileConfiguration cfg ,File cfgfile) throws FileNotFoundException, IOException, InvalidConfigurationException {
		if (!cfgfile.exists()) {
			saveResource(cfgfile.getName(), false);
			cfg.load(cfgfile);
		} else {
			cfg.load(cfgfile);
		}
	}
	
	public String Savecfg(FileConfiguration cfg, File cfgfile) {
		try {
			// cfg.saveToString();
			cfg.save(cfgfile);
		} catch (IOException e) {
		}
		return getmessage("saveconfig").replaceAll("%filename%",
				cfgfile.getName());
	}

	public FileConfiguration GetPlayerConfig(String Player) {
		File playerfile = new File(this.getDataFolder() + "/player", Player
				+ ".yml");
		if (!playerfile.getParentFile().exists()) {
			playerfile.getParentFile().mkdirs();
		}
		if (!playerfile.exists()) {
			try {
				playerfile.createNewFile();
			} catch (IOException e) {
			}
			return YamlConfiguration.loadConfiguration(playerfile);
		} else {
			return YamlConfiguration.loadConfiguration(playerfile);
		}
	}

	public void SavePlayerConfig(String Player, FileConfiguration pcfg) {
		File playerfile = new File(this.getDataFolder() + "/player", Player
				+ ".yml");
		if (!playerfile.getParentFile().exists()) {
			playerfile.getParentFile().mkdirs();
		}
		if (!playerfile.exists()) {
			try {
				playerfile.createNewFile();
			} catch (IOException e) {
			}
			return;
		} else {
			try {
				pcfg.save(playerfile);
			} catch (IOException e) {
			}
			return;
		}
	}

	public String getmessage(String path) {
		return msgConfig.getString(path).replaceAll("&", "§");
	}

	public boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
		return (economy != null);
	}

}





