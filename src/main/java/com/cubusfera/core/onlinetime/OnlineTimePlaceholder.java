package com.cubusfera.core.onlinetime;

import com.cubusfera.core.Core;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class OnlineTimePlaceholder extends EZPlaceholderHook {
    private Core core;

    public OnlineTimePlaceholder(Core core) {
        super(core, "onlinetime");
        this.core = core;
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier){
        if(p == null){
            return "";
        }
        if(identifier.equals("onlinehours")){
            return String.valueOf((int) (core.getOnlineTimeData().getOnlineTime(p) / 3600000));

        }
        return "";
    }

}
