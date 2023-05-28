package pl.itarea.time.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.itarea.time.TimePlugin;
import pl.itarea.time.utils.ChatHelper;
import pl.itarea.time.utils.TimeFormatter;

import java.util.List;

import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed;

public class TimeCommand implements TabExecutor {

    private final TimePlugin plugin;

    public TimeCommand(TimePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        OfflinePlayer player;

        if (args.length >= 1) {
            player = Bukkit.getOfflinePlayer(args[0]);

            if (player.getName() == null || !player.hasPlayedBefore()) {
                ChatHelper.sendMessage(sender, this.plugin.config().getPlayerNotFoundMessage(), unparsed("player-name", args[0]));
                return true;
            }

        } else if (sender instanceof Player senderPlayer) {
            player = senderPlayer;

        } else {
            sender.sendMessage("Nie możesz użyć tej komendy z poziomu konsoli.");
            return true;
        }

        long playTime = this.plugin.getPlayTimeManager().getPlayerPlayTime(player.getUniqueId());

        ChatHelper.sendMessage(sender,
            player == sender ?
                this.plugin.config().getYourTimeMessage() :
                this.plugin.config().getOtherPlayersTimeMessage(),
            unparsed("time", TimeFormatter.formatTime(playTime)),
            unparsed("player-name", player.getName())
        );

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return null;
        }

        return List.of();
    }
}
