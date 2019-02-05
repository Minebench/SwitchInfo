package de.themoep.switchinfo;

import de.themoep.serverclusters.bungee.ServerClusters;
import de.themoep.switchinfo.config.LanguageConfig;
import de.themoep.switchinfo.config.PluginConfig;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.util.logging.Level;

/**
 * SwitchInfo
 * Copyright (C) 2015 Max Lee (https://github.com/Phoenix616/)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class SwitchInfo extends Plugin {

    private ServerClusters serverClusters = null;
    private LanguageConfig lang;
    private PluginConfig config;

    public void onEnable() {
        Plugin sc = getProxy().getPluginManager().getPlugin("ServerClusters");
        if(sc == null) {
            getLogger().log(Level.SEVERE, "This plugin won't work without ServerClusters!");
            return;
        } else if(sc instanceof ServerClusters) {
            serverClusters = (ServerClusters) sc;
        }

        try {
            config = new PluginConfig(this, "config.yml");
        } catch (IOException e) {
            getLogger().severe("Unable to load configuration! NeoBans will not be enabled.");
            e.printStackTrace();
            return;
        }

        try {
            lang = new LanguageConfig(this, "lang." + config.getLanguage() + ".yml");
        } catch (IOException e) {
            getLogger().severe("Unable to load language " + config.getLanguage() + " !NeoBans will not be enabled.");
            e.printStackTrace();

            return;
        }

        getProxy().getPluginManager().registerListener(this, new PlayerListener(this));
    }

    public ServerClusters getServerClusters() {
        return serverClusters;
    }

    public LanguageConfig getLang() {
        return lang;
    }
}
