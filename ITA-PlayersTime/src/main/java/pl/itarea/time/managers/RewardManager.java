package pl.itarea.time.managers;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.itarea.time.TimeConfig;
import pl.itarea.time.TimePlugin;
import pl.itarea.time.utils.TimeFormatter;

import java.util.Collection;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed;

public class RewardManager {

    private final TimePlugin plugin;

    public RewardManager(TimePlugin plugin) {
        this.plugin = plugin;
    }

    public void checkAndGiveRewards(Player player, long playTime) {
        for (TimeConfig.Reward reward : this.plugin.config().getRewards()) {
            if (reward.time() != playTime) {
                continue;
            }

            ItemStack[] items = reward.items().toArray(ItemStack[]::new);
            Collection<ItemStack> itemsLeft = player.getInventory().addItem(items).values();
            for (ItemStack item : itemsLeft) {
                player.getWorld().dropItem(player.getLocation(), item);
            }

            TagResolver placeholders = TagResolver.resolver(
                unparsed("player-name", player.getName()),
                unparsed("time", TimeFormatter.formatTime(playTime))
            );

            if (reward.broadcastMessage() != null) {
                Bukkit.broadcast(miniMessage().deserialize(reward.broadcastMessage(), placeholders));
            }

            if (reward.playerMessage() != null) {
                player.sendMessage(miniMessage().deserialize(reward.playerMessage(), placeholders));
            }
        }
    }
}
