package org.bnjrKemal.simpleFeatures;

import org.bnjrKemal.simpleFeatures.Commands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SimpleFeatures extends JavaPlugin {

    @Override
    public void onEnable() {

        ConfigManager.setup(this, "messages.yml");

        Objects.requireNonNull(getCommand("sfreload")).setExecutor(new SFReload());

        PluginCommand gameModeCMD = Objects.requireNonNull(getCommand("gm"));
        Gamemode gamemodeClazz = new Gamemode();
        gameModeCMD.setExecutor(gamemodeClazz);
        gameModeCMD.setTabCompleter(gamemodeClazz);

        // ===== /god =====
        PluginCommand godCMD = Objects.requireNonNull(getCommand("god"));
        God godClazz = new God();
        godCMD.setExecutor(godClazz);
        godCMD.setTabCompleter(godClazz);
        Bukkit.getPluginManager().registerEvents(godClazz, this);


        // ===== /openinv =====
        PluginCommand openINV = Objects.requireNonNull(getCommand("openinv"));
        OpenInventory openInventoryClazz = new OpenInventory();
        openINV.setExecutor(openInventoryClazz);
        openINV.setTabCompleter(openInventoryClazz);

        // ===== /enderchest =====
        PluginCommand enderchestCMD = Objects.requireNonNull(getCommand("enderchest"));
        Enderchest enderchestClazz = new Enderchest();
        enderchestCMD.setExecutor(enderchestClazz);
        enderchestCMD.setTabCompleter(enderchestClazz);

        // ===== /fix =====
        PluginCommand fixCMD = Objects.requireNonNull(getCommand("fix"));
        fixCMD.setExecutor(new Fix());

        // ===== /trash =====
        PluginCommand trashCMD = Objects.requireNonNull(getCommand("trash"));
        trashCMD.setExecutor(new Trash());

        // ===== Teleport commands =====
        PluginCommand tpa = Objects.requireNonNull(getCommand("tpa"));
        PluginCommand tpaccept = Objects.requireNonNull(getCommand("tpaccept"));
        PluginCommand tpdeny = Objects.requireNonNull(getCommand("tpdeny"));

        TeleportCommand teleportHandler = new TeleportCommand(this);
        tpa.setExecutor(teleportHandler);
        tpaccept.setExecutor(teleportHandler);
        tpdeny.setExecutor(teleportHandler);
        tpa.setTabCompleter(teleportHandler);
    }
}
