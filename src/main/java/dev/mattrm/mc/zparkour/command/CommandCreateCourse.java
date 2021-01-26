package dev.mattrm.mc.zparkour.command;

import dev.mattrm.mc.zparkour.Constants;
import dev.mattrm.mc.zparkour.service.CourseService;
import dev.mattrm.mc.zparkour.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.permission.Permission;

@Commands(@org.bukkit.plugin.java.annotation.command.Command(
        name = "zp.create",
        aliases = {"createcourse"},
        desc = "Create a parkour/elytra course.",
        usage = "Usage: /zp.create <course name>",
        permission = "zparkour.course.create",
        permissionMessage = "You do not permission to use this command."
))
@Permission(
        name = "zparkour.course.create",
        desc = "Create a parkour/elytra course",
        defaultValue = PermissionDefault.TRUE
)
public class CommandCreateCourse implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        String name = String.join(" ", args);
        int courseId = CourseService.getInstance().createCourse(name, UUIDUtils.getUUIDOfOnlinePlayer(sender.getName()));

        sender.sendMessage(Constants.CHAT_PREFIX + "Successfully created new course \"" + ChatColor.YELLOW + name + ChatColor.RESET + "\" with course ID #" + ChatColor.YELLOW + courseId);

        return true;
    }
}
