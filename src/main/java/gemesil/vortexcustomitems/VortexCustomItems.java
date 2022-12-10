package gemesil.vortexcustomitems;

import gemesil.vortexcustomitems.commands.Vitem;
import gemesil.vortexlogger.VortexLogger;
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
    public HashMap<ItemStack, CustomItem> customItems;

    // Import custom logger plugin reference
    private final VortexLogger vortexLogger = (VortexLogger) Bukkit.getServer().getPluginManager().getPlugin("VortexLogger");

    // Get reference to other classes
    // TODO make db for all custom items

    @Override
    public void onEnable() {

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
                PotionEffectType.FIRE_RESISTANCE,
                2
        );

        CustomItem topazPickaxe = new CustomItem(
                Material.NETHERITE_PICKAXE,
                2,

                "Topaz Pickaxe",
                "Very Rare",
                "Topaz is love, topaz is life",

                new LinkedHashMap<Enchantment, Integer>() {{
                    put(Enchantment.DIG_SPEED, 12);
                    put(Enchantment.LOOT_BONUS_BLOCKS, 6);
                }},

                Particle.ENCHANTMENT_TABLE,
                PotionEffectType.NIGHT_VISION,
                2
        );

        CustomItem topazAxe = new CustomItem(
                Material.NETHERITE_AXE,
                2,

                "Topaz Axe",
                "Very Rare",
                "I never knew topaz could cut through trees this quickly",

                new LinkedHashMap<Enchantment, Integer>() {{
                    put(Enchantment.DIG_SPEED, 12);
                    put(Enchantment.LOOT_BONUS_BLOCKS, 6);
                }},

                Particle.ENCHANTMENT_TABLE,
                PotionEffectType.NIGHT_VISION,
                2
        );

        CustomItem topazShovel = new CustomItem(
                Material.NETHERITE_SHOVEL,
                2,

                "Topaz Shovel",
                "Very Rare",
                "A huge piece of topaz waxed onto a stick",

                new LinkedHashMap<Enchantment, Integer>() {{
                    put(Enchantment.DIG_SPEED, 12);
                    put(Enchantment.LOOT_BONUS_BLOCKS, 6);
                }},

                Particle.ENCHANTMENT_TABLE,
                PotionEffectType.NIGHT_VISION,
                2
        );

        CustomItem topazHoe = new CustomItem(
                Material.NETHERITE_HOE,
                2,

                "Topaz Hoe",
                "Very Rare",
                "Harvest, fight, this hoe can do everything",

                new LinkedHashMap<Enchantment, Integer>() {{
                    put(Enchantment.DAMAGE_ALL, 10);
                    put(Enchantment.SWEEPING_EDGE, 3);
                    put(Enchantment.KNOCKBACK, 2);
                }},

                Particle.ENCHANTMENT_TABLE,
                PotionEffectType.NIGHT_VISION,
                2
        );

        CustomItem topazSword = new CustomItem(
                Material.NETHERITE_SWORD,
                2,

                "Topaz Sword",
                "Very Rare",
                "Slay them all with a smile on your face",

                new LinkedHashMap<Enchantment, Integer>() {{
                    put(Enchantment.DAMAGE_ALL, 8);
                    put(Enchantment.SWEEPING_EDGE, 3);
                    put(Enchantment.KNOCKBACK, 1);
                }},

                Particle.ENCHANTMENT_TABLE,
                PotionEffectType.NIGHT_VISION,
                2
        );

        // Register custom items
        customItems.put (blazeSword.getCustomItem(), blazeSword);
        customItems.put (topazPickaxe.getCustomItem(), topazPickaxe);
        customItems.put (topazHoe.getCustomItem(), topazHoe);
        customItems.put (topazAxe.getCustomItem(), topazAxe);
        customItems.put (topazShovel.getCustomItem(), topazShovel);
        customItems.put (topazSword.getCustomItem(), topazSword);


        // Register commands
        getCommand("vitem").setExecutor(new Vitem(this, vortexLogger));

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
                if (customItems.get(heldItem).getItemEffect() != null) {

                    // Check if any other effects are present
                    for (PotionEffect potEffect : p.getActivePotionEffects())

                        // If the player has a different effect than what the items gives
                        if (potEffect.getType() != customItems.get(heldItem).getItemEffect())
                            p.removePotionEffect(potEffect.getType());

                    p.addPotionEffect(new PotionEffect(customItems.get(heldItem).getItemEffect(), 9999999, customItems.get(heldItem).getItemEffectLevel() - 1));
                }

                // Apply particle effect around player
                if (customItems.get(heldItem).getItemParticle() != null)
                    p.getWorld().spawnParticle(customItems.get(heldItem).getItemParticle(), p.getLocation(), 5,  1,  0.2,  1,  0);

            }
        }
    }


}
