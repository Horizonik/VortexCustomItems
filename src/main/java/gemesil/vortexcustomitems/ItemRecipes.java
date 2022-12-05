package gemesil.vortexcustomitems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class ItemRecipes {

    private final VortexCustomItems plugin;

    public ItemRecipes(VortexCustomItems plugin) {
        this.plugin = plugin;
    }



    // Creates an item, gives it a custom model data and returns it
    public ItemStack makeCustomItem(Material itemType, int customModelData, String itemName, List<String> itemLore, HashMap<Enchantment, Integer> enchants) {

        // ItemType = Material.DIAMOND_SWORD
        // customModelData = The value from the .json in the texturepack
        // ItemName = the display name of the item
        // ItemLore = a list of lines to display in the item lore

        // - Create a custom item and assign it some metadata -
        ItemStack item = new ItemStack(itemType);
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.RED + itemName);
            itemMeta.setCustomModelData(customModelData);
            itemMeta.setLore(itemLore);
            itemMeta.setUnbreakable(true);

            // Add each enchant to the item
            for (Enchantment ench : enchants.keySet()) {
                itemMeta.addEnchant(ench, enchants.get(ench), true);
            }

            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(itemMeta);
        }

        return item;
    }

    // Creates an item, gives it a custom model data and returns it
    public ItemStack makeCustomItem(Material itemType, int customModelData, String itemName, List<String> itemLore) {

        // ItemType = Material.DIAMOND_SWORD
        // customModelData = The value from the .json in the texturepack
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

}