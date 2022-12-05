package gemesil.vortexcustomitems.commands;

import gemesil.vortexcustomitems.VortexCustomItems;
import gemesil.vortexcustomitems.utils.VLog;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

// Vitem = Vortex Item
public class Vitem implements CommandExecutor {

    // Get main plugin reference
    private VortexCustomItems plugin;
    private VLog vLog;

    public Vitem(VortexCustomItems plugin, VLog vLog) {
        this.plugin = plugin;
        this.vLog = vLog;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        // Check if the executor is not a player
        if (!(commandSender instanceof Player)) {
            vLog.sendAlert("Must be a player to execute this command!");
            return true;
        }

        // Get player that used the command
        Player p = (Player) commandSender;

        // When args were entered
        if (args.length > 0) {

            switch (args[0]) {

                // If the player is trying to get an item
                case "get":

                    // If a 2nd arg value was entered (an item name supposedly)
                    if (args.length > 1) {

                        String inputItemName = "";

                        // Get over all args that come after the "get" word, concat them into one string
                        for (int i = 1; i < args.length; i++) {
                            inputItemName += args[i];

                            // Add space between words
                            if (i < args.length - 1)
                                inputItemName += " ";
                        }

                        // Check if the name entered by the player matches any custom item names
                        for (ItemStack customItem : plugin.customItems.keySet()) {

                            // If the name matches
                            if (customItem.getItemMeta().getDisplayName().contains(inputItemName)) {

                                // Add the customItem to the player's inventory
                                if (p.getInventory().firstEmpty() == -1)
                                    p.getLocation().getWorld().dropItemNaturally(p.getLocation(), customItem);
                                else
                                    p.getInventory().addItem(customItem);

                                vLog.sendChat(p, "Gave " + customItem.getItemMeta().getDisplayName() + ChatColor.GRAY + " to " + p.getDisplayName() + ".");
                            } else
                                vLog.sendChat(p, ChatColor.GRAY + "Item name" + ChatColor.RED + "not found" + ChatColor.GRAY + ", use /vitem list to see all names!");
                        }
                    }
                    break;

                // If the player is trying to list all item names
                case "list":

                    vLog.sendChat(p, "CUSTOM ITEMS:");

                    // Display all custom item names in chat
                    for (ItemStack customItem : plugin.customItems.keySet())
                        vLog.sendChat(p, "• " + customItem.getItemMeta().getDisplayName() + ".");
                    break;

                // If the player is trying to see help menu
                case "help":
                    vLog.sendChat(p,
                            "VITEM COMMANDS:\n" +
                                    ChatColor.BOLD + "/vitem help" + ChatColor.GRAY + " - display this help menu\n" +
                                    ChatColor.BOLD + "/vitem get [item name]" + ChatColor.GRAY + " - get a custom item\n" +
                                    ChatColor.BOLD + "/vitem list" + ChatColor.GRAY + " - Show names of all custom items"
                    );
                    break;
            }
        }
        // When there weren't any args, show help message
        else {
            vLog.sendChat(p,
        "VITEM COMMANDS:\n" +
                ChatColor.BOLD + "/vitem help" + ChatColor.GRAY + " - display this help menu\n" +
                ChatColor.BOLD + "/vitem get [item name]" + ChatColor.GRAY + " - get a custom item\n" +
                ChatColor.BOLD + "/vitem list" + ChatColor.GRAY + " - Show names of all custom items"
            );
        }

        return true;
    }
}
