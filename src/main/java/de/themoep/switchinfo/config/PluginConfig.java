package de.themoep.switchinfo.config;

import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Phoenix616 on 09.02.2015.
 */
public class PluginConfig extends YamlConfig {
    private HashMap<String, List<String>>[] commandMap;

    /**
     * Load configuration from disk
     * @param plugin The plugin this config is for
     * @param configFileName Name of the configuration file
     * @throws IOException
     */
    public PluginConfig(Plugin plugin, String configFileName) throws IOException {
        super(plugin, configFileName);
    }

    @Override
    public void createDefaultConfig() {
        cfg = ymlCfg.load(new InputStreamReader(plugin.getResourceAsStream("config.yml")));

        save();
    }

    public String getLanguage() {
        return cfg.getString("language");
    }

}
