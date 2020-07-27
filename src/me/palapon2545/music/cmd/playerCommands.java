package me.palapon2545.music.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import me.palapon2545.music.pluginMain;

public class playerCommands implements CommandExecutor {

	pluginMain pl;

	public playerCommands(pluginMain pl) {
		this.pl = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		Player player = (Player) sender;
		String message = "";
		for (String part : args) {
			if (message != "")
				message += " ";
			message += part;
		}
		if (cmd.getName().equalsIgnoreCase("music") || cmd.getName().equalsIgnoreCase("SMDMusic:music")) {
			if (sender instanceof Player) {
				if (args.length == 0) {
					String valve = pl.getConfig().getString("Players." + player.getName() + ".music");
					String message1 = "";
					String message2 = "";
					if (valve != null && valve.equalsIgnoreCase("false")) {
						// SWTICH TO TRUE
						pl.getConfig().set("Players." + player.getName() + ".music", "true");
						pluginMain.getMusicThread().getSongPlayer().addPlayer(player);
						message1 = ChatColor.GREEN + "" + ChatColor.BOLD + "UNMUTED";
						message2 = ChatColor.RED + "" + ChatColor.BOLD + "MUTED";
					} else {
						// SWTICH TO FALSE
						pl.getConfig().set("Players." + player.getName() + ".music", "false");
						pluginMain.getMusicThread().getSongPlayer().removePlayer(player);
						message1 = ChatColor.RED + "" + ChatColor.BOLD + "MUTED";
						message2 = ChatColor.GREEN + "" + ChatColor.BOLD + "UNMUTED";
					}

					pl.saveConfig();
					player.sendMessage("You " + message1 + ChatColor.WHITE + " noteblock song.");
					player.sendMessage("Type " + ChatColor.YELLOW + "/music " + ChatColor.WHITE + "to " + message2
							+ ChatColor.WHITE + ".");
				} else if (args.length != 0) {
					if (args[0].equalsIgnoreCase("mute") || args[0].equalsIgnoreCase("m")) {
						pl.getConfig().set("Players." + player.getName() + ".music", "false");
						pluginMain.getMusicThread().getSongPlayer().removePlayer(player);
						player.sendMessage("You " + ChatColor.RED + ChatColor.BOLD + "MUTED " + ChatColor.WHITE
								+ "noteblock song.");
						player.sendMessage("Type " + ChatColor.YELLOW + "/music " + ChatColor.WHITE + "to "
								+ ChatColor.GREEN + ChatColor.BOLD + "UNMUTED" + ChatColor.WHITE + ".");
					} else if (args[0].equalsIgnoreCase("unmute") || args[0].equalsIgnoreCase("u")) {
						pl.getConfig().set("Players." + player.getName() + ".music", "true");
						pluginMain.getMusicThread().getSongPlayer().removePlayer(player);
						player.sendMessage("You " + ChatColor.GREEN + ChatColor.BOLD + "UNMUTED " + ChatColor.WHITE
								+ "noteblock song.");
						player.sendMessage("Type " + ChatColor.YELLOW + "/music " + ChatColor.WHITE + "to "
								+ ChatColor.RED + ChatColor.BOLD + "MUTED" + ChatColor.WHITE + ".");
					} else if (args[0].equalsIgnoreCase("status")) {
						String targetPlayer = player.getName();
						if (args.length >= 2 && !args[2].isEmpty()) {
							if (Bukkit.getPlayer(args[2]) != null) {
								targetPlayer = Bukkit.getPlayer(args[2]).getName();
							}
						}

						String musicBoolean = pl.getConfig().getString("Players." + targetPlayer + ".music");
						player.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Player " + targetPlayer
								+ " is " + musicBoolean + ".");
					} else if (args[0].equalsIgnoreCase("info")) {
						String title = pluginMain.getMusicThread().getCurrentSong().getTitle();
						String author = pluginMain.getMusicThread().getCurrentSong().getAuthor();
						String description = pluginMain.getMusicThread().getCurrentSong().getDescription();
						if (title.isEmpty()) {
							title = ChatColor.GRAY + "" + ChatColor.ITALIC + "Unknown Song" + ChatColor.RESET;
						}
						if (author.isEmpty()) {
							author = ChatColor.GRAY + "" + ChatColor.ITALIC + "Unknown Author" + ChatColor.RESET;
						}
						if (description.isEmpty()) {
							description = ChatColor.GRAY + "" + ChatColor.ITALIC + "No Description" + ChatColor.RESET;
						}
						player.sendMessage(ChatColor.STRIKETHROUGH + "----------------------------");
						player.sendMessage(ChatColor.WHITE + "@~" + ChatColor.YELLOW + ChatColor.BOLD + "Music "
								+ ChatColor.GOLD + ChatColor.BOLD + "Information" + ChatColor.WHITE + "~@");
						player.sendMessage(ChatColor.DARK_GREEN + "Name: " + ChatColor.GREEN + title);
						player.sendMessage(ChatColor.DARK_GREEN + "Author: " + ChatColor.GREEN + author);
						player.sendMessage(ChatColor.DARK_GREEN + "Description:");
						player.sendMessage(ChatColor.GREEN + description);
						player.sendMessage(ChatColor.STRIKETHROUGH + "----------------------------");
					} else {
						player.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
								+ "/music [mute/unmute/info/status] [args]");
					}
				}
			}
		}
		return true;
	}
}
