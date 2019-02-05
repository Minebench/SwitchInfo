package de.themoep.switchinfo.config;

/*
 * By zaiyers
 * https://github.com/zaiyers/Channels/blob/master/src/main/java/net/zaiyers/Channels/config/YamlConfig.java
 */

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class YamlConfig {
    protected final Plugin plugin;
    protected Configuration cfg;
    protected final static ConfigurationProvider ymlCfg = ConfigurationProvider.getProvider(YamlConfiguration.class);

    protected File configFile;

    /**
     * read configuration into memory
     *
     * @param plugin
     * @param configFileName
     * @throws IOException
     */
    public YamlConfig(Plugin plugin, String configFileName) throws IOException {
        this.plugin = plugin;
        configFile = new File(plugin.getDataFolder(), configFileName);

        if (!configFile.exists()) {
            if (!configFile.getParentFile().exists()) {
                configFile.getParentFile().mkdirs();
            }
            configFile.createNewFile();
            cfg = ymlCfg.load(configFile);

            createDefaultConfig();
        } else {
            cfg = ymlCfg.load(configFile);
        }
    }

    protected abstract void createDefaultConfig();

    /**
     * save configuration to disk
     */
    public void save() {
        try {
            ymlCfg.save(cfg, configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Unable to save configuration at " + configFile.getAbsolutePath());
            e.printStackTrace();
        }
    }
    
    /**
     * deletes configuration file
     */
    public void removeConfig() {
        configFile.delete();
    }

    public String getString(String path) {
        return cfg.getString(path);
    }

    public String getString(String path, String def) {
        return cfg.getString(path, def);
    }
}