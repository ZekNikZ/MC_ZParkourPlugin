package dev.mattrm.mc.zparkour.command;

import dev.mattrm.mc.zparkour.Constants;
import dev.mattrm.mc.zparkour.service.CourseService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.permission.Permission;

@Commands(@org.bukkit.plugin.java.annotation.command.Command(
    name = "zp.unpublish",
    aliases = { "unpublishcourse" },
    desc = "Unpublish a parkour/elytra course.",
    usage = "Usage: /zp.unpublish <course id>",
    permission = "zparkour.course.unpublish.mine",
    permissionMessage = "You do not permission to use this command."
))
@Permission(
    name = "zparkour.course.unpublish.mine",
    desc = "Unpublish a parkour/elytra course",
    defaultValue = PermissionDefault.TRUE
)
@Permission(
    name = "zparkour.course.unpublish.all",
    desc = "Unpublish a parkour/elytra course",
    defaultValue = PermissionDefault.OP
)
public class CommandUnpublishCourse implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        if (sender instanceof Player) {
            Player player = ((Player) sender);

            int courseId = Integer.parseInt(args[0]);

            if (!CourseService.getInstance().courseExists(courseId)) {
                sender.sendMessage(ChatColor.RED + "Error: that course does not exist.");
                return true;
            }

            if (!CourseService.getInstance().getCourseById(courseId).isPublished()) {
                sender.sendMessage(ChatColor.RED + "Error: the course is already not published.");
                return true;
            }

            if (!CourseService.getInstance().canModify(courseId, player.getUniqueId(), sender.hasPermission("zparkour.course.publish.all"))) {
                sender.sendMessage(ChatColor.RED + "Error: you do not have permission to unpublish that course.");
                return true;
            }

            CourseService.getInstance().publishCourse(courseId);
            sender.sendMessage(Constants.CHAT_PREFIX + "Course unpublished.");
        }

        return true;
    }
}
