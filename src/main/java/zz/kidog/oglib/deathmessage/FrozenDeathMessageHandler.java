package zz.kidog.oglib.deathmessage;

import com.google.common.base.Preconditions;
import zz.kidog.oglib.deathmessage.damage.Damage;
import zz.kidog.oglib.deathmessage.listener.DamageListener;
import zz.kidog.oglib.deathmessage.listener.DeathListener;
import zz.kidog.oglib.deathmessage.listener.DisconnectListener;
import zz.kidog.oglib.deathmessage.tracker.*;
import zz.kidog.oglib.deathmessage.tracker.*;
import zz.kidog.oglib.ogLib;
import lombok.NoArgsConstructor;
import net.minecraft.util.com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.*;

@NoArgsConstructor
public final class FrozenDeathMessageHandler {

    private static DeathMessageConfiguration configuration = DeathMessageConfiguration.DEFAULT_CONFIGURATION;
    private static final Map<UUID, List<Damage>> damage = new HashMap<>();
    private static boolean initiated = false;

    public static void init() {
        Preconditions.checkState(!initiated);
        initiated = true;
        PluginManager pluginManager = ogLib.getInstance().getServer().getPluginManager();
        pluginManager.registerEvents(new DamageListener(), ogLib.getInstance());
        pluginManager.registerEvents(new DeathListener(), ogLib.getInstance());
        pluginManager.registerEvents(new DisconnectListener(), ogLib.getInstance());
        pluginManager.registerEvents(new GeneralTracker(), ogLib.getInstance());
        pluginManager.registerEvents(new PvPTracker(), ogLib.getInstance());
        pluginManager.registerEvents(new EntityTracker(), ogLib.getInstance());
        pluginManager.registerEvents(new FallTracker(), ogLib.getInstance());
        pluginManager.registerEvents(new ArrowTracker(), ogLib.getInstance());
        pluginManager.registerEvents(new VoidTracker(), ogLib.getInstance());
        pluginManager.registerEvents(new BurnTracker(), ogLib.getInstance());
    }

    public static List<Damage> getDamage(Player player) {
        return damage.containsKey(player.getUniqueId()) ? damage.get(player.getUniqueId()) : ImmutableList.of();
    }

    public static void addDamage(Player player, Damage addedDamage) {
        damage.putIfAbsent(player.getUniqueId(), new ArrayList<>());
        List<Damage> damageList = damage.get(player.getUniqueId());
        while (damageList.size() > 30) {
            damageList.remove(0);
        }
        damageList.add(addedDamage);
    }

    public static void clearDamage(Player player) {
        damage.remove(player.getUniqueId());
    }

    public static DeathMessageConfiguration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(DeathMessageConfiguration configuration) {
        FrozenDeathMessageHandler.configuration = configuration;
    }
}

