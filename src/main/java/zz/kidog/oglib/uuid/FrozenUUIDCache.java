package zz.kidog.oglib.uuid;

import com.google.common.base.Preconditions;
import java.util.UUID;

import zz.kidog.oglib.ogLib;
import lombok.Getter;
import zz.kidog.oglib.uuid.impl.RedisUUIDCache;

public final class FrozenUUIDCache {

    @Getter private static UUIDCache impl = null;
    private static boolean initiated = false;

    private FrozenUUIDCache() {
    }

    public static void init() {
        Preconditions.checkState(!initiated);
        initiated = true;
        try {
            impl = (UUIDCache) new RedisUUIDCache();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ogLib.getInstance().getServer().getPluginManager().registerEvents(new UUIDListener(), ogLib.getInstance());
    }

    public static UUID uuid(String name) {
        return impl.uuid(name);
    }

    public static String name(UUID uuid) {
        return impl.name(uuid);
    }

    public static void ensure(UUID uuid) {
        impl.ensure(uuid);
    }

    public static void update(UUID uuid, String name) {
        impl.update(uuid, name);
    }

}

