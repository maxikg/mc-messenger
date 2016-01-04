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

import com.google.common.base.Supplier;
import de.maxikg.messenger.bukkit.service.DefaultPlayerMessenger;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    @NonNull
    private final Supplier<DefaultPlayerMessenger> messengerSupplier;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        DefaultPlayerMessenger messenger = messengerSupplier.get();
        if (messenger != null)
            messenger.getRawMessenger().getQueue().subscribe(event.getPlayer().getUniqueId().toString());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        DefaultPlayerMessenger messenger = messengerSupplier.get();
        if (messenger != null)
            messenger.getRawMessenger().getQueue().unsubscribe(event.getPlayer().getUniqueId().toString());
    }
}
