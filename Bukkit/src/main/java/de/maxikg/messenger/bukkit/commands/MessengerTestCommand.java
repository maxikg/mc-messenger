/*
 * MC Messenger - de.maxikg:messenger adapter for Minecraft related software
 *     Copyright (C) 2016  Max Walsch (github.com/maxikg)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.maxikg.messenger.bukkit.commands;

import de.maxikg.messenger.bukkit.service.MessengerService;
import de.maxikg.messenger.gson.AbstractObjectMessageListener;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

@RequiredArgsConstructor
public class MessengerTestCommand implements CommandExecutor {

    @NonNull
    private final Server server;

    @NonNull
    private final RegisteredServiceProvider<MessengerService> service;

    public MessengerTestCommand init() {
        service.getProvider().getServerPlayerMessenger().register(new AbstractObjectMessageListener<DebugMessage>(DebugMessage.class) {
            @Override
            public void onMessage(String namespace, String target, DebugMessage message) {
                Player player = server.getPlayer(message.getTarget());
                if (player != null)
                    player.sendMessage(ChatColor.GREEN + "Debug message received.");
            }
        });

        return this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        MessengerService messenger = service.getProvider();
        if (messenger == null) {
            sender.sendMessage(ChatColor.RED + "Messenger not ready. Maybe the server_player feature is disabled or the startup failed.");
            return true;
        }

        OfflinePlayer player = (OfflinePlayer) sender;
        messenger.getServerPlayerMessenger().submit(player, new DebugMessage(player.getUniqueId()));
        sender.sendMessage(ChatColor.GREEN + "Message sent.");

        return true;
    }

    @RequiredArgsConstructor
    @Data
    public static class DebugMessage {

        @NonNull
        private final UUID target;
    }
}
