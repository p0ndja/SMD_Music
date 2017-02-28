package me.catallena.mcaholic.cmd;

import java.util.Random;

import javax.persistence.Entity;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import me.catallena.mcaholic.pluginMain;

public class playerCommands implements CommandExecutor {

	pluginMain pl;

	public playerCommands(pluginMain pl) {
		this.pl = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		String message = "";
		for (String part : args) {
			if (message != "")
				message += " ";
			message += part;
		}
		// Toggle between mute and un-mute
		if (cmd.getName().equalsIgnoreCase("music")) {
			if (sender instanceof Player) {
				if (args.length == 0) {
					Player p = (Player) sender;
					if (pl.getConfig().getString("Players." + p.getName() + ".music").equalsIgnoreCase("false")) {
						p.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "You have" + ChatColor.GREEN + " Unmute Music!");
						p.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Type '/music' again to " + ChatColor.RED + "Mute.");
						p.playSound(p.getLocation(), Sound.ENTITY_ITEMFRAME_REMOVE_ITEM, 1, 1);
						// p.playSound(p.getLocation(), Sound.<SOUND>, <VOLUME>,
						// <PITCH>);
						pluginMain.getMusicThread().getSongPlayer().addPlayer(p);
						pl.getConfig().set("Players." + p.getName() + ".music", "true");
						pl.saveConfig();
					} else {
						if (pl.getConfig().getString("Players." + p.getName() + ".music").equalsIgnoreCase("true")) {
							p.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "You have" + ChatColor.RED + " Mute Music!");
							p.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Type '/music' again to " + ChatColor.GREEN + "Unmute.");
							p.playSound(p.getLocation(), Sound.ENTITY_ITEMFRAME_ADD_ITEM, 1, 1);
							pluginMain.getMusicThread().getSongPlayer().removePlayer(p);
							pl.getConfig().set("Players." + p.getName() + ".music", "false");
							pl.saveConfig();
						}
					}
				}
				if (args.length != 0) {
					if (args[0].equalsIgnoreCase("status")) {
						Player p = Bukkit.getPlayer(args[1]);
						String playerName = p.getName();
						Player player = (Player) sender;
						if (p.isOnline()) {
							if (pl.getConfig().getString("Players." + playerName + ".music").equalsIgnoreCase("false")) {
								player.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Player " + playerName + " is " + ChatColor.RED + "mute");
							}
							if (pl.getConfig().getString("Players." + playerName + ".music").equalsIgnoreCase("true")) {
								player.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Player " + playerName + " is " + ChatColor.GREEN + "unmute");
							}
						} else {
							player.sendMessage(playerName + " not found.");
						}
					}
				}
			}
		}
		return true;
	}
}
