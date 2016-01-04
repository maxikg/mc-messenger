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

package de.maxikg.messenger.bukkit;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import de.maxikg.messenger.AmqpMessenger;
import de.maxikg.messenger.bukkit.commands.MessengerTestCommand;
import de.maxikg.messenger.bukkit.service.DefaultMessengerService;
import de.maxikg.messenger.bukkit.service.DefaultPlayerMessenger;
import de.maxikg.messenger.bukkit.service.MessengerService;
import de.maxikg.messenger.bukkit.utils.Holder;
import de.maxikg.messenger.gson.ObjectMessenger;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

public class MessengerPlugin extends JavaPlugin {

    private final Holder<DefaultPlayerMessenger> serverPlayerMessenger = new Holder<>();
    private final Holder<DefaultPlayerMessenger> proxyPlayerMessenger = new Holder<>();
    private final MessengerService messengerService = new DefaultMessengerService(serverPlayerMessenger, proxyPlayerMessenger);
    private Connection connection;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (!getConfig().getBoolean("features.server_player.enabled") && getConfig().getBoolean("features.proxy_player.enabled")) {
            getLogger().warning("All features are disabled. Skipping connection.");
        } else {
            getLogger().info("Preparing connection to AMQP broker...");
            ConnectionFactory connectionFactory = new ConnectionFactory();
            try {
                connectionFactory.setUri(getConfig().getString("amqp.uri"));
            } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException e) {
                getLogger().log(Level.SEVERE, "Error while configuring amqp endpoint.", e);
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            getLogger().info("Connecting to AMQP broker...");
            try {
                connection = connectionFactory.newConnection();
            } catch (IOException | TimeoutException e) {
                getLogger().log(Level.SEVERE, "Error while opening connection.", e);
                return;
            }
            getLogger().info("Connection successfully established.");

            serverPlayerMessenger.setValue(getConfig().getBoolean("features.server_player.enabled") ? createPlayerMessenger(getConfig().getString("features.server_player.namespace")) : null);
            proxyPlayerMessenger.setValue(getConfig().getBoolean("features.proxy_player.enabled") ? createPlayerMessenger(getConfig().getString("features.proxy_player.namespace")) : null);

            if (getConfig().getBoolean("features.server_player.enabled")) {
                getServer().getPluginManager().registerEvents(new PlayerListener(serverPlayerMessenger), this);
            }
        }

        getServer().getServicesManager().register(MessengerService.class, messengerService, this, ServicePriority.Normal);

        getCommand("messenger-test").setExecutor(new MessengerTestCommand(getServer(), getServer().getServicesManager().getRegistration(MessengerService.class)).init());

        getLogger().info("Startup complete.");
    }

    @Override
    public void onDisable() {
        if (connection != null && connection.isOpen()) {
            try {
                connection.close();
            } catch (IOException ignore) {
            }
        }
    }

    private DefaultPlayerMessenger createPlayerMessenger(String namespace) {
        AmqpMessenger messenger = new AmqpMessenger(connection, getConfig().getString("amqp.exchange"), namespace);
        messenger.initialize();
        return new DefaultPlayerMessenger(messenger, new ObjectMessenger(messenger, getClassLoader()));
    }
}
