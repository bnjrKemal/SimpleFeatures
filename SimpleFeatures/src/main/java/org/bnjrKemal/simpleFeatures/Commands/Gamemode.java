package org.bnjrKemal.simpleFeatures.Commands;

import org.bnjrKemal.simpleFeatures.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Gamemode implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sender.sendMessage(ConfigManager.getString("gamemode.unknown"));
            return true;
        }

        GameMode gameMode = switch (args[0].toLowerCase()) {
            case "1", "creative" -> GameMode.CREATIVE;
            case "2", "adventure" -> GameMode.ADVENTURE;
            case "3", "spectator" -> GameMode.SPECTATOR;
            default -> GameMode.SURVIVAL;
        };

        Player target;
        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(ConfigManager.getString("must-be-player"));
                return true;
            }
            target = player;
        } else {
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ConfigManager.getString("no-found-player"));
                return true;
            }
        }

        target.setGameMode(gameMode);
        sender.sendMessage(ConfigManager.getString("gamemode.changed-game-mode")
                .replace("{player}", target.getName())
                .replace("{gamemode}", gameMode.name()));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1)
            return List.of("survival", "creative", "adventure", "spectator");
        if (args.length == 2)
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        return List.of();
    }
}
