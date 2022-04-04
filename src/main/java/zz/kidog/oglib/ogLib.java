package zz.kidog.oglib;

import com.comphenix.protocol.ProtocolLibrary;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import zz.kidog.oglib.boss.FrozenBossBarHandler;
import zz.kidog.oglib.chat.ChatHandler;
import zz.kidog.oglib.hologram.FrozenHologramHandler;
import zz.kidog.oglib.hologram.listener.HologramListener;
import zz.kidog.oglib.serialization.*;
import zz.kidog.oglib.tab.FrozenTabHandler;
import zz.kidog.oglib.tab.TabAdapter;
import zz.kidog.oglib.autoreboot.AutoRebootHandler;
import zz.kidog.oglib.event.HalfHourEvent;
import zz.kidog.oglib.event.HourEvent;
import zz.kidog.oglib.nametag.FrozenNametagHandler;
import zz.kidog.oglib.redis.RedisCommand;
import zz.kidog.oglib.util.ItemUtils;
import zz.kidog.oglib.util.SignGUI;
import zz.kidog.oglib.util.TPSUtils;
import zz.kidog.oglib.xpacket.FrozenXPacketHandler;
import lombok.Getter;
import zz.kidog.oglib.command.FrozenCommandHandler;
import zz.kidog.oglib.uuid.FrozenUUIDCache;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import zz.kidog.oglib.economy.FrozenEconomyHandler;
import zz.kidog.oglib.protocol.InventoryAdapter;
import zz.kidog.oglib.protocol.LagCheck;
import zz.kidog.oglib.protocol.PingAdapter;
import zz.kidog.oglib.redstone.RedstoneListener;
import zz.kidog.oglib.scoreboard.FrozenScoreboardHandler;
import zz.kidog.oglib.visibility.FrozenVisibilityHandler;
import zz.kidog.oglib.serialization.*;

