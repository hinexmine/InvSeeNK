package tim03we.invsee.commands;

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


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import com.nukkitx.fakeinventories.inventory.ChestFakeInventory;
import com.nukkitx.fakeinventories.inventory.FakeSlotChangeEvent;
import tim03we.invsee.Language;

public class EnderCommand extends Command {

    public EnderCommand() {
        super("enderchest", "Open your or the from other players enderchest", "/enderchest [player]");
        setAliases(new String[]{"ec"});
        setPermission("invsee.enderchest");
        setPermissionMessage(Language.get("no.permission"));
    }

    public boolean execute(CommandSender sender, String s, String[] args) {
        if(!testPermission(sender)) {
            return false;
        }
        if (sender instanceof Player) {
            if (args.length < 1) {
                ChestFakeInventory inv = new ChestFakeInventory();
                inv.addListener(this::onSlotChange);
                inv.setName(sender.getName().toLowerCase() + "'s " + Language.translate(false, "enderchest"));
                inv.setContents(((Player) sender).getEnderChestInventory().getContents());
                ((Player)sender).addWindow(inv);
            } else {
                if(sender.hasPermission("invsee.enderchest.other")) {
                    Player target = Server.getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if(target.getName().equalsIgnoreCase(sender.getName())) {
                            sender.sendMessage(Language.translate(true, "same.player"));
                            return true;
                        }
                        ChestFakeInventory inv = new ChestFakeInventory();
                        inv.addListener(this::onSlotChange);
                        inv.setName(target.getName().toLowerCase() + "'s " + Language.translate(false, "enderchest"));
                        inv.setContents(target.getEnderChestInventory().getContents());
                        ((Player)sender).addWindow(inv);
                    } else {
                        sender.sendMessage(Language.translate(true, "player.not.found"));
                    }
                } else {
                    sender.sendMessage(Language.translate(true, "no.permission"));
                }
            }
        } else {
            sender.sendMessage(getUsage());
        }

        return false;
    }

    private void onSlotChange(FakeSlotChangeEvent e) {
        if (e.getInventory() instanceof ChestFakeInventory && e.getInventory().getName().contains("'s " + Language.translate(false, "enderchest"))) {
            if (!e.getInventory().getName().equals(e.getPlayer().getName().toLowerCase() + "'s " + Language.translate(false, "enderchest"))) {
                if(e.getPlayer().hasPermission("invsee.enderchest.move")) {
                    String[] ex = e.getInventory().getName().split("'");
                    Player target = Server.getInstance().getPlayer(ex[0]);
                    if (target != null) {
                        if (e.getPlayer().isOp()) {
                            Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
                                target.getEnderChestInventory().setContents(e.getInventory().getContents());
                            }, 1);
                        } else {
                            e.setCancelled(true);
                        }
                    }
                }
            } else {
                Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
                    e.getPlayer().getEnderChestInventory().setContents(e.getInventory().getContents());
                }, 1);
            }
        }
    }
}
