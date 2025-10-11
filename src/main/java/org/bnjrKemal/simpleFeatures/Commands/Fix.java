package org.bnjrKemal.simpleFeatures.Commands;

import org.bnjrKemal.simpleFeatures.ConfigManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Fix implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ConfigManager.getString("must-be-player"));
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR || item.getDurability() == 0) {
            player.sendMessage(ConfigManager.getString("fix.no-item"));
            return true;
        }

        item.setDurability((short) 0);
        player.sendMessage(ConfigManager.getString("fix.repaired"));
        return true;
    }
}
