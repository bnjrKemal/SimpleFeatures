package org.bnjrKemal.simpleFeatures.Commands;

import org.bnjrKemal.simpleFeatures.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Trash implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (sender instanceof Player player)
            player.openInventory(Bukkit.createInventory(null, 54, "Trash"));
        else sender.sendMessage(ConfigManager.getString("must-be-player"));
        return true;
    }
}
