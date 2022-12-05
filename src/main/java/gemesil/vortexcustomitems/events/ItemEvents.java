package gemesil.vortexcustomitems.events;

import gemesil.vortexcustomitems.VortexCustomItems;
import gemesil.vortexcustomitems.utils.VLog;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ItemEvents implements Listener {

    private VortexCustomItems plugin;

    public ItemEvents(VortexCustomItems plugin) {
        this.plugin = plugin;
    }
}
