package zz.kidog.oglib.xpacket;

import zz.kidog.oglib.ogLib;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import redis.clients.jedis.JedisPubSub;

@NoArgsConstructor
public class XPacketPubSub extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        Class<?> packetClass;
        int packetMessageSplit = message.indexOf("||");
        String packetClassStr = message.substring(0, packetMessageSplit);
        String messageJson = message.substring(packetMessageSplit + "||".length());
        try {
            packetClass = Class.forName(packetClassStr);
        }
        catch (ClassNotFoundException ignored) {
            return;
        }
        XPacket packet = (XPacket) ogLib.PLAIN_GSON.fromJson(messageJson, packetClass);
        if (ogLib.getInstance().isEnabled()) {
            Bukkit.getScheduler().runTask(ogLib.getInstance(), packet::onReceive);
        }
    }
}

