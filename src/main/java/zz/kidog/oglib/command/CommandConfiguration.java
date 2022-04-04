package zz.kidog.oglib.command;

import org.bukkit.ChatColor;

public class CommandConfiguration {
    private String noPermissionMessage;

    public CommandConfiguration setNoPermissionMessage(String noPermissionMessage) {
        this.noPermissionMessage = ChatColor.translateAlternateColorCodes('&', noPermissionMessage);
        return this;
    }

    public String getNoPermissionMessage() {
        return this.noPermissionMessage;
    }
}

