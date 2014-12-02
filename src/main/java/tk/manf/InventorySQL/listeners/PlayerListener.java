/**
 * Copyright (c) 2013 Exo-Network
 * 
 * This software is provided 'as-is', without any express or implied
 * warranty. In no event will the authors be held liable for any damages
 * arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 *    1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 
 *    2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 
 *    3. This notice may not be removed or altered from any source
 *    distribution.
 * 
 * manf                   info@manf.tk
 */

package tk.manf.InventorySQL.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import tk.manf.InventorySQL.manager.DatabaseManager;
import tk.manf.InventorySQL.manager.InventoryLockingSystem;

public class PlayerListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerLogin(final PlayerJoinEvent ev) {
        DatabaseManager.getInstance().guidedLoad(ev);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickup(final PlayerPickupItemEvent ev) {
        InventoryLockingSystem.getInstance().check(ev);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDrop(final PlayerDropItemEvent ev) {
        InventoryLockingSystem.getInstance().check(ev);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(final InventoryClickEvent ev) {
        InventoryLockingSystem.getInstance().check(ev, ev.getWhoClicked());
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(final InventoryOpenEvent ev) {
        InventoryLockingSystem.getInstance().check(ev, ev.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent ev) {
        DatabaseManager.getInstance().savePlayer(ev.getPlayer());
        InventoryLockingSystem.getInstance().removeLock(String.valueOf(ev.getPlayer().getUniqueId()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeave(final PlayerQuitEvent ev) {
        DatabaseManager.getInstance().savePlayer(ev.getPlayer());
        InventoryLockingSystem.getInstance().removeLock(String.valueOf(ev.getPlayer().getUniqueId()));
    }
}
