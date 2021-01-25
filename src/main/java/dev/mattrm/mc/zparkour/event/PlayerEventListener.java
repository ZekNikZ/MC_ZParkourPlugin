package dev.mattrm.mc.zparkour.event;

import dev.mattrm.mc.zparkour.service.TriggerService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerEventListener implements Listener {
    private final JavaPlugin plugin;

    public PlayerEventListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

//    @EventHandler
//    public void onPlayerInteract(PlayerInteractEvent event) {
//        if (event.getAction() == Action.PHYSICAL) {
//            Bukkit.broadcastMessage(event.getPlayer().getLocation().getBlock().getBlockData().getAsString());
//        }
//    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (this.plugin.getConfig().getStringList("trigger-blocks").contains(event.getTo().getBlock().getType().getKey().toString())){
            TriggerService.getInstance().onPlayerInTriggerRegion(event.getPlayer(), event.getTo());
        }
    }
}
