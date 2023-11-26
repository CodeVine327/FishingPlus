package io.github.codevine327.fishingplus;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class FishingPlus extends JavaPlugin {
    private static FishingPlus instance;
    private final PlayerListener listener = new PlayerListener(this);

    public static FishingPlus getInstance() {
        return instance;
    }

    public PlayerListener getListener() {
        return listener;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getCommand("fishingplus").setExecutor(new CommandImp());
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
