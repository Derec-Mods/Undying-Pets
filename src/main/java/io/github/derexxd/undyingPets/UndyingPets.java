package io.github.derexxd.undyingPets;

import io.github.derexxd.undyingPets.listeners.TotemListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class UndyingPets extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new TotemListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
