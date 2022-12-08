package gemesil.vortexcustomitems;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Array;
import java.util.*;

public class ItemRecipes {

    private final VortexCustomItems plugin;

    public ItemRecipes(VortexCustomItems plugin) {
        this.plugin = plugin;
    }

    List<String> itemLores;

    // Creates an item, gives it a custom model data and returns it
    public ItemStack makeCustomItem(Material itemType, int customModelData, String itemName, LinkedHashMap<Enchantment, Integer> enchants, String rarity) {

        /*
        ItemType = Material.DIAMOND_SWORD
        customModelData = The value from the .json in the texture-pack
        ItemName = the display name of the item
        ItemLore = a list of lines to display in the item lore
        */

        // - Create a custom item and assign it some metadata -
        ItemStack item = new ItemStack(itemType);
        ItemMeta itemMeta = item.getItemMeta();

        if (!item.hasItemMeta()) {
            Bukkit.getLogger().info("Item is missing its item meta! Did you use the wrong Material type?");
            return null;
        }

        // Set the custom model data to the same value as the texturepack
        itemMeta.setCustomModelData(customModelData);

        // Make the item unbreakable
        itemMeta.setUnbreakable(true);

        // Initialize our lore container
        itemLores = new ArrayList<>();

        // Go over all enchants that we would like to put on the item
        for (Enchantment ench : enchants.keySet()) {

            // Add enchant to item
            itemMeta.addEnchant(ench, enchants.get(ench), true);

            // Add the enchant to the lore
            if (enchantAsString.containsKey(ench))
                itemLores.add(ChatColor.GRAY + enchantAsString.get(ench).substring(0, 1).toUpperCase() + enchantAsString.get(ench).substring(1) + " " + toRomanNumber(enchants.get(ench)));

            // When I made a mistake and forgot to type a as string version of the enchant
            else
                Bukkit.getLogger().info("The enchant " + ench.toString() + " is missing from enchantAsString hashmap!");
        }

        itemLores.add(""); // Empty line

        // Add UNBREAKABLE text in lore
        if (itemMeta.isUnbreakable())
            itemLores.add(ChatColor.translateAlternateColorCodes('&',  formatRarity(rarity).get(1) + "&l") + "| " + ChatColor.WHITE + "Unbreakable");

        // Add rarity in lore
        itemLores.add(formatRarity(rarity).get(0));

        itemLores.add(""); // Empty line

         // Add an item description in lore TODO make line auto separate if too long
        itemLores.add(ChatColor.DARK_GRAY + "\"Sword used by the ancient blazes of the server, whereby lorem ipsom\"");

        itemMeta.setLore(itemLores);

        // Set item name according to rarity
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', formatRarity(rarity).get(1)) + itemName);

        // Set item flags
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        // Finally, set the newly configured item meta
        item.setItemMeta(itemMeta);

        return item;
    }

    // Creates an item, gives it a custom model data and returns it
    public ItemStack makeCustomItem(Material itemType, int customModelData, String itemName, List<String> itemLore) {

        // ItemType = Material.DIAMOND_SWORD
        // customModelData = The value from the .json in the texture-pack
        // ItemName = the display name of the item
        // ItemLore = a list of lines to display in the item lore

        // - Create a custom item and assign it some metadata -
        ItemStack item = new ItemStack(itemType);
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setCustomModelData(customModelData);
            itemMeta.setDisplayName(itemName);
            itemMeta.setLore(itemLore);
            itemMeta.setUnbreakable(true);
        }

        item.setItemMeta(itemMeta);

        return item;
    }

    // Gets an item and a recipe shape, makes a recipe for it
    public ShapedRecipe makeCustomRecipe(ItemStack item, String recipeShape, HashMap<Material, Character> materials) {

        // - Create a custom recipe for the item -
        NamespacedKey key = new NamespacedKey(plugin, item.getItemMeta().getDisplayName());
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape(recipeShape);

        // Workbench view:
        // #   #
        // #   #
        //   #

        // Go over hashmap, for every material define a character that represents it
        for (Material mat : materials.keySet()) {
            recipe.setIngredient(materials.get(mat), mat);
        }

        return recipe;
    }

    // Contains the conversion rates of normal numbers to roman numerics
    LinkedHashMap<String, Integer> conversions = new LinkedHashMap<String, Integer>() {{
        put("X", 10);
        put("V", 5);
        put("I", 1);
    }};

    // Converts normal numbers to roman numerals up to 49
    public String toRomanNumber(int baseNum) {

        StringBuilder resultNum = new StringBuilder();

        // Go over all values
        for (String romanNum : conversions.keySet() ) {

            // While the num can be subdivided
            while (baseNum >= conversions.get(romanNum)) {

                // Subdivide current value from num
                baseNum = baseNum - conversions.get(romanNum);

                // Concat the corresponding roman numeric
                resultNum.append(romanNum);
            }
        }

        return resultNum.toString();
    }

    public List<String> formatRarity(String rarity) {

        String color, rarityName;

        switch (rarity) {

            case "Extremely Rare":
                color = "&c";
                rarityName = "Extremely Rare";
                break;

            case "Very Rare":
                color = "&d";
                rarityName = "Very Rare";
                break;

            case "Rare":
                color = "&b";
                rarityName = "Rare";
                break;

            case "Uncommon":
                color = "&a";
                rarityName = "Uncommon";
                break;

            case "Common":
                color = "&7";
                rarityName = "Common";
                break;

            default:
                color = "&f";
                rarityName = "ERROR!";
                break;
        }

        return Arrays.asList(
                ChatColor.translateAlternateColorCodes('&',  color + "&l") + "| " + ChatColor.WHITE + rarityName,
                color
        );
    }

    // Contains all enchantments and their corresponding names as strings
    HashMap<Enchantment, String> enchantAsString = new HashMap<Enchantment, String>() {{

        // Swords
        put(Enchantment.DAMAGE_ALL, "Sharpness");
        put(Enchantment.DAMAGE_UNDEAD, "Smite");
        put(Enchantment.LOOT_BONUS_MOBS, "Looting");
        put(Enchantment.DAMAGE_ARTHROPODS, "Bane of Arthropods");
        put(Enchantment.SWEEPING_EDGE, "Sweeping Edge");

        // Tools
        put(Enchantment.DIG_SPEED, "Efficiency");
        put(Enchantment.LOOT_BONUS_BLOCKS, "Fortune");
        put(Enchantment.DURABILITY, "Unbreaking");
        put(Enchantment.SILK_TOUCH, "Silk Touch");

        // Armors
        put(Enchantment.PROTECTION_ENVIRONMENTAL, "Protection");
        put(Enchantment.PROTECTION_FALL, "Feather Falling");
        put(Enchantment.THORNS, "Thorns");
    }};
}