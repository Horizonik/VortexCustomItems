package gemesil.vortexcustomitems;

import gemesil.vortexcustomitems.commands.Vitem;
import gemesil.vortexcustomitems.events.ItemEvents;
import gemesil.vortexcustomitems.utils.VLog;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public final class VortexCustomItems extends JavaPlugin {

    int sched;
    private VLog vLog;
    public HashMap<ItemStack, CustomItem> customItems;

    // Get reference to other classes

    @Override
    public void onEnable() {

        // Get reference to other classes
        vLog = new VLog(this);

        customItems = new HashMap<>();

        CustomItem blazeSword = new CustomItem(
                Material.DIAMOND_SWORD,
                2,

                "Blaze Sword",
                "Extremely Rare",
                "Let them burn, let everything burn!",

                new LinkedHashMap<Enchantment, Integer>() {{
                    put(Enchantment.DAMAGE_ALL, 12);
                    put(Enchantment.DAMAGE_UNDEAD, 12);
                    put(Enchantment.DAMAGE_ARTHROPODS, 12);
                    put(Enchantment.SWEEPING_EDGE, 3);
                    put(Enchantment.LOOT_BONUS_MOBS, 6);
                }},

                Particle.FLAME,
                PotionEffectType.FIRE_RESISTANCE
        );

        CustomItem blazePickaxe = new CustomItem(
                Material.DIAMOND_PICKAXE,
                2,

                "Blaze Pickaxe",
                "Extremely Rare",
                "Mine blazingly fast!",

                new LinkedHashMap<Enchantment, Integer>() {{
                    put(Enchantment.DIG_SPEED, 12);
                    put(Enchantment.LOOT_BONUS_BLOCKS, 6);
                }},

                Particle.CAMPFIRE_COSY_SMOKE,
                PotionEffectType.FAST_DIGGING
        );

        // Register custom items
        customItems.put (blazeSword.getCustomItem(), blazeSword);
        customItems.put (blazePickaxe.getCustomItem(), blazePickaxe);

        // Register commands
        getCommand("vitem").setExecutor(new Vitem(this, vLog));

        // Register event listeners
        getServer().getPluginManager().registerEvents(new ItemEvents(this), this);

        if (!Bukkit.getScheduler().isCurrentlyRunning(sched)) {
            sched = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

                @Override
                public void run() {
                    onTick();
                }
            }, 20, 10);
        }
    }


    public void onTick() {
        // Are any players online at the moment?
        if (Bukkit.getServer().getOnlinePlayers().size() > 0) {

            // Go over all online players
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {

                p.setPlayerListName("\uE239 " + ChatColor.GRAY + p.getName());

                // Get the currently held item by the player
                ItemStack heldItem = p.getInventory().getItem(p.getInventory().getHeldItemSlot());

                // Is the item normal?
                if (heldItem == null || !heldItem.hasItemMeta() || !heldItem.getItemMeta().hasCustomModelData() || !customItems.containsKey(heldItem)) {

                    // Is the player affected by any potion effects?
                    if (p.getActivePotionEffects().size() > 0)

                        // Remove all potion effects from the player
                        for (PotionEffect potEffect : p.getActivePotionEffects())
                            p.removePotionEffect(potEffect.getType());

                    break;
                }

                // If any effects on player
                if (p.getActivePotionEffects().size() > 0)

                    // Check if an effect is on that isnt part of the held item
                    for (PotionEffect potEffect : p.getActivePotionEffects())
                        p.removePotionEffect(potEffect.getType());

                // Apply corresponding potion effects (potion level is set by the customModelData number)
                if (customItems.get(heldItem).getCustomItemPotionEffectType() != null) {

                    // Check if any other effects are present
                    for (PotionEffect potEffect : p.getActivePotionEffects())

                        // If the player has a different effect than what the items gives
                        if (potEffect.getType() != customItems.get(heldItem).getCustomItemPotionEffectType())
                            p.removePotionEffect(potEffect.getType());

                    p.addPotionEffect(new PotionEffect(customItems.get(heldItem).getCustomItemPotionEffectType(), 9999999, 0));
                }

                // Apply particle effect around player
                if (customItems.get(heldItem).getCustomItemParticle() != null)
                    p.getWorld().spawnParticle(customItems.get(heldItem).getCustomItemParticle(), p.getLocation(), 5,  1,  0.2,  1,  0);

            }
        }
    }


}
