package zz.kidog.oglib.xpacket;

import zz.kidog.oglib.ogLib;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public final class FrozenXPacketHandler {

    private static String GLOBAL_MESSAGE_CHANNEL;
    static final String PACKET_MESSAGE_DIVIDER = "||";

    public static void init() {
        FileConfiguration config = ogLib.getInstance().getConfig();
        GLOBAL_MESSAGE_CHANNEL = config.getString("packetChannel", "XPacket:ALL");
        String localHost = config.getString("Redis.Host");
        int localDb = config.getInt("Redis.DbId", 0);
        String remoteHost = config.getString("BackboneRedis.Host");
        int remoteDb = config.getInt("BackboneRedis.DbId", 0);
        boolean sameServer = localHost.equalsIgnoreCase(remoteHost) && localDb == remoteDb;
        FrozenXPacketHandler.connectToServer(ogLib.getInstance().getLocalJedisPool());
        if (!sameServer) {
            FrozenXPacketHandler.connectToServer(ogLib.getInstance().getBackboneJedisPool());
        }
    }

    public static void connectToServer(JedisPool connectTo) {
        if (ogLib.testing) {
            return;
        }
        Thread subscribeThread = new Thread(() -> {
            while (ogLib.getInstance().isEnabled()) {
                try {
                    Jedis jedis = connectTo.getResource();
                    Throwable throwable = null;
                    try {
                        XPacketPubSub pubSub = new XPacketPubSub();
                        String channel = GLOBAL_MESSAGE_CHANNEL;
                        jedis.subscribe(pubSub, channel);
                    }
                    catch (Throwable pubSub) {
                        throwable = pubSub;
                        throw pubSub;
                    }
                    finally {
                        if (jedis == null) continue;
                        if (throwable != null) {
                            try {
                                jedis.close();
                            }
                            catch (Throwable pubSub) {
                                throwable.addSuppressed(pubSub);
                            }
                            continue;
                        }
                        jedis.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "ogLib - XPacket Subscribe Thread");
        subscribeThread.setDaemon(true);
        subscribeThread.start();
    }

    public static void sendToAll(XPacket packet) {
        FrozenXPacketHandler.send(packet, ogLib.getInstance().getBackboneJedisPool());
    }

    public static void sendToAllViaLocal(XPacket packet) {
        FrozenXPacketHandler.send(packet, ogLib.getInstance().getLocalJedisPool());
    }

    public static void send(XPacket packet, JedisPool sendOn) {
        if (!ogLib.getInstance().isEnabled()) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(ogLib.getInstance(), () -> {
            try (Jedis jedis = sendOn.getResource()){
                String encodedPacket = packet.getClass().getName() + PACKET_MESSAGE_DIVIDER + ogLib.PLAIN_GSON.toJson(packet);
                jedis.publish(GLOBAL_MESSAGE_CHANNEL, encodedPacket);
            }
        });
    }

    private FrozenXPacketHandler() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

