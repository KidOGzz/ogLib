package zz.kidog.oglib.uuid.impl;

import java.util.UUID;

import zz.kidog.oglib.ogLib;
import zz.kidog.oglib.uuid.UUIDCache;

public final class BukkitUUIDCache implements UUIDCache {

    @Override
    public UUID uuid(String name) {
        return ogLib.getInstance().getServer().getOfflinePlayer(name).getUniqueId();
    }

    @Override
    public String name(UUID uuid) {
        return ogLib.getInstance().getServer().getOfflinePlayer(uuid).getName();
    }

    @Override
    public void ensure(UUID uuid) {
    }

    @Override
    public void update(UUID uuid, String name) {
    }
}

