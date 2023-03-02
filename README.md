# What is it exactly?
**VortexCustomItems** is an unfinished Minecraft 1.19.2 plugin coded by Ofek Buchnik, it adds new items to the game each with their own custom effects, particles, enchantments and textures. This plugin is meant to be used with a corresponding texture-pack.

### The way it works:
You can create a new CustomItem object, the constructor takes arguments that define what the item will do, what particles and effects it will emit and more. It allows full customization of every specific item.
The constructor knows how to format the id names of effects, particles and enchantments names into a read-able format and displays them inside the item's lore.

The way to see assign custom textures for every custom item is by giving it a customModelData. Each vanilla item type can get a customModelData, once added we can then create a texturepack and define a texture for vanilla item X that has our custoModelData Y and so on.

The item is then added to the game and can be accessed by running the command
    /vitem get {item_name}
  
Alternatively to see a list of all existing custom items you can run
    /vitem list
  
## Dependencies
- VortexLogger - Handles plugin-to-player messages like hotbar pop ups or chat notifications. [You can download VortexLogger here](https://github.com/Gemesil/VortexLogger/releases/tag/v1.0.0).
