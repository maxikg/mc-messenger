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

import de.maxikg.messenger.Messenger;
import de.maxikg.messenger.gson.ObjectMessageListener;
import de.maxikg.messenger.gson.ObjectMessenger;
import de.maxikg.messenger.gson.ObjectPublisher;
import de.maxikg.messenger.gson.TypeRegistry;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

@RequiredArgsConstructor
public class DefaultPlayerMessenger implements PlayerMessenger {

    @NonNull
    @Getter
    private final Messenger rawMessenger;

    @NonNull
    private final ObjectMessenger messenger;

    @Override
    public void submit(OfflinePlayer target, Object message) {
        submit(target.getUniqueId(), message);
    }

    @Override
    public void submit(UUID uuid, Object message) {
        messenger.getPublisher().publish(uuid.toString(), message);
    }

    @Override
    public <T> void register(ObjectMessageListener<T> objectMessageListener) {
        messenger.getListenerRegistry().register(objectMessageListener);
    }

    @Override
    public TypeRegistry getTypeRegistry() {
        return messenger.getTypeRegistry();
    }
}
