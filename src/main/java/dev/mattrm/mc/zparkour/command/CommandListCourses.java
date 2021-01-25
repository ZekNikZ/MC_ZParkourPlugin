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
        name = "zp.listcourses",
        aliases = {"listcourses", "courses"},
        desc = "Lists all current ZParkour courses.",
        usage = "Usage: /zp.listcourses [player]",
        permission = "zparkour.list.all",
        permissionMessage = "You do not permission to use this command."
))
@Permission(
        name = "zparkour.list.all",
        desc = "Lists all current ZParkour courses.",
        defaultValue = PermissionDefault.OP
)
public class CommandListCourses implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return listAllCurrentCourses(sender);
        } else if (args.length == 1) {
            return listCurrentCoursesOfPlayer(sender, args[0]);
        } else {
            return false;
        }
    }

    public static boolean listCurrentCoursesOfPlayer(CommandSender sender, String playerName) {
        UUID playerUUID = UUIDUtils.getUUIDOfOfflinePlayer(playerName);

        if (playerUUID == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return false;
        }

        sender.sendMessage(Constants.CHAT_PREFIX + "List of all current ZParkour courses for player " + ChatColor.YELLOW + playerName + ChatColor.RESET + ":");

        CourseService.getInstance().getCourses().stream()
                .filter(c -> c.getOwner().equals(playerUUID))
                .forEach(course -> sender.sendMessage("   [" + ChatColor.AQUA + course.getId() + "] " + ChatColor.YELLOW + course.getName() + ChatColor.RESET + " by " + ChatColor.YELLOW + playerName));

        return true;
    }

    public static boolean listAllCurrentCourses(CommandSender sender) {
        sender.sendMessage(Constants.CHAT_PREFIX + "List of all current ZParkour courses:");

        for (Course course : CourseService.getInstance().getCourses()) {
            sender.sendMessage("   [" + ChatColor.AQUA + course.getId() + "] " + ChatColor.YELLOW + course.getName() + ChatColor.RESET + " by " + ChatColor.YELLOW + UUIDUtils.getNameFromUUID(course.getOwner()));
        }

        return true;
    }
}
