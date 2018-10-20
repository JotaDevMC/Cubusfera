package com.cubusfera.core.onlinetime;

import org.bukkit.entity.Player;

public interface OnlineTimeData {

    Long getOnlineTime(Player player);

    void setOnlineTime(Player player, long time);

    void addOnlineTime(Player player, long time);
}