import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class ogLib extends JavaPlugin {

    private static ogLib instance;
    private long localRedisLastError;
    private long backboneRedisLastError;
    public static boolean testing;
    @Getter private static boolean bridge;
    public static final Random RANDOM;
    public static final Gson GSON;
    public static final Gson PLAIN_GSON;
    @Getter private SignGUI signGUI;
    private JedisPool localJedisPool;
    private JedisPool backboneJedisPool;

    public void onEnable() {
        instance = this;
        testing = this.getConfig().getBoolean("testing", false);
        this.saveDefaultConfig();

        try {
            this.localJedisPool = new JedisPool(new JedisPoolConfig(), this.getConfig().getString("Redis.Host"), 6379, 20000, (this.getConfig().getString("Redis.Password").equals("") ? null : this.getConfig().getString("Redis.Password")), this.getConfig().getInt("Redis.DbId", 0));
        }
        catch (Exception e) {
            this.localJedisPool = null;
            e.printStackTrace();
            this.getLogger().warning("Couldn't connect to a Redis instance at " + this.getConfig().getString("Redis.Host") + ".");
        }
        try {
            this.backboneJedisPool = new JedisPool(new JedisPoolConfig(), this.getConfig().getString("BackboneRedis.Host"), 6379, 20000, (this.getConfig().getString("BackboneRedis.Password").equals("") ? null : this.getConfig().getString("BackboneRedis.Password")), this.getConfig().getInt("BackboneRedis.DbId", 0));
        }
        catch (Exception e) {
            this.backboneJedisPool = null;
            e.printStackTrace();
            this.getLogger().warning("Couldn't connect to a Backbone Redis instance at " + this.getConfig().getString("BackboneRedis.Host") + ".");
        }
        bridge = this.getConfig().getBoolean("Bridge", true);
        FrozenCommandHandler.init();
        FrozenNametagHandler.init();
        FrozenScoreboardHandler.init();
        FrozenUUIDCache.init();
        FrozenXPacketHandler.init();
        FrozenBossBarHandler.init();
        FrozenTabHandler.init();
        AutoRebootHandler.init();
        FrozenVisibilityHandler.init();
        ChatHandler.init();
        FrozenHologramHandler.init();
        FrozenCommandHandler.registerAll(this);
        FrozenCommandHandler.registerPackage(this, "zz.kidog.oglib.chat.commands");
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPSUtils(), 1L, 1L);
        ItemUtils.load();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        new BukkitRunnable(){

            public void run() {
                if (Bukkit.getPluginManager().getPlugin("CommonLibs") != null || Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
                    ProtocolLibrary.getProtocolManager().addPacketListener(new InventoryAdapter());
                    PingAdapter ping = new PingAdapter();
                    ProtocolLibrary.getProtocolManager().addPacketListener(ping);
                    Bukkit.getPluginManager().registerEvents(ping, ogLib.getInstance());
                    new LagCheck().runTaskTimerAsynchronously(ogLib.this, 100L, 100L);
                    ProtocolLibrary.getProtocolManager().addPacketListener(new TabAdapter());
                    Bukkit.getPluginManager().registerEvents(new HologramListener(), ogLib.this);
                }
            }
        }.runTaskLater(this, 1L);
        Bukkit.getPluginManager().registerEvents(new RedstoneListener(), this);
        this.setupHourEvents();
        signGUI = new SignGUI(this);
    }

    public void onDisable() {
        if (FrozenEconomyHandler.isInitiated()) {
            FrozenEconomyHandler.saveAll();
        }
        this.localJedisPool.close();
        this.backboneJedisPool.close();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public <T> T runRedisCommand(RedisCommand<T> redisCommand) {
        if (testing) {
            return null;
        }
        Jedis jedis = this.localJedisPool.getResource();
        T result = null;
        try {
            result = redisCommand.execute(jedis);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.localRedisLastError = System.currentTimeMillis();
            if (jedis != null) {
                this.localJedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        }
        finally {
            if (jedis != null) {
                this.localJedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public <T> T runBackboneRedisCommand(RedisCommand<T> redisCommand) {
        if (testing) {
            return null;
        }
        Jedis jedis = this.backboneJedisPool.getResource();
        T result = null;
        try {
            result = redisCommand.execute(jedis);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.backboneRedisLastError = System.currentTimeMillis();
            if (jedis != null) {
                this.backboneJedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        }
        finally {
            if (jedis != null) {
                this.backboneJedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    @Deprecated
    public long getLocalRedisLastError() {
        return this.localRedisLastError;
    }

    @Deprecated
    public long getBackboneRedisLastError() {
        return this.backboneRedisLastError;
    }

    private void setupHourEvents() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("ogLib - Hour Event Thread").setDaemon(true).build());
        int minOfHour = Calendar.getInstance().get(12);
        int minToHour = 60 - minOfHour;
        int minToHalfHour = minToHour >= 30 ? minToHour : 30 - minOfHour;
        executor.scheduleAtFixedRate(() -> Bukkit.getScheduler().runTask(this, () -> Bukkit.getPluginManager().callEvent(new HourEvent(Calendar.getInstance().get(11)))), minToHour, 60L, TimeUnit.MINUTES);
        executor.scheduleAtFixedRate(() -> Bukkit.getScheduler().runTask(this, () -> Bukkit.getPluginManager().callEvent(new HalfHourEvent(Calendar.getInstance().get(11), Calendar.getInstance().get(12)))), minToHalfHour, 30L, TimeUnit.MINUTES);
    }

    public static ogLib getInstance() {
        return instance;
    }

    public JedisPool getLocalJedisPool() {
        return this.localJedisPool;
    }

    public JedisPool getBackboneJedisPool() {
        return this.backboneJedisPool;
    }


    static {
        testing = false;
        RANDOM = new Random();
        GSON = new GsonBuilder().registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter()).registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter()).registerTypeHierarchyAdapter(Location.class, new LocationAdapter()).registerTypeHierarchyAdapter(Vector.class, new VectorAdapter()).registerTypeAdapter(BlockVector.class, new BlockVectorAdapter()).setPrettyPrinting().serializeNulls().create();
        PLAIN_GSON = new GsonBuilder().registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter()).registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter()).registerTypeHierarchyAdapter(Location.class, new LocationAdapter()).registerTypeHierarchyAdapter(Vector.class, new VectorAdapter()).registerTypeAdapter(BlockVector.class, new BlockVectorAdapter()).serializeNulls().create();
    }

}

