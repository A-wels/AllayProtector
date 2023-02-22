package de.awels.mc;

import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class AllayProtector extends JavaPlugin implements Listener {
    private File customConfigFile;
    private FileConfiguration customConfig = new YamlConfiguration();
    private boolean isInvulnerable = true;
    private double followRange = 256;
    private boolean removeWhenFarAway=false;

    @Override
    public void onEnable() {
        // Load config
        createConfig();
        isInvulnerable = getConfig().getBoolean("invulnerable");
        followRange = getConfig().getDouble("followRange");
        removeWhenFarAway = getConfig().getBoolean("removeWhenfarAway");

        // Register events
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("AllayProtector has been enabled!");
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Entity[] entities = event.getChunk().getEntities();
        for (Entity e : entities) {
            if (e.getType().toString() == "ALLAY") {
                // get allay attributes
                Allay allay = (Allay) e;
                // invincible allay
                allay.setInvulnerable(isInvulnerable);
                Objects.requireNonNull(allay.getAttribute(Attribute.GENERIC_FOLLOW_RANGE)).setBaseValue(followRange);
                // do not remove when far away
                allay.setRemoveWhenFarAway(removeWhenFarAway);
            }
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public FileConfiguration getConfig() {
        return this.customConfig;
    }
    private void createConfig() {
        customConfigFile = new File(getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }
}
