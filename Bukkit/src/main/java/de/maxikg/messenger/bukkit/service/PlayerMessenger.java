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

package de.maxikg.messenger.bukkit.service;

import de.maxikg.messenger.gson.ObjectListenerRegistry;
import de.maxikg.messenger.gson.TypeRegistry;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface PlayerMessenger extends ObjectListenerRegistry {

    void submit(OfflinePlayer target, Object message);

    void submit(UUID uuid, Object message);

    TypeRegistry getTypeRegistry();
}
