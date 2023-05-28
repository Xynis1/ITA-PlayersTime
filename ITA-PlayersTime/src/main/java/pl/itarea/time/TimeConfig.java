package pl.itarea.time;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand;

public class TimeConfig {

    private String yourTimeMessage;
    private String otherPlayersTimeMessage;
    private String playerNotFoundMessage;

    private List<Reward> rewards;

    public String getYourTimeMessage() {
        return this.yourTimeMessage;
    }

    public String getOtherPlayersTimeMessage() {
        return this.otherPlayersTimeMessage;
    }

    public String getPlayerNotFoundMessage() {
        return this.playerNotFoundMessage;
    }

    public List<Reward> getRewards() {
        return this.rewards;
    }

    void load(TimePlugin plugin) {
        plugin.saveDefaultConfig();

        ConfigurationSection messages = plugin.getConfig().getConfigurationSection("messages");
        this.yourTimeMessage = messages.getString("your-time");
        this.otherPlayersTimeMessage = messages.getString("other-players-time");
        this.playerNotFoundMessage = messages.getString("player-not-found");
        this.rewards = new ArrayList<>();

        ConfigurationSection rewards = plugin.getConfig().getConfigurationSection("rewards");

        for (String rewardName : rewards.getKeys(false)) {
            ConfigurationSection rewardData = rewards.getConfigurationSection(rewardName);
            int time = rewardData.getInt("time");

            ConfigurationSection messagesData = rewardData.getConfigurationSection("messages");
            String playerMessage = messagesData.getString("to-player");
            String broadcastMessage = messagesData.getString("broadcast");

            ConfigurationSection itemsData = rewardData.getConfigurationSection("items");
            List<ItemStack> items = new ArrayList<>();
            for (String itemName : itemsData.getKeys(false)) {
                ConfigurationSection itemData = itemsData.getConfigurationSection(itemName);
                ItemStack item = this.getItem(itemData);

                items.add(item);
            }

            this.rewards.add(new Reward(time, items, playerMessage, broadcastMessage));
        }
    }

    private ItemStack getItem(ConfigurationSection config) {
        Material itemType = Material.matchMaterial(config.getString("type"));
        int amount = config.getInt("amount", 1);

        ItemStack item = new ItemStack(itemType, amount);
        ItemMeta meta = item.getItemMeta();

        if (config.contains("display-name")) {
            meta.displayName(this.getComponent(config.getString("display-name")));
        }

        if (config.contains("lore")) {
            List<Component> lore = config.getStringList("lore")
                .stream()
                .map(this::getComponent)
                .toList();

            meta.lore(lore);
        }

        if (config.contains("enchants")) {
            ConfigurationSection enchants = config.getConfigurationSection("enchants");

            for (String enchantName : enchants.getKeys(false)) {
                Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantName.toLowerCase(Locale.ROOT)));
                int level = enchants.getInt(enchantName);

                meta.addEnchant(enchant, level, true);
            }
        }

        item.setItemMeta(meta);

        return item;
    }

    private Component getComponent(String string) {
        return miniMessage().deserialize(miniMessage().serialize(legacyAmpersand().deserialize(string)));
    }

    public record Reward(int time, List<ItemStack> items, String playerMessage, String broadcastMessage) {
    }
}