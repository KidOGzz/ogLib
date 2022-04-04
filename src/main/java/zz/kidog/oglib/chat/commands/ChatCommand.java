package zz.kidog.oglib.chat.commands;

import zz.kidog.oglib.chat.ChatPlayer;
import zz.kidog.oglib.chat.ChatHandler;
import zz.kidog.oglib.chat.ChatPopulator;
import zz.kidog.oglib.command.Command;
import zz.kidog.oglib.command.Param;
import zz.kidog.oglib.ogLib;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

public class ChatCommand {

    @Command(names = "chat", permission = "")
    public static void chat(Player player) {
        ChatPlayer chatPlayer = ChatHandler.getChatPlayer(player.getUniqueId());
        if(ogLib.isBridge()) {
            player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat('-', 53));
            player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Chat " + ChatColor.WHITE.toString() + ChatColor.BOLD + "Modes:");
            player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat('-', 53));
            player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "Current Chat Mode: " + ChatColor.GREEN.toString() + ChatColor.BOLD + chatPlayer.getSelectedPopulator().getName());
            chatPlayer.getRegisteredPopulators().forEach(chatPopulator -> player.sendMessage(ChatColor.WHITE + " " + ChatColor.GREEN + " " + chatPopulator.getName() + ": " + ChatColor.GRAY + chatPopulator.getCommandParam() + ChatColor.DARK_GRAY + " (or prefix your message with " + ChatColor.YELLOW.toString() + ChatColor.BOLD + chatPopulator.getChatChar() + ChatColor.DARK_GRAY + ")"));
            player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat('-', 53));
        } else {
            player.sendMessage(ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat('-', 53));
            player.sendMessage(ChatColor.BLUE.toString() + ChatColor.BOLD + "Chat Modes");
            player.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "You currently have the " + ChatColor.AQUA + chatPlayer.getSelectedPopulator().getName() + ChatColor.GRAY.toString() + ChatColor.ITALIC + " chat mode selected.");
            player.sendMessage("");
            chatPlayer.getRegisteredPopulators().forEach(chatPopulator -> player.sendMessage(ChatColor.GRAY + " » " + ChatColor.BLUE + chatPopulator.getName() + ChatColor.GRAY + " - " + ChatColor.AQUA + chatPopulator.getCommandParam() + ChatColor.DARK_GRAY + " (" + ChatColor.WHITE + "or prefix with " + chatPopulator.getChatChar() + ChatColor.DARK_GRAY + ")"));
            player.sendMessage(ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat('-', 53));
        }
    }

    @Command(names = "pc", permission = "")
    public static void pc(Player player, @Param(name = "message", wildcard = true, defaultValue = "   ") String message) {
        ChatPlayer chatPlayer = ChatHandler.getChatPlayer(player.getUniqueId());
        ChatPopulator chatPopulator = new ChatPopulator.PublicChatProvider();
        if(message.equals("   ")) chatPlayer.setActiveType(chatPopulator);
        else chatPopulator.handleChat(player, message);
    }

}
