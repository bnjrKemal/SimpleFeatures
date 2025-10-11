package org.bnjrKemal.simpleFeatures.Commands;

import org.bnjrKemal.simpleFeatures.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class God implements CommandExecutor, TabCompleter, Listener {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player target;
        if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage(ConfigManager.getString("must-be-player"));
            return true;
        }

        if (args.length >= 1) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ConfigManager.getString("no-found-player"));
                return true;
            }
        } else {
            target = (Player) sender;
        }

        target.setInvulnerable(!target.isInvulnerable());
        boolean enabled = target.isInvulnerable();

        if (target.equals(sender)) {
            sender.sendMessage(enabled
                    ? ConfigManager.getString("god.enabled")
                    : ConfigManager.getString("god.disabled"));
        } else {
            sender.sendMessage(enabled
                    ? ConfigManager.getString("god.other.enabled").replace("{player}", target.getName())
                    : ConfigManager.getString("god.other.disabled").replace("{player}", target.getName()));
            target.sendMessage(enabled
                    ? ConfigManager.getString("god.enabled")
                    : ConfigManager.getString("god.disabled"));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1)
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        return List.of();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player victim) {
            if(victim.isInvulnerable())
                event.setCancelled(true);
        }
    }
}
