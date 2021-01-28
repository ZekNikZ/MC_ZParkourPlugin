package dev.mattrm.mc.zparkour.command;

import com.google.common.collect.Lists;
import dev.mattrm.mc.zparkour.Constants;
import dev.mattrm.mc.zparkour.data.Course;
import dev.mattrm.mc.zparkour.service.CourseService;
import dev.mattrm.mc.zparkour.service.TriggerService;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.permission.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
public class CommandRemoveTrigger implements TabExecutor {
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return Lists.newArrayList();
        }

        Player player = ((Player) sender);

        if (args.length == 1) {
            return CourseService.getInstance().getCourses().stream().filter(c -> c.getOwner() == player.getUniqueId()).map(Course::getId).map(id -> "" + id).collect(Collectors.toList());
        } else if (args.length == 2) {
            int courseId = -1;
            try {
                courseId = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
            }

            if (!CourseService.getInstance().courseExists(courseId)) {
                return Lists.newArrayList();
            }

            return IntStream.range(0, CourseService.getInstance().getCourseById(courseId).getTriggers().size()).mapToObj(id -> "" + id).collect(Collectors.toList());
        }

        return Lists.newArrayList();
    }
}
