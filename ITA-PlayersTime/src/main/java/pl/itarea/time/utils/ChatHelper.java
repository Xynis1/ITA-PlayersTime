package pl.itarea.time.utils;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand;

public class ChatHelper {

    public static void sendMessage(CommandSender receiver, String message, TagResolver... placeholders) {
        receiver.sendMessage(legacyAmpersand().deserialize(legacyAmpersand().serialize(miniMessage().deserialize(message, placeholders))));
    }
}