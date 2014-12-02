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

package tk.manf.InventorySQL.tasks;

import lombok.Getter;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import tk.manf.InventorySQL.manager.DatabaseManager;

import java.util.HashMap;
import java.util.UUID;

public class SyncingTask extends BukkitRunnable {

    @Getter
    private HashMap<UUID, PlayerJoinEvent> eventsMap = null;

    public SyncingTask() {
        eventsMap = new HashMap<UUID, PlayerJoinEvent>();
    }

    public void run() {
        if(eventsMap == null || eventsMap.size() == 0) {return;}

        for(UUID UUID: eventsMap.keySet()) {
            DatabaseManager.getInstance().guidedLoad(eventsMap.get(UUID));
        }

        eventsMap.clear();
    }

    /**
     * Adds a player to the list of players to sync
     * @param UUID the UUID of the player
     * @param ev the player's join event
     */
    public void addPlayerToSync(UUID UUID, PlayerJoinEvent ev) {
        eventsMap.put(UUID, ev);
    }
}
