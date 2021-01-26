package dev.mattrm.mc.zparkour.command;

import dev.mattrm.mc.zparkour.Constants;
import dev.mattrm.mc.zparkour.data.Trigger;
import dev.mattrm.mc.zparkour.data.TriggerType;
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
        name = "zp.createtrigger",
        aliases = {"createtrigger"},
        desc = "Create a trigger region. Requires you to set the trigger region corners. When creating a section checkpoint, the section number starts at 1 and will push all later sections back.",
        usage = "Usage: /zp.createtrigger <course id> <start|end|checkpoint [section num]>",
        permission = "zparkour.trigger.create.mine",
        permissionMessage = "You do not permission to use this command."
))
@Permission(
        name = "zparkour.trigger.create.mine",
        desc = "Create a trigger",
        defaultValue = PermissionDefault.TRUE
)
@Permission(
        name = "zparkour.trigger.create.all",
        desc = "Create a trigger",
        defaultValue = PermissionDefault.OP
)
public class CommandCreateTrigger implements CommandExecutor {
    private final int maxSize;

    public CommandCreateTrigger(int maxSize) {
        this.maxSize = maxSize;
    }

    // TODO: check if trigger positions have been set
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 && args.length != 3) {
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;

            int courseId = Integer.parseInt(args[0]);

            if (!CourseService.getInstance().courseExists(courseId)) {
                sender.sendMessage(ChatColor.RED + "Error: course with that ID does not exist.");
                return true;
            }

            if (CourseService.getInstance().getCourseById(courseId).isPublished()) {
                sender.sendMessage(ChatColor.RED + "Error: the course is published. Un-publish it to modify it.");
                return true;
            }

            if (!CourseService.getInstance().canModify(courseId, player.getUniqueId(), sender.hasPermission("zparkour.trigger.create.all"))) {
                sender.sendMessage(ChatColor.RED + "Error: you do not have permission to modify that course.");
                return true;
            }

            TriggerType type = TriggerType.from(args[1]);

            if (type == null) {
                sender.sendMessage(ChatColor.RED + "Error: invalid trigger type.");
                return false;
            }

            if (type == TriggerType.SECTION_END && args.length == 2) {
                return false;
            } else if (type != TriggerType.SECTION_END && args.length == 3) {
                return false;
            }

            int sectionNum = 0;

            if (type == TriggerType.SECTION_END) {
                sectionNum = Integer.parseInt(args[2]);
            }

            if (!TriggerService.getInstance().canCreateTrigger(player.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "Error: use /zp.triggerpos1 and /zp.triggerpos2 to set the corners of the trigger.");
                return true;
            }

            Trigger trigger = TriggerService.getInstance().createTrigger(player.getUniqueId(), courseId, type, sectionNum);

            if (trigger == null) {
                sender.sendMessage(ChatColor.RED + "Error: trigger region is too large. Max dimension length is " + this.maxSize + ".");
                return true;
            }

            sender.sendMessage(Constants.CHAT_PREFIX + "Successfully created new trigger for course " + ChatColor.YELLOW + courseId + ChatColor.RESET + ".");
        }

        return true;
    }
}
