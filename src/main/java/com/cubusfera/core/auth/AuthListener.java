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
import java.util.concurrent.TimeUnit;

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
        CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.BLUE + "" + ChatColor.BOLD + "play.cubusfera.com " + ChatColor.GRAY + "-" + ChatColor.BLUE + "" + ChatColor.BOLD + " discord.cubusfera.com");
        event.getPlayer().sendMessage("");
    }

    @EventHandler
    public void onPlayerLogin(LoginEvent event) {
        if (notLoggedIn.contains(event.getPlayer())) notLoggedIn.remove(event.getPlayer());
        Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + event.getPlayer().getName());
        if (core.getPermsApi().getUser(event.getPlayer().getUniqueId()).getPrimaryGroup().equalsIgnoreCase("default")) {
            event.getPlayer().sendMessage("");
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.BLUE + "¡Bienvenido a " + ChatColor.BOLD + "Cubusfera" + ChatColor.RESET + ChatColor.BLUE + "!");
            event.getPlayer().sendMessage("");
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.YELLOW + "Aún no has sido verificado por un administrador.");
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.YELLOW + "Para ser verificado, ponte en contacto con un");
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.YELLOW + "admin a través de Discord si no lo has hecho ya.");
            event.getPlayer().sendMessage("");
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.BLUE + "" + ChatColor.BOLD + " discord.cubusfera.com");
            event.getPlayer().sendMessage("");
        }else{
            event.getPlayer().sendMessage("");
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.BLUE + "¡Bienvenido a " + ChatColor.BOLD + "Cubusfera" + ChatColor.RESET + ChatColor.BLUE + "!");
            event.getPlayer().sendMessage("");
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.AQUA + "Tiempo online:");
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), getOnlineDuration(core.getOnlineTimeData().getOnlineTime(event.getPlayer())));
            event.getPlayer().sendMessage("");
            CentralizeHelper.sendCenteredMessage(event.getPlayer(), ChatColor.BLUE + "" + ChatColor.BOLD + "play.cubusfera.com " + ChatColor.GRAY + "-" + ChatColor.BLUE + "" + ChatColor.BOLD + " discord.cubusfera.com");
            event.getPlayer().sendMessage("");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (notLoggedIn.contains(event.getPlayer())) notLoggedIn.remove(event.getPlayer());
    }

    public static String getOnlineDuration(long l1) {
        if (l1 < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(l1);
        l1 -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(l1);
        l1 -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(l1);
        l1 -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(l1);

        StringBuilder sb = new StringBuilder(64);
        if (days > 0) {
            sb.append(ChatColor.GREEN + "" + days);
            sb.append(ChatColor.YELLOW + " dia(s) ");
        }
        if (hours > 0) {
            sb.append(ChatColor.GREEN + "" + hours);
            sb.append(ChatColor.YELLOW + " hora(s) ");
        }
        if (minutes > 0) {
            sb.append(ChatColor.GREEN + "" + minutes);
            sb.append(ChatColor.YELLOW + " minuto(s) ");
        }
        if (sb.toString().equalsIgnoreCase("")) {
            sb.append(ChatColor.GREEN + "" + seconds);
            sb.append(ChatColor.YELLOW + " segundo(s) ");
        }
        return (sb.toString().replaceFirst("\\s++$", ""));
    }
}
