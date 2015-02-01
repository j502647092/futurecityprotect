package com.jtb.futurecityprotect.Protect;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class Explosion implements Listener {

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        event.blockList().clear();
    }
}
