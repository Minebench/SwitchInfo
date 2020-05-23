package de.themoep.switchinfo;

import com.google.common.collect.ImmutableMap;
import de.themoep.serverclusters.bungee.Cluster;
import de.themoep.serverclusters.bungee.events.ClusterSwitchEvent;
import de.themoep.serverclusters.bungee.events.NetworkConnectEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.concurrent.TimeUnit;

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
public class PlayerListener implements Listener {
    SwitchInfo plugin;

    public PlayerListener(SwitchInfo plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerJoin(NetworkConnectEvent event) {
        BaseComponent[] joinMessage = TextComponent.fromLegacyText(
                plugin.getLang().getTranslation(
                        "actionbar.connect",
                        ImmutableMap.of(
                                "player",
                                event.getPlayer().getDisplayName()
                        )
                )
        );

        for(ProxiedPlayer player : plugin.getProxy().getPlayers()) {
            if(!player.hasPermission("switchinfo.alert.connect"))
                continue;
            if(event.getPlayer().hasPermission("vanish.see") && !player.hasPermission("vanish.see"))
                continue;
            if(event.getTarget().isHidden() && !player.hasPermission("serverclusters.seehidden") && !event.getTarget().getPlayerlist().contains(player))
                continue;
            if(!player.hasPermission("serverclusters.cluster." + event.getTarget().getName()) && !event.getTarget().getPlayerlist().contains(player))
                continue;
            player.sendMessage(ChatMessageType.ACTION_BAR, joinMessage);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        Cluster cluster = null;
        if(event.getPlayer().getServer() != null) {
            cluster = plugin.getServerClusters().getClusterManager().getClusterByServer(event.getPlayer().getServer().getInfo().getName());
        }
        BaseComponent[] quitMessage =  TextComponent.fromLegacyText(
                plugin.getLang().getTranslation(
                        "actionbar.quit",
                        ImmutableMap.of(
                                "player",
                                event.getPlayer().getDisplayName()
                        )
                )
        );

        for(ProxiedPlayer player : plugin.getProxy().getPlayers()) {
            if(!player.hasPermission("switchinfo.alert.quit"))
                continue;
            if(event.getPlayer().hasPermission("vanish.see") && !player.hasPermission("vanish.see"))
                continue;
            if(cluster != null) {
                if(cluster.isHidden() && !player.hasPermission("serverclusters.seehidden") && !cluster.getPlayerlist().contains(player))
                    continue;
                if(!player.hasPermission("serverclusters.cluster." + cluster.getName()) && !cluster.getPlayerlist().contains(player))
                    continue;
            }
            player.sendMessage(ChatMessageType.ACTION_BAR, quitMessage);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerQuit(ClusterSwitchEvent event) {
        if (event.isCancelled()) {
            return;
        }
        BaseComponent[] leaveMessage = TextComponent.fromLegacyText(
                plugin.getLang().getTranslation(
                        "actionbar.switch.leave",
                        ImmutableMap.of(
                                "player",
                                event.getPlayer().getDisplayName()
                        )
                )
        );
        BaseComponent[] joinMessage = TextComponent.fromLegacyText(
                plugin.getLang().getTranslation(
                        "actionbar.switch.join",
                        ImmutableMap.of(
                                "player",
                                event.getPlayer().getDisplayName()
                        )
                )
        );

        plugin.getProxy().getScheduler().schedule(plugin, () -> {
            for(ProxiedPlayer player : plugin.getProxy().getPlayers()) {
                if(player.equals(event.getPlayer()) || !player.hasPermission("switchinfo.alert.switch"))
                    continue;
                boolean onFrom = event.getFrom().getPlayerlist().contains(player);
                boolean onTo = event.getTo().getPlayerlist().contains(player);
                if(!onFrom && !onTo)
                    continue;
                if(event.getPlayer().hasPermission("vanish.see") && !player.hasPermission("vanish.see"))
                    continue;

                if(onFrom)
                    player.sendMessage(ChatMessageType.ACTION_BAR, leaveMessage);
                else if(onTo)
                    player.sendMessage(ChatMessageType.ACTION_BAR, joinMessage);
            }
        }, 2, TimeUnit.SECONDS);
    }
}
