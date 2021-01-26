package dev.mattrm.mc.zparkour.command;

import dev.mattrm.mc.zparkour.Constants;
import dev.mattrm.mc.zparkour.service.CourseService;
import dev.mattrm.mc.zparkour.service.TriggerService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.permission.Permission;

@Commands(@org.bukkit.plugin.java.annotation.command.Command(
    name = "zp.removetrigger",
    aliases = { "zp.deletetrigger", "removetrigger", "deletetrigger" },
    desc = "Remove a ZParkour trigger.",
    usage = "Usage: /zp.removetrigger <course id> <trigger id>",
    permission = "zparkour.trigger.delete.mine",
    permissionMessage = "You do not permission to use this command."
))
@Permission(
    name = "zparkour.trigger.delete.mine",
    desc = "Remove a ZParkour trigger.",
    defaultValue = PermissionDefault.TRUE
)
@Permission(
    name = "zparkour.trigger.delete.all",
    desc = "Remove a ZParkour trigger.",
    defaultValue = PermissionDefault.OP
)
public class CommandRemoveTrigger implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return false;
        }

        if (sender instanceof Player) {
            Player player = ((Player) sender);

            int courseId = Integer.parseInt(args[0]);
            int triggerId = Integer.parseInt(args[1]);

            if (!CourseService.getInstance().courseExists(courseId)) {
                sender.sendMessage(ChatColor.RED + "Error: that course does not exist.");
                return true;
            }

            if (CourseService.getInstance().getCourseById(courseId).isPublished()) {
                sender.sendMessage(ChatColor.RED + "Error: the course is published. Un-publish it to modify it.");
                return true;
            }

            if (!CourseService.getInstance().canModify(courseId, player.getUniqueId(), sender.hasPermission("zparkour.trigger.delete.all"))) {
                sender.sendMessage(ChatColor.RED + "Error: you do not have permission to modify that course.");
                return true;
            }

            if (!TriggerService.getInstance().deleteIfExists(courseId, triggerId)) {
                sender.sendMessage(ChatColor.RED + "Error: that trigger does not exist.");
            }
        }

        return true;
    }
}
