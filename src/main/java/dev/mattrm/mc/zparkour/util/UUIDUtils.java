package dev.mattrm.mc.zparkour.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public final class UUIDUtils {
    public static UUID getUUIDOfOnlinePlayer(String name) {
        return Bukkit.getOnlinePlayers().stream().filter(p -> p.getName().equals(name)).map(Player::getUniqueId).findFirst().orElse(null);
    }

    public static String getNameFromUUID(UUID uuid) {
        return Arrays.stream(Bukkit.getOfflinePlayers()).filter(p -> p.getUniqueId().equals(uuid)).map(OfflinePlayer::getName).findFirst().orElse(null);
    }

    public static UUID getUUIDOfOfflinePlayer(String name) {
        return Arrays.stream(Bukkit.getOfflinePlayers()).filter(p -> p.getName().equals(name)).map(OfflinePlayer::getUniqueId).findFirst().orElse(null);
    }
}
