package de.themoep.switchinfo.config;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * By zaiyers
 * https://github.com/zaiyers/Channels/blob/master/src/main/java/net/zaiyers/Channels/config/LanguageConfig.java
 */
public class LanguageConfig extends YamlConfig {
    public LanguageConfig(Plugin plugin, String configFileName) throws IOException {
        super(plugin, configFileName);
    }

    public void createDefaultConfig() {
        // default is english
        cfg = ymlCfg.load(
                new InputStreamReader(plugin.getResourceAsStream(configFile.getName()))
        );

        save();
    }
    
    public String getTranslation(String key) {
        if (cfg.getString(key, "").isEmpty()) {
            return ChatColor.RED + "Unknown language key: " + ChatColor.YELLOW + key;
        } else {
            return ChatColor.translateAlternateColorCodes('&', cfg.getString(key));
        }
    }
    
    public String getTranslation(String key, Map<String, String> replacements) {
        String string = getTranslation(key);

        // insert replacements
        if (replacements != null)
            for (String variable: replacements.keySet())
                string = string.replaceAll("%"+variable+"%", replacements.get(variable));
        return string;
    }

}