package dev.mattrm.mc.zparkour.command;

import dev.mattrm.mc.zparkour.Constants;
import dev.mattrm.mc.zparkour.data.Course;
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
    name = "zp.publish",
    aliases = { "publishcourse" },
    desc = "Publish a parkour/elytra course.",
    usage = "Usage: /zp.publish <course id>",
    permission = "zparkour.course.publish.mine",
    permissionMessage = "You do not permission to use this command."
))
@Permission(
    name = "zparkour.course.publish.mine",
    desc = "Publish a parkour/elytra course",
    defaultValue = PermissionDefault.TRUE
)
@Permission(
    name = "zparkour.course.publish.all",
    desc = "Publish a parkour/elytra course",
    defaultValue = PermissionDefault.OP
)
public class CommandPublishCourse implements CommandExecutor {
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

            Course course = CourseService.getInstance().getCourseById(courseId);

            if (course.isPublished()) {
                sender.sendMessage(ChatColor.RED + "Error: the course is already published.");
                return true;
            }

            if (!CourseService.getInstance().canModify(courseId, player.getUniqueId(), sender.hasPermission("zparkour.course.publish.all"))) {
                sender.sendMessage(ChatColor.RED + "Error: you do not have permission to publish that course.");
                return true;
            }

            if (!course.isValidCourse()) {

            }

            CourseService.getInstance().publishCourse(courseId);
            sender.sendMessage(Constants.CHAT_PREFIX + "Course published.");
        }

        return true;
    }
}
