package com.cubusfera.core.onlinetime;

import com.cubusfera.core.util.Config;
import org.bukkit.entity.Player;

public class YAMLOnlineTimeData implements OnlineTimeData {
    Config config;


    public YAMLOnlineTimeData(Config config) {
        this.config = config;
        config.saveDefaultConfig();
    }

    public Long getOnlineTime(Player player) {
        return (Long) config.getConfig().getLong("players." + player.getUniqueId().toString());
    }

    public void setOnlineTime(Player player, long time) {
        config.getConfig().set("players." + player.getUniqueId().toString(), time);
        config.save();
    }

    public void addOnlineTime(Player player, long time) {
        Long previous = ((previous = getOnlineTime(player)) != null) ? previous : 0;
        config.getConfig().set("players." + player.getUniqueId().toString(), (previous + time));
        config.save();
    }
}
