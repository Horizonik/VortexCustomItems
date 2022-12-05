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
    public ItemStack currentlyHeld;
    public Player currentlyHeldPlayer;

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
        customItems = new HashMap<ItemStack, ArrayList<Object>>();

        // Add a new custom item
        customItems.put(
                itemRecipes.makeCustomItem(
                        Material.DIAMOND_SWORD,
                        10,
                        ChatColor.YELLOW + "Blaze Sword",
                        Arrays.asList(
                                ChatColor.GRAY + "Sharpness XII",
                                ChatColor.GRAY + "Smite XII",
                                ChatColor.GRAY + "Bane of Arthropods XII",
                                ChatColor.GRAY + "Looting VI",
                                ChatColor.GRAY + "Sweeping Edge III",
                                ChatColor.BLUE + "Speed III",
                                "",
                                ChatColor.translateAlternateColorCodes('&', "&6&l") + "| " + ChatColor.YELLOW + "LEGENDARY"
                        ),
                        new HashMap<Enchantment, Integer>() {{
                            put(Enchantment.DAMAGE_ALL, 12);
                            put(Enchantment.DAMAGE_UNDEAD, 12);
                            put(Enchantment.DAMAGE_ARTHROPODS, 12);
                            put(Enchantment.SWEEPING_EDGE, 3);
                            put(Enchantment.LOOT_BONUS_MOBS, 6);
                        }}
                ),
                new ArrayList<Object>() {{
                    add(Particle.FLAME);
                    add(PotionEffectType.SPEED);
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
        // if any players are online
        if (Bukkit.getServer().getOnlinePlayers().size() > 0) {

            // Iterate over all of them
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                ItemStack heldItem = p.getInventory().getItem(p.getInventory().getHeldItemSlot());

                // If a custom model is held
                if (heldItem != null && heldItem.getItemMeta().hasCustomModelData() && heldItem.getItemMeta().getCustomModelData() == 10) {
                    p.addPotionEffect(new PotionEffect((PotionEffectType) customItems.get(heldItem).get(1), 9999999, 2));

                    // If custom item
                    if (customItems.containsKey(heldItem)) {
                        p.getWorld().spawnParticle((Particle) customItems.get(heldItem).get(0), p.getLocation(), 5,  1,  0.2,  1,  0);

                    }
                }
                // if normal item //todo logical error here, the item can also not be custom at this stage
                else {
                    for (PotionEffect potEffect : p.getActivePotionEffects()) {
                        p.removePotionEffect(potEffect.getType());
                    }
                }
            }
        }
    }
}
