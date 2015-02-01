package com.jtb.futurecityprotect;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
/**
 *
 * @author Administrator
 */
public class Spk implements CommandExecutor {

    Main plugin;
    boolean speaker;
    String prefix;
    int time;
    int i;

    Spk(Main instance) {
        plugin=instance;
    }


    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        speaker = plugin.getConfig().getBoolean("Speaker.Enable", true);
        prefix = plugin.getConfig().getString("Speaker.Prefix").replaceAll("&", "§");
        time = plugin.getConfig().getInt("Speaker.Time");
        if (!speaker) {
            cs.sendMessage(prefix + " §c管理员未开启此功能！");
            return true;
        }
        if (cs instanceof Player) {
        	if (!cs.hasPermission("spk.use")){
        		cs.sendMessage(prefix + plugin.getmessage("no-permission"));
        		return true;
        	}
            if (strings.length == 0) {
                cs.sendMessage(prefix + " §c请输入喇叭内容！");
                return true;
            }
            Player p = Bukkit.getPlayer(cs.getName());
            String Message = prefix + p.getName() + "§r";
            for (i = 0; i < strings.length; i++) {
                Message = Message +" " + strings[i];
            }
            Message = Message.replaceAll("&", "§");
            //BarAPI.setMessage(p, Message, time);
            BarAPI.setMessage(Message, time);
            return true;
        }
        cs.sendMessage(prefix + "§c控制台无法使用此命令！");
        return true;
    }
}
