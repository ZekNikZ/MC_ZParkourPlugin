package dev.mattrm.mc.zparkour.command;

import dev.mattrm.mc.zparkour.Constants;
import dev.mattrm.mc.zparkour.data.Course;
import dev.mattrm.mc.zparkour.service.CourseService;
import dev.mattrm.mc.zparkour.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.permission.Permission;

import java.util.UUID;

@Commands(@org.bukkit.plugin.java.annotation.command.Command(
        name = "zp.mycourses",
        aliases = {"mycourses"},
        desc = "Lists your current ZParkour courses.",
        usage = "Usage: /zp.mycourses",
        permission = "zparkour.course.list.mine",
        permissionMessage = "You do not permission to use this command."
))
@Permission(
        name = "zparkour.course.list.mine",
        desc = "Lists your current ZParkour courses.",
        defaultValue = PermissionDefault.TRUE
)
public class CommandMyCourses implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return CommandListCourses.listCurrentCoursesOfPlayer(sender, sender.getName());
    }
}
