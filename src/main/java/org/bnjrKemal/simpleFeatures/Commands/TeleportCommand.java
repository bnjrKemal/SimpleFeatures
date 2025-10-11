package org.bnjrKemal.simpleFeatures.Commands;

import org.bnjrKemal.simpleFeatures.ConfigManager;
import org.bnjrKemal.simpleFeatures.SimpleFeatures;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TeleportCommand implements CommandExecutor, TabCompleter {

    private final SimpleFeatures plugin;

    private static class PendingData {
        final UUID requesterUUID;
        final long expirationTime;

        PendingData(UUID requester, long expiresInMs) {
            this.requesterUUID = requester;
            this.expirationTime = System.currentTimeMillis() + expiresInMs;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }

    private final Map<UUID, PendingData> pendingRequests = new HashMap<>();

    public TeleportCommand(SimpleFeatures plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ConfigManager.getString("must-be-player"));
            return true;
        }

        switch (command.getName().toLowerCase()) {

            case "tpa" -> {
                if (args.length != 1) {
                    player.sendMessage(ConfigManager.getString("tpa.usage"));
                    return true;
                }

                Player target = Bukkit.getPlayer(args[0]);
                if (target == null || !target.isOnline()) {
                    player.sendMessage(ConfigManager.getString("no-found-player"));
                    return true;
                }

                if (target.equals(player)) {
                    player.sendMessage(ConfigManager.getString("tpa.self-request"));
                    return true;
                }

                PendingData existing = pendingRequests.get(target.getUniqueId());
                if (existing != null) {
                    UUID existingRequester = existing.requesterUUID;
                    Player existingPlayer = Bukkit.getPlayer(existingRequester);
                    String existingName = (existingPlayer != null) ? existingPlayer.getName() : "Unknown";
                    player.sendMessage(ConfigManager.getString("tpa.already-pending")
                            .replace("{target}", target.getName())
                            .replace("{player}", existingName));
                    return true;
                }

                long expirationMs = 30 * 1000L;
                pendingRequests.put(target.getUniqueId(), new PendingData(player.getUniqueId(), expirationMs));

                target.sendMessage(ConfigManager.getString("tpa.received")
                        .replace("{player}", player.getName()));
                player.sendMessage(ConfigManager.getString("tpa.sent")
                        .replace("{target}", target.getName()));

                // Expiration task
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    PendingData current = pendingRequests.get(target.getUniqueId());
                    if (current != null && current.requesterUUID.equals(player.getUniqueId()) && current.isExpired()) {
                        pendingRequests.remove(target.getUniqueId());
                        if (target.isOnline())
                            target.sendMessage(ConfigManager.getString("tpa.expired")
                                    .replace("{player}", player.getName()));
                        if (player.isOnline())
                            player.sendMessage(ConfigManager.getString("tpa.expired")
                                    .replace("{target}", target.getName()));
                    }
                }, 20L * 30);
                return true;
            }

            case "tpaccept" -> {
                PendingData data = pendingRequests.get(player.getUniqueId());
                if (data == null) {
                    player.sendMessage(ConfigManager.getString("tpa.no-request"));
                    return true;
                }

                if (data.isExpired()) {
                    player.sendMessage(ConfigManager.getString("tpa.expired"));
                    pendingRequests.remove(player.getUniqueId());
                    return true;
                }

                Player requester = Bukkit.getPlayer(data.requesterUUID);
                if (requester == null || !requester.isOnline()) {
                    player.sendMessage(ConfigManager.getString("tpa.requester-offline"));
                    pendingRequests.remove(player.getUniqueId());
                    return true;
                }

                requester.teleport(player.getLocation());
                requester.sendMessage(ConfigManager.getString("tpa.accepted")
                        .replace("{player}", player.getName()));
                player.sendMessage(ConfigManager.getString("tpa.accepted-self")
                        .replace("{player}", requester.getName()));

                pendingRequests.remove(player.getUniqueId());
                return true;
            }

            case "tpdeny" -> {
                PendingData data = pendingRequests.get(player.getUniqueId());
                if (data == null) {
                    player.sendMessage(ConfigManager.getString("tpa.no-request"));
                    return true;
                }

                Player requester = Bukkit.getPlayer(data.requesterUUID);
                if (requester != null && requester.isOnline()) {
                    requester.sendMessage(ConfigManager.getString("tpa.denied")
                            .replace("{player}", player.getName()));
                }

                player.sendMessage(ConfigManager.getString("tpa.denied-self")
                        .replace("{target}", requester != null ? requester.getName() : "Unknown"));

                pendingRequests.remove(player.getUniqueId());
                return true;
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1 && (command.getName().equalsIgnoreCase("tpa"))) {
            return Bukkit.getOnlinePlayers().stream()
                    .filter(p -> !p.equals(sender) && p.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    .map(Player::getName)
                    .toList();
        }
        return List.of();
    }
}
