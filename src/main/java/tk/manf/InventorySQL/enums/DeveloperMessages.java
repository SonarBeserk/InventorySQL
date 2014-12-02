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

package tk.manf.InventorySQL.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DeveloperMessages {
    //#SUPPORT_DEVELOPERS
    //Just remind people to be supportive.
    METRICS_OFF("You turned metrics off :(. Metrics is a good way to support Developers! ( You should also consider donating :) )"),
    METRICS_LOADED("Thanks for supporting us and using Metrics :)"),
    DEPRECATED_CLASS("Sorry, but the loaded class is deprecated"),
    HANDLING_BROKEN("You did not specify proper a proper Handler. Please specify a correct Classname for the missing Handler");

    @Getter
    private String message;
}
