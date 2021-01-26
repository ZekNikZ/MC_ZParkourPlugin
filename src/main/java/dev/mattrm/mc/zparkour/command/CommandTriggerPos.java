package dev.mattrm.mc.zparkour.command;

import dev.mattrm.mc.zparkour.Constants;
import dev.mattrm.mc.zparkour.service.CourseService;
import dev.mattrm.mc.zparkour.service.TriggerService;
import dev.mattrm.mc.zparkour.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.permission.Permission;

@Commands({
        @org.bukkit.plugin.java.annotation.command.Command(
                name = "zp.triggerpos1",
                aliases = {"triggerpos1", "tp1"},
                desc = "Choose the first corner of a trigger region.",
                usage = "Usage: /zp.triggerpos1 (be looking at a block)",
                permission = "zparkour.trigger.create",
                permissionMessage = "You do not permission to use this command."
        ),
        @org.bukkit.plugin.java.annotation.command.Command(
                name = "zp.triggerpos2",
                aliases = {"triggerpos2", "tp2"},
                desc = "Choose the second corner of a trigger region.",
                usage = "Usage: /zp.triggerpos2 (be looking at a block)",
                permission = "zparkour.trigger.create",
                permissionMessage = "You do not permission to use this command."
        )})
@Permission(
        name = "zparkour.trigger.create",
        desc = "Create ZParkour triggers",
        defaultValue = PermissionDefault.TRUE
)
public class CommandTriggerPos implements CommandExecutor {
    private final int corner;

    public CommandTriggerPos(int corner) {
        this.corner = corner;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Block block = player.getTargetBlock(null, 100);

            if (block.getType().isAir()) {
                return false;
            }

            TriggerService.getInstance().setPlayerCorner(player.getUniqueId(), this.corner, block.getLocation());

            sender.sendMessage(Constants.CHAT_PREFIX + "Trigger region position " + ChatColor.AQUA + this.corner + ChatColor.RESET + " set to (" + ChatColor.YELLOW + block.getLocation().getBlockX() + ChatColor.RESET + ","  + ChatColor.YELLOW + block.getLocation().getBlockY() + ChatColor.RESET + ","  + ChatColor.YELLOW + block.getLocation().getBlockZ() + ChatColor.RESET + ")");

            return true;
        }

        return true;
    }
}
