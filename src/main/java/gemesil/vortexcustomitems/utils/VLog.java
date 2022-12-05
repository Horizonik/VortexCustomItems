package gemesil.vortexcustomitems.utils;

import gemesil.vortexcustomitems.VortexCustomItems;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

// VLog = Vortex Logger
public class VLog {

    private String prefix = ChatColor.YELLOW + "MCVortex | ";

    private VortexCustomItems plugin;
    public VLog(VortexCustomItems plugin) {
        this.plugin = plugin;
    }

    // Send an action bar message to a selected player
    public void sendAB(Player p, String message) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
    }

    // Send a chat message to the selected player
    public void sendChat(Player p, String message) {
        p.sendMessage(prefix + ChatColor.GRAY + message);
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
    }

    public void sendAlert(String message) {
        plugin.getLogger().info(message);
    }
}
