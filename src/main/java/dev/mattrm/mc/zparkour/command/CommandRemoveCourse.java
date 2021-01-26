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

@Commands(@org.bukkit.plugin.java.annotation.command.Command(
    name = "zp.remove",
    aliases = { "zp.delete", "removecourse", "deletecourse" },
    desc = "Remove a parkour/elytra course.",
    usage = "Usage: /zp.remove <course id>",
    permission = "zparkour.course.delete.mine",
    permissionMessage = "You do not permission to use this command."
))
@Permission(
    name = "zparkour.course.delete.mine",
    desc = "Delete a parkour/elytra course",
    defaultValue = PermissionDefault.TRUE
)
@Permission(
    name = "zparkour.course.delete.all",
    desc = "Delete a parkour/elytra course",
    defaultValue = PermissionDefault.OP
)
public class CommandRemoveCourse implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length >= 3) {
            return false;
        }

        if (sender instanceof Player) {
            Player player = ((Player) sender);

            int courseId = Integer.parseInt(args[0]);

            if (!CourseService.getInstance().courseExists(courseId)) {
                sender.sendMessage(ChatColor.RED + "Error: that course does not exist.");
                return true;
            }

            if (CourseService.getInstance().getCourseById(courseId).isPublished()) {
                sender.sendMessage(ChatColor.RED + "Error: the course is published. Un-publish it to modify it.");
                return true;
            }

            if (!CourseService.getInstance().canModify(courseId, player.getUniqueId(), sender.hasPermission("zparkour.course.delete.all"))) {
                sender.sendMessage(ChatColor.RED + "Error: you do not have permission to delete that course.");
                return true;
            }

            if (args.length == 2) {
                if (!args[1].trim().toLowerCase().equals("confirm")) {
                    return false;
                }

                CourseService.getInstance().deleteCourse(courseId);
            } else {
                sender.sendMessage(Constants.CHAT_PREFIX + "Type " + ChatColor.YELLOW + "/" + label + " " + courseId + " confirm" + ChatColor.RESET + " to confirm deletion.");
            }
        }

        return true;
    }
}
