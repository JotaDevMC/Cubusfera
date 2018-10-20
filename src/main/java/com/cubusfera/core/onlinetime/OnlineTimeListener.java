package com.cubusfera.core.onlinetime;

import com.cubusfera.core.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Calendar;
import java.util.HashMap;

public class OnlineTimeListener implements Listener {
    private Core core;
    HashMap<Player, Long> millis = new HashMap<>();

    public OnlineTimeListener(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        millis.put(event.getPlayer(), Calendar.getInstance().getTimeInMillis());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Long join = millis.get(event.getPlayer());
        Long millisOnline = (Calendar.getInstance().getTimeInMillis() - join);

    }

}
