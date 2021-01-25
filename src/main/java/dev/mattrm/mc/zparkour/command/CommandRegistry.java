package dev.mattrm.mc.zparkour.command;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandRegistry {
    public static void registerCommands(JavaPlugin plugin) {
        plugin.getCommand("zp.create").setExecutor(new CommandCreateCourse());
        plugin.getCommand("zp.listcourses").setExecutor(new CommandListCourses());
        plugin.getCommand("zp.mycourses").setExecutor(new CommandMyCourses());
        plugin.getCommand("zp.triggerpos1").setExecutor(new CommandTriggerPos(1));
        plugin.getCommand("zp.triggerpos2").setExecutor(new CommandTriggerPos(2));
        plugin.getCommand("zp.listtriggers").setExecutor(new CommandListTriggers());
        plugin.getCommand("zp.createtrigger").setExecutor(new CommandCreateTrigger(plugin.getConfig().getInt("max-trigger-region-width")));
    }
}
