package com.cubusfera.core;

import com.cubusfera.core.auth.AuthListener;
import com.cubusfera.core.onlinetime.OnlineTimeData;
import com.cubusfera.core.onlinetime.OnlineTimeListener;
import com.cubusfera.core.onlinetime.YAMLOnlineTimeData;
import com.cubusfera.core.util.Config;
import fr.xephi.authme.api.v3.AuthMeApi;
import me.lucko.luckperms.api.LuckPermsApi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {
    private boolean placeholderapi = false;

    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) placeholderapi = true;
        /*
         * Load all modules
         */
        setupOnlineModule();
        setupPermissionModule();
        setupAuthModule();
        System.out.println("Plugin enabled.");
    }

    public void onDisable() {

    }

    /**
     * Online Time Module
     */
    private static OnlineTimeData onlineTimeData;

    public void setupOnlineModule() {
        onlineTimeData = new YAMLOnlineTimeData(new Config("data/onlinetime", this));
        this.getServer().getPluginManager().registerEvents(new OnlineTimeListener(this), this);
        //TODO: Register placeholder api integration
        System.out.println("Successfully loaded online time module;");
    }

    public static OnlineTimeData getOnlineTimeData() {
        return onlineTimeData;
    }

    /**
     * Permission module
     */
    private static LuckPermsApi permsApi;

    public void setupPermissionModule() {
        RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
        if (provider != null) {
            permsApi = provider.getProvider();
            System.out.println("Succesfully hooked into LuckPerms.");
        }
    }

    public static LuckPermsApi getPermsApi() {
        return permsApi;
    }

    /**
     * Auth module
     */
    private static AuthMeApi authmeApi;

    public void setupAuthModule() {
        if (!getServer().getPluginManager().isPluginEnabled("AuthMe")) {
            System.out.println("Cannot enable auth module. AuthMe plugin not detected.");
            return;
        }
        if (permsApi == null) {
            System.out.println("Cannot enable auth module. Cannot hook with any permission plugin.");
            return;
        }
        authmeApi = AuthMeApi.getInstance();
        getServer().getPluginManager().registerEvents(new AuthListener(this), this);
        System.out.println("Succesfully loaded auth module.");
    }

    public static AuthMeApi getAuthmeApi() {
        return authmeApi;
    }
}
