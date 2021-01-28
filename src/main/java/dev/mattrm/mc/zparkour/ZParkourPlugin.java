package dev.mattrm.mc.zparkour;

import dev.mattrm.mc.zparkour.command.CommandRegistry;
import dev.mattrm.mc.zparkour.event.PlayerEventListener;
import dev.mattrm.mc.zparkour.service.CourseService;
import dev.mattrm.mc.zparkour.service.ParkourSessionService;
import dev.mattrm.mc.zparkour.service.TriggerService;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

@Plugin(name="ZParkour", version="1.16.5.0")
@Author("ZekNikZ")
@ApiVersion(ApiVersion.Target.v1_15)
@Description("Allows easy creation of parkour/elytra courses in a survival world.")
public class ZParkourPlugin extends JavaPlugin {
    private final PlayerEventListener playerEventListener = new PlayerEventListener(this);

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(playerEventListener, this);

        this.saveDefaultConfig();

        CommandRegistry.registerCommands(this);

        TriggerService.initialize(this);
        CourseService.initialize(this);
        ParkourSessionService.initialize(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
