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

import com.google.common.base.Supplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultMessengerService implements MessengerService {

    private final Supplier<? extends PlayerMessenger> serverPlayerMessenger;
    private final Supplier<? extends PlayerMessenger> proxyPlayerMessenger;

    @Override
    public PlayerMessenger getServerPlayerMessenger() {
        PlayerMessenger messenger = serverPlayerMessenger.get();
        if (messenger == null)
            throw new IllegalStateException("ServerPlayerMessenger is disabled.");
        return serverPlayerMessenger.get();
    }

    @Override
    public boolean isServerPlayerMessengerEnabled() {
        return serverPlayerMessenger.get() != null;
    }

    @Override
    public PlayerMessenger getProxyPlayerMessenger() {
        PlayerMessenger messenger = proxyPlayerMessenger.get();
        if (messenger == null)
            throw new IllegalStateException("ProxyPlayerMessenger is disabled.");
        return proxyPlayerMessenger.get();
    }

    @Override
    public boolean isProxyPlayerMessengerEnabled() {
        return proxyPlayerMessenger.get() != null;
    }
}
