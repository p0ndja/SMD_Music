package me.palapon2545.music.cmd;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import me.palapon2545.music.pluginMain;
import me.palapon2545.music.api.tools.ActionBarAPI;

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
		// Toggle between mute and un-mute
		if (cmd.getName().equalsIgnoreCase("music") || cmd.getName().equalsIgnoreCase("SMDMusic:music")) {
			if (sender instanceof Player) {
				if (args.length == 0) {
					String valve = pl.getConfig().getString("Players." + player.getName() + ".music");
					if (valve != null && valve.equalsIgnoreCase("false")) {
						player.sendMessage(ChatColor.WHITE + "You " + ChatColor.GREEN + ChatColor.BOLD + "UNMUTED "
								+ ChatColor.WHITE + "Music System." + ChatColor.YELLOW + " Type " + ChatColor.GOLD
								+ ChatColor.BOLD + "/music " + ChatColor.YELLOW + "again to " + ChatColor.RED
								+ "mute.");
						ActionBarAPI.send(player, ChatColor.WHITE + "You " + ChatColor.GREEN + "UNMUTED "
								+ ChatColor.WHITE + "Music System." + ChatColor.YELLOW + " Type " + ChatColor.GOLD
								+ "/music " + ChatColor.YELLOW + "again to " + ChatColor.RED + "mute.");
						player.playSound(player.getLocation(), Sound.ENTITY_ITEMFRAME_REMOVE_ITEM, 1, 1);
						pluginMain.getMusicThread().getSongPlayer().addPlayer(player);
						pl.getConfig().set("Players." + player.getName() + ".music", "true");
						pl.saveConfig();
					} else {
						player.sendMessage(ChatColor.WHITE + "You " + ChatColor.RED + ChatColor.BOLD + "MUTED "
								+ ChatColor.WHITE + "Music System." + ChatColor.YELLOW + " Type " + ChatColor.GOLD
								+ ChatColor.BOLD + "/music " + ChatColor.YELLOW + "again to " + ChatColor.GREEN
								+ "unmute");
						ActionBarAPI.send(player, ChatColor.WHITE + "You " + ChatColor.RED + "MUTED "
								+ ChatColor.WHITE + "Music System." + ChatColor.YELLOW + " Type " + ChatColor.GOLD
								+ "/music " + ChatColor.YELLOW + "again to " + ChatColor.GREEN + "unmute.");
						player.playSound(player.getLocation(), Sound.ENTITY_ITEMFRAME_ADD_ITEM, 1, 1);
						pluginMain.getMusicThread().getSongPlayer().removePlayer(player);
						pl.getConfig().set("Players." + player.getName() + ".music", "false");
						pl.saveConfig();
					}
				}
				if (args.length != 0) {
					if (args[0].equalsIgnoreCase("mute") || args[0].equalsIgnoreCase("m")) {
						player.sendMessage(ChatColor.WHITE + "You " + ChatColor.RED + ChatColor.BOLD + "MUTED "
								+ ChatColor.WHITE + "Music System." + ChatColor.YELLOW + " Type " + ChatColor.GOLD
								+ ChatColor.BOLD + "/music " + ChatColor.YELLOW + "again to " + ChatColor.GREEN
								+ "unmute");
						ActionBarAPI.send(player, ChatColor.WHITE + "You " + ChatColor.RED + "MUTED "
								+ ChatColor.WHITE + "Music System." + ChatColor.YELLOW + " Type " + ChatColor.GOLD
								+ "/music " + ChatColor.YELLOW + "again to " + ChatColor.GREEN + "unmute.");
						player.playSound(player.getLocation(), Sound.ENTITY_ITEMFRAME_ADD_ITEM, 1, 1);
						pluginMain.getMusicThread().getSongPlayer().removePlayer(player);
						pl.getConfig().set("Players." + player.getName() + ".music", "false");
						pl.saveConfig();
					}
					if (args[0].equalsIgnoreCase("unmute") || args[0].equalsIgnoreCase("u")) {
						player.sendMessage(ChatColor.WHITE + "You " + ChatColor.GREEN + ChatColor.BOLD + "UNMUTED "
								+ ChatColor.WHITE + "Music System." + ChatColor.YELLOW + " Type " + ChatColor.GOLD
								+ ChatColor.BOLD + "/music " + ChatColor.YELLOW + "again to " + ChatColor.RED
								+ "mute.");
						ActionBarAPI.send(player, ChatColor.WHITE + "You " + ChatColor.GREEN + "UNMUTED "
								+ ChatColor.WHITE + "Music System." + ChatColor.YELLOW + " Type " + ChatColor.GOLD
								+ "/music " + ChatColor.YELLOW + "again to " + ChatColor.RED + "mute.");
						player.playSound(player.getLocation(), Sound.ENTITY_ITEMFRAME_REMOVE_ITEM, 1, 1);
						// p.playSound(p.getLocation(), Sound.<SOUND>, <VOLUME>,
						// <PITCH>);
						pluginMain.getMusicThread().getSongPlayer().addPlayer(player);
						pl.getConfig().set("Players." + player.getName() + ".music", "true");
						pl.saveConfig();
					}
					if (args[0].equalsIgnoreCase("status")) {
						if (args.length == 2) {
							String p = args[1];
							Player player2 = Bukkit.getPlayer(p);
							String playerName = player2.getName();
							if (player2.isOnline()) {
								if (pl.getConfig().getString("Players." + playerName + ".music")
										.equalsIgnoreCase("false")) {
									player.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Player "
											+ playerName + " is " + ChatColor.RED + "mute");
								}
								if (pl.getConfig().getString("Players." + playerName + ".music")
										.equalsIgnoreCase("true")) {
									player.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Player "
											+ playerName + " is " + ChatColor.GREEN + "unmute");
								}
							} else {
								player.sendMessage(playerName + " not found.");
							}
						} else if (args.length!=2) {
							player.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN + "/music status [Player]");
						}
					}
					if (args[0].equalsIgnoreCase("info")) {
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
					}
					if (!args[0].equalsIgnoreCase("info") && !args[0].equalsIgnoreCase("status")
							&& !args[0].equalsIgnoreCase("mute") && !args[0].equalsIgnoreCase("unmute")
							&& !args[0].equalsIgnoreCase("u") && !args[0].equalsIgnoreCase("m")) {
						player.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
								+ "/music [mute/unmute/info/status] [args]");
					}
				}
			}
		}
		return true;
	}
}
