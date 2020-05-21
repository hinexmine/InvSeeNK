package tim03we.invsee;

/*
 * This software is distributed under "GNU General Public License v3.0".
 * This license allows you to use it and/or modify it but you are not at
 * all allowed to sell this plugin at any cost. If found doing so the
 * necessary action required would be taken.
 *
 * GunGame is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License v3.0 for more details.
 *
 * You should have received a copy of the GNU General Public License v3.0
 * along with this program. If not, see
 * <https://opensource.org/licenses/GPL-3.0>.
 */

import cn.nukkit.plugin.PluginBase;
import tim03we.invsee.commands.*;

public class InvSee extends PluginBase {

    private static InvSee instance;

    public static InvSee getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        Language.init();
        this.getServer().getCommandMap().register("invsee", new EnderCommand());
        this.getServer().getCommandMap().register("invsee", new InvSeeCommand());
    }
}
