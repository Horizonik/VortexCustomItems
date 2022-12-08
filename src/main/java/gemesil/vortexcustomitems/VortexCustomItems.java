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
    public HashMap<ItemStack, ArrayList<Object>> customItems;

    // Get reference to other classes
    private VLog vLog;
    private ItemRecipes itemRecipes;

    @Override
    public void onEnable() {

        // Get reference to other classes
        VLog vLog = new VLog(this);
        ItemRecipes itemRecipes = new ItemRecipes(this);

        // Register a new recipe
        //Bukkit.addRecipe(itemRecipes.CustomElytraRecipe());

        // todo convert this to hashmap
        //customItems = new ArrayList<ItemStack>();
        customItems = new HashMap<>();

        // Add a new custom item
        customItems.put(
            itemRecipes.makeCustomItem(
                    Material.DIAMOND_SWORD,
                    2,
                    "Blaze Sword",
                    new LinkedHashMap<Enchantment, Integer>() {{
                        put(Enchantment.DAMAGE_ALL, 12);
                        put(Enchantment.DAMAGE_UNDEAD, 12);
                        put(Enchantment.DAMAGE_ARTHROPODS, 12);
                        put(Enchantment.SWEEPING_EDGE, 3);
                        put(Enchantment.LOOT_BONUS_MOBS, 6);
                    }},
                    "Extremely Rare"
            ),
            new ArrayList<Object>() {{
                add(Particle.FLAME);
                add(PotionEffectType.FIRE_RESISTANCE);
            }}
        );

        customItems.put(
                itemRecipes.makeCustomItem(
                        Material.DIAMOND_PICKAXE,
                        2,
                        "Blaze Pickaxe",
                        new LinkedHashMap<Enchantment, Integer>() {{
                            put(Enchantment.DIG_SPEED, 12);
                            put(Enchantment.LOOT_BONUS_BLOCKS, 6);
                        }},
                        "Extremely Rare"
                ),
                new ArrayList<Object>() {{
                    add(Particle.FLAME);
                    add(PotionEffectType.FIRE_RESISTANCE);
                }}
        );

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

                // Get the currently held item by the player
                ItemStack heldItem = p.getInventory().getItem(p.getInventory().getHeldItemSlot());

                // Is the item normal?
                if (heldItem == null || !heldItem.hasItemMeta() || !heldItem.getItemMeta().hasCustomModelData() || !customItems.containsKey(heldItem)) {

                    // Is the player affected by any potion effects?
                    if (p.getActivePotionEffects().size() > 0)

                        // Remove all potion effects from the player
                        for (PotionEffect potEffect : p.getActivePotionEffects())
                            p.removePotionEffect(potEffect.getType());

                    return;
                }

                // Apply corresponding potion effects (potion level is set by the customModelData number)
                if (customItems.get(heldItem).get(1) != null)
                    p.addPotionEffect(new PotionEffect((PotionEffectType) customItems.get(heldItem).get(1), 9999999, 0));

                // Apply particle effect around player
                if (customItems.get(heldItem).get(0) != null)
                    p.getWorld().spawnParticle((Particle) customItems.get(heldItem).get(0), p.getLocation(), 5,  1,  0.2,  1,  0);

            }
        }
    }


}
