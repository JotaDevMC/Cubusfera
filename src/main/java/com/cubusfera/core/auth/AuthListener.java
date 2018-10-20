package com.cubusfera.core.auth;

import com.cubusfera.core.Core;
import com.cubusfera.core.util.centering.CentralizeHelper;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class AuthListener implements Listener {
    Core core;
    ArrayList<Player> notLoggedIn = new ArrayList<Player>();

    public AuthListener(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        notLoggedIn.add(event.getPlayer());
        event.getPlayer().sendMessage("");
        CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.BLUE + "¡Bienvenido a " + ChatColor.BOLD + "Cubusfera" + ChatColor.RESET + ChatColor.BLUE + "!");
        event.getPlayer().sendMessage("");
        if (core.getAuthmeApi().isRegistered(event.getPlayer().getName())) {
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.YELLOW + "Inicia sesión usando:");
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.YELLOW + "/login <contraseña>");
        } else {
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.YELLOW + "Regístrate usando:");
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.YELLOW + "/register <contraseña> <contraseña>");
        }
        event.getPlayer().sendMessage("");
        CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.BLUE + "" + ChatColor.BOLD + "play.cubusfera.com " + ChatColor.GRAY + "-" + ChatColor.BLUE + "" + ChatColor.BOLD + "discord.cubusfera.com");
        event.getPlayer().sendMessage("");
    }

    @EventHandler
    public void onPlayerLogin(LoginEvent event) {
        if (notLoggedIn.contains(event.getPlayer())) notLoggedIn.remove(event.getPlayer());
        if (core.getPermsApi().getUser(event.getPlayer().getUniqueId()).getPrimaryGroup() == "default") {
            event.getPlayer().sendMessage("");
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.BLUE + "¡Bienvenido a " + ChatColor.BOLD + "Cubusfera" + ChatColor.RESET + ChatColor.BLUE + "!");
            event.getPlayer().sendMessage("");
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.YELLOW + "Aún no has sido verificado por un administrador. Para ello,");
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.YELLOW + "asegúrate de haber hablado con un administrador por Discord.");
            event.getPlayer().sendMessage("");
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.BLUE + "" + ChatColor.BOLD + " discord.cubusfera.com");
            event.getPlayer().sendMessage("");
        }
        Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (notLoggedIn.contains(event.getPlayer())) notLoggedIn.remove(event.getPlayer());
    }
}
