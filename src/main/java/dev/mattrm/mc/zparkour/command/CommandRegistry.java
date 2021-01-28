package dev.mattrm.mc.zparkour.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandRegistry {
    public static void registerCommand(JavaPlugin plugin, String command, CommandExecutor executor) {
        plugin.getCommand(command).setExecutor(executor);
    }

    public static void registerAutocompleteCommand(JavaPlugin plugin, String command, TabExecutor executor) {
        registerCommand(plugin, command, executor);
        plugin.getCommand(command).setTabCompleter(executor);
    }

    public static void registerCommands(JavaPlugin plugin) {
        registerCommand(plugin, "zp.create", new CommandCreateCourse());
        registerCommand(plugin, "zp.remove", new CommandRemoveCourse());
        registerCommand(plugin, "zp.listcourses", new CommandListCourses());
        registerCommand(plugin, "zp.mycourses", new CommandMyCourses());
        registerCommand(plugin, "zp.triggerpos1", new CommandTriggerPos(1));
        registerCommand(plugin, "zp.triggerpos2", new CommandTriggerPos(2));
        registerCommand(plugin, "zp.listtriggers", new CommandListTriggers());
        registerCommand(plugin, "zp.createtrigger", new CommandCreateTrigger(plugin.getConfig().getInt("max-trigger-region-width")));
        registerAutocompleteCommand(plugin, "zp.removetrigger", new CommandRemoveTrigger());
        registerCommand(plugin, "zp.publish", new CommandPublishCourse());
        registerCommand(plugin, "zp.unpublish", new CommandUnpublishCourse());
    }
}
