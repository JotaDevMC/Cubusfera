package com.cubusfera.core.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.cubusfera.core.Core;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.manager.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

@CommandAlias("staff|s")
@CommandPermission("cubusfera.staff")
public class StaffUserCommand extends BaseCommand {

    @Subcommand("accept")
    @CommandPermission("cubusfera.staff.accept")
    @CommandCompletion("@players")
    @Syntax("<player> - Player to accept")
    @Description("Accept a player and allow him to play without restrictions on the server.")
    public void onAcceptPlayer(CommandSender sender, String name) {
        if (sender instanceof Player && ((Player) sender).getName().equalsIgnoreCase(name)) {
            sender.sendMessage(ChatColor.RED + "No puedes aceptarte a ti mismo!");
            return;
        }
        if ((Bukkit.getOfflinePlayer(name).getUniqueId() == null) || (!Bukkit.getOfflinePlayer(name).hasPlayedBefore())) {
            sender.sendMessage(ChatColor.RED + "El jugador debe estar registrado.");
            return;
        }
        UserManager userManager = Core.getPermsApi().getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(Bukkit.getOfflinePlayer(name).getUniqueId());
        userFuture.thenAcceptAsync(user -> {
            if (user == null) {
                sender.sendMessage(ChatColor.RED + "El jugador debe estar registrado.");
                return;
            }
            if (user.getPrimaryGroup().equalsIgnoreCase("default")) {
                addUserNode(user, userManager).thenRunAsync(() -> setUserPrimaryGroup(user, userManager).thenRunAsync(()
                        -> removeDefaultNode(user, userManager).thenRunAsync(() -> {
                    sender.sendMessage(ChatColor.GREEN + "Has aceptado a " + ChatColor.BLUE + name + ChatColor.GREEN + "!");
                })));
                //TODO; Rewrite this code to make it simpler, easier to read, easier to maintain and less redundant
            } else {
                sender.sendMessage(ChatColor.RED + "No se puede aceptar a " + ChatColor.DARK_RED + name
                        + ChatColor.RED + " por que su rango es " + ChatColor.DARK_RED +
                        Core.getPermsApi().getUser(name).getPrimaryGroup().toUpperCase() + ChatColor.RED + "!");
            }
            return;
        });
    }

    public CompletableFuture<Void> addUserNode(User user, UserManager userManager) {
        user.setPermission(Core.getPermsApi().getNodeFactory().makeGroupNode("miembro").build());
        return userManager.saveUser(user);
    }

    public CompletableFuture<Void> setUserPrimaryGroup(User user, UserManager userManager) {
        user.setPrimaryGroup("miembro");
        return userManager.saveUser(user);
    }

    public CompletableFuture<Void> removeDefaultNode(User user, UserManager userManager) {
        user.setPermission(Core.getPermsApi().getNodeFactory().makeGroupNode("default").setValue(false).build());
        return userManager.saveUser(user);
    }

    @HelpCommand
    public void help(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
