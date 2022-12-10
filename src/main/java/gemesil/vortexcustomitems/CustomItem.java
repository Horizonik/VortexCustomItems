package gemesil.vortexcustomitems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class CustomItem {

    // -- OBJECT VARIABLES --
    private ItemStack customItem;
    private String itemName;
    private Particle itemParticle;
    private PotionEffectType itemEffect;
    private int itemEffectLevel;

    // -- CONSTRUCTOR --
    public CustomItem(Material itemType, int customModelData, String itemName, String rarity, String itemDescription, LinkedHashMap<Enchantment, Integer> enchants, Particle particle, PotionEffectType potionEffectType, int effectLevel) {

        // Start by assigning variables to the object
        this.itemName = itemName;
        this.itemParticle = particle;
        this.itemEffect = potionEffectType;
        this.itemEffectLevel = effectLevel;

        // Create a new item
        ItemStack item = new ItemStack(itemType);
        ItemMeta itemMeta = item.getItemMeta();
        List<String> itemLores = new ArrayList<>();

        // Set the custom model data to the same value as the texture-pack
        itemMeta.setCustomModelData(customModelData);

        // Make the item unbreakable
        itemMeta.setUnbreakable(true);

        // Go over all enchants that we would like to put on the item
        for (Enchantment ench : enchants.keySet()) {

            // Add enchant to item
            itemMeta.addEnchant(ench, enchants.get(ench), true);

            // Add the enchant to the lore
            itemLores.add(ChatColor.GRAY + capitalizeString(ench.toString().substring(ench.toString().indexOf(':') + 1, ench.toString().indexOf(',')).replace("_", " ")) + " " + toRomanNumber(enchants.get(ench)));
        }

        itemLores.add(""); // Empty line

        // Add UNBREAKABLE text in lore
        if (itemMeta.isUnbreakable())
            itemLores.add(ChatColor.translateAlternateColorCodes('&',  formatRarity(rarity).get(1) + "&l") + "| " + ChatColor.WHITE + "Unbreakable");

        // Add potion effect in lore
        String potName = capitalizeString(this.getItemEffect().getName().replace("_", " "));
        itemLores.add(ChatColor.translateAlternateColorCodes('&',  formatRarity(rarity).get(1) + "&l") + "| " + ChatColor.WHITE + potName + " " + toRomanNumber(effectLevel));

        // Add rarity in lore
        itemLores.add(formatRarity(rarity).get(0));

        itemLores.add(""); // Empty line

        // Add an item description in lore TODO make line auto separate if too long
        if (itemDescription != null)
            itemLores.add(ChatColor.DARK_GRAY + "\"" + itemDescription + "\"");

        itemMeta.setLore(itemLores);

        // Set item name according to rarity
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', formatRarity(rarity).get(1)) + itemName);

        // Set item flags
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        // Finally, set the newly configured item meta
        item.setItemMeta(itemMeta);

        // Assign the completed item as an object
        this.customItem = item;
    }

    // - GETS & SETS --
    // CustomItem
    public ItemStack getCustomItem() {
        return this.customItem;
    }

    public void setCustomItem(ItemStack customItem) {
        this.customItem = customItem;
    }

    // ItemName
    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    // Particle
    public Particle getItemParticle() {
        return this.itemParticle;
    }

    public void setItemParticle(Particle itemParticle) {
        this.itemParticle = itemParticle;
    }

    // PotionEffectType
    public PotionEffectType getItemEffect() {
        return this.itemEffect;
    }

    public void setItemEffect(PotionEffectType itemEffect) {
        this.itemEffect = itemEffect;
    }

    // PotionEffectLevel
    public int getItemEffectLevel() {
        return this.itemEffectLevel;
    }

    public void setItemEffect(int itemEffectLevel) {
        this.itemEffectLevel = itemEffectLevel;
    }

    // -- UTILITIES VARIABLES --
    // Contains the conversion rates of normal numbers to roman numerics
    LinkedHashMap<String, Integer> numToRomanConversions = new LinkedHashMap<String, Integer>() {{
        put("X", 10);
        put("V", 5);
        put("I", 1);
    }};

    // -- UTILITIES --
    // Converts normal numbers to roman numerals up to 49
    public String toRomanNumber(int baseNum) {

        StringBuilder resultNum = new StringBuilder();

        // Go over all values
        for (String romanNum : numToRomanConversions.keySet() ) {

            // While the num can be subdivided
            while (baseNum >= numToRomanConversions.get(romanNum)) {

                // Subdivide current value from num
                baseNum = baseNum - numToRomanConversions.get(romanNum);

                // Concat the corresponding roman numeric
                resultNum.append(romanNum);
            }
        }

        return resultNum.toString();
    }

    // Assigns correct rarity color based on given rarity name
    public List<String> formatRarity(String rarity) {

        String color, rarityName;

        switch (rarity) {

            case "Extremely Rare":
                color = "&d";
                rarityName = "Extremely Rare";
                break;

            case "Very Rare":
                color = "&5";
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
                ChatColor.translateAlternateColorCodes('&',  color + "&o") + rarityName,
                color
        );
    }

    // Capitalizes every word in a given string
    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
}
