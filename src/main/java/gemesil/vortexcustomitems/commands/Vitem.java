package gemesil.vortexcustomitems.commands;
import gemesil.vortexcustomitems.VortexCustomItems;
import gemesil.vortexlogger.VortexLogger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

// Vitem = Vortex Item
public class Vitem implements CommandExecutor {

    // Get main plugin reference
    private final VortexCustomItems plugin;
    private final VortexLogger vortexLogger;

    public Vitem(VortexCustomItems plugin, VortexLogger vortexLogger) {
        this.plugin = plugin;
        this.vortexLogger = vortexLogger;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        // Check if the executor is not a player
        if (!(commandSender instanceof Player)) {
            vortexLogger.sendAlert("Must be a player to execute this command!");
            return true;
        }

        // Get player that used the command
        Player p = (Player) commandSender;

        // When args were entered
        if (args.length > 0) {

            switch (args[0]) {

                // If the player is trying to get an item
                case "get":

                    if (args.length < 2) {
                        vortexLogger.sendChat(p, "Fool, you forgot to enter an item name! use /vitem get [name]", true);
                        return true;
                    }

                    // Get over all args that come after the "get" word, concat them into one string
                    StringBuilder inputItemName = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        inputItemName.append(args[i]);

                        // Add space between words
                        if (i < args.length - 1)
                            inputItemName.append(" ");
                    }

                    boolean givenItem = false;

                    // Check if the name entered by the player matches any custom item names
                    for (ItemStack customItem : plugin.customItems.keySet()) {

                        // If the name matches
                        if (customItem.getItemMeta().getDisplayName().contains(inputItemName)) {

                            givenItem = true;

                           // Check if the player's inventory is not full
                            if (p.getInventory().firstEmpty() != -1)

                                // Add the customItem to the player's inventory
                                p.getInventory().addItem(customItem);

                            // When the player's inventory is actually full
                            else
                                // Drop the item on the floor next to them
                                p.getLocation().getWorld().dropItemNaturally(p.getLocation(), customItem);

                            // Send status message to the player and exit function
                            vortexLogger.sendChat(p, "Gave " + customItem.getItemMeta().getDisplayName() + ChatColor.GRAY + " to " + p.getDisplayName() + ".", true);
                        }
                    }

                    if (!givenItem) {
                        vortexLogger.sendChat(p, ChatColor.GRAY + "Item name not found, use /vitem list to see all names!", true);
                    }

                    return true;

                // If the player is trying to list all item names
                case "list":

                    vortexLogger.sendChat(p, "CUSTOM ITEMS:", true);

                    // Display all custom item names in chat
                    for (ItemStack customItem : plugin.customItems.keySet())
                        vortexLogger.sendChat(p, "• " + plugin.customItems.get(customItem).getItemName() + ".", true);

                    return true;

                // If the player is trying to see help menu
                case "help":
                    vortexLogger.sendChat(p,
                            "VITEM COMMANDS:\n" +
                                    ChatColor.BOLD + "/vitem help" + ChatColor.GRAY + " - display this help menu\n" +
                                    ChatColor.BOLD + "/vitem get [item name]" + ChatColor.GRAY + " - get a custom item\n" +
                                    ChatColor.BOLD + "/vitem list" + ChatColor.GRAY + " - Show names of all custom items",
                            true
                    );

                    return true;
            }
        }

        return false;
    }
}
