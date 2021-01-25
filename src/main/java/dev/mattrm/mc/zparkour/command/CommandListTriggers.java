package dev.mattrm.mc.zparkour.command;

import dev.mattrm.mc.zparkour.Constants;
import dev.mattrm.mc.zparkour.data.Course;
import dev.mattrm.mc.zparkour.service.CourseService;
import dev.mattrm.mc.zparkour.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.permission.Permission;

import java.util.UUID;
import java.util.stream.IntStream;

@Commands(@org.bukkit.plugin.java.annotation.command.Command(
        name = "zp.listtriggers",
        aliases = {"listtriggers", "triggers"},
        desc = "Lists all current triggers for a course.",
        usage = "Usage: /zp.listtriggers <courseId>",
        permission = "zparkour.trigger.list.mine",
        permissionMessage = "You do not permission to use this command."
))
@Permission(
        name = "zparkour.trigger.list.mine",
        desc = "Lists all current triggers for a course.",
        defaultValue = PermissionDefault.OP
)
@Permission(
        name = "zparkour.trigger.list.all",
        desc = "Lists all current triggers for a course.",
        defaultValue = PermissionDefault.OP
)
public class CommandListTriggers implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                int courseId = Integer.parseInt(args[0]);

                if (!CourseService.getInstance().courseExists(courseId)) {
                    sender.sendMessage(ChatColor.RED + "Error: course with that ID does not exist.");
                    return true;
                }

                if (!sender.hasPermission("zparkour.trigger.list.all") && (!sender.hasPermission("zparkour.trigger.list.mine") || (sender.hasPermission("zparkour.trigger.list.mine") && CourseService.getInstance().canModify(courseId, player.getUniqueId(), false)))) {
                    sender.sendMessage(ChatColor.RED + "Error: you do not have permission to list triggers for that course.");
                    return true;
                }

                sender.sendMessage(Constants.CHAT_PREFIX + "List of all triggers for course " + ChatColor.YELLOW + courseId + ChatColor.RESET + ":");

                CourseService.getInstance().getCourseById(courseId).getTriggers()
                        .forEach(trigger -> sender.sendMessage("   [" + ChatColor.AQUA + trigger.getSectionId() + "] " + ChatColor.YELLOW + trigger.getType() + ChatColor.RESET + " by (" + ChatColor.YELLOW
                                + trigger.getX1() + ChatColor.RESET + "," + ChatColor.YELLOW + trigger.getY1() + ChatColor.RESET + "," + ChatColor.YELLOW + trigger.getZ1() + ChatColor.RESET + ") to (" + ChatColor.YELLOW
                                + trigger.getX2() + ChatColor.RESET + "," + ChatColor.YELLOW + trigger.getY2() + ChatColor.RESET + "," + ChatColor.YELLOW + trigger.getZ2() + ChatColor.RESET + ")"));

                return true;
            } else {
                return false;
            }
        }

        return true;
    }
}
