package com.jtb.futurecityprotect.Anti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.jtb.futurecityprotect.Main;

public class HighRedstone implements Runnable, Listener {

	HashMap<Block, Integer> map = new HashMap<Block, Integer>();
	HashMap<Block, Player> pmap = new HashMap<Block, Player>();
	Main plugin;
	ArrayList<Player> tipop = new ArrayList<Player>();

	public HighRedstone(Main main) {
		plugin = main;
	}

	public void run() {
		List<Block> blocks = new ArrayList<Block>();
		for (Entry<Block, Integer> entry : map.entrySet()) {
			// System.out.println(entry.getValue());
			if (entry.getValue() > plugin.getConfig().getLong(
					"HighRedstone.Maxevents")) {
				blocks.add(entry.getKey());
			}
		}
		Boolean tip = true;
		for (Block block : blocks) {
			World rw = block.getWorld();
			int rx = block.getX();
			int ry = block.getY();
			int rz = block.getZ();
			Player rp = pmap.get(block);
			if (rp != null) {
				if (rp.isOp() || rp.hasPermission("fcp.ignore.highredstone")) {
					if (!tipop.contains(rp)) {
						rp.sendMessage(plugin.servername
								+ "[§4红石检测§6] §c发现您放置高频红石,由于您是管理员,在服务器重启之前此高频将不会被清理,请用完后及时清理！");
						tipop.add(rp);
					}
					continue;
				}
			}
			if (tip) {
				Bukkit.broadcastMessage(plugin.servername
						+ "[§4红石检测§6] §c发现高频红石 §5世界：" + rw.getName()
						+ " §d坐标： X:" + rx + " Y:" + ry + " Z:" + rz
						+ " §a现已清理！");
				if (rp != null) {
					Bukkit.broadcastMessage(plugin.servername
							+ "[§4红石检测§6] §c高频红石数据监测 §5上述高频红石由 §6玩家：§a"
							+ rp.getName() + "§6放置！");
					pmap.remove(block);
				}
				tip = !tip;
			}
			block.breakNaturally();
		}
		map.clear();
	}

	@EventHandler(ignoreCancelled = true)
	public void RedStonePlace(BlockPlaceEvent event) {
		Block rb = event.getBlock();
		Player rp = event.getPlayer();
		if (rb.getType() == Material.REDSTONE_WIRE) {
			pmap.put(rb, rp);
		}
	}

	@EventHandler
	public void onRedClock(BlockRedstoneEvent event) {
		put(event.getBlock());
	}

	private void put(Block block) {
		if (map.containsKey(block)) {
			int i = map.remove(block);
			map.put(block, i + 1);
		} else {
			map.put(block, 1);
		}
	}
}
