package me.daddychurchill.WellWorld.Support;

import me.daddychurchill.WellWorld.WellWorld;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WellWorldCreateCMD implements CommandExecutor {
    private final WellWorld plugin;

    public WellWorldCreateCMD(WellWorld plugin)
    {
        this.plugin = plugin;
    }

	public boolean onCommand(CommandSender sender, Command command, String label, String[] split) 
    {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("wellworld.command")) {
				player.sendMessage("Loading/creating WellWorld... This might take a moment...");
				player.teleport(plugin.getWellWorld().getSpawnLocation());
				return true;
			} else {
				sender.sendMessage("You do not have permission to use this command");
				return false;
			}
		} else {
			sender.sendMessage("This command is only usable by a player");
			return false;
		}
    }
}
