package me.palapon2545.music.cmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.palapon2545.music.pluginMain;
import me.palapon2545.music.NoteblockAPI.Song;
import me.palapon2545.music.api.MusicThread;

public class adminCommands implements CommandExecutor {

	MusicThread musicThread;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		String message = "";
		for (String part : args) {
			if (message != "")
				message += " ";
			message += part;
		}

		if (cmd.getName().equalsIgnoreCase("musicadmin")) {
			if (sender.isOp() || sender.hasPermission("music.admin")) {
				if (args.length == 0) {
					Player p = (Player) sender;
					sender.sendMessage(ChatColor.STRIKETHROUGH + "-----------------" + ChatColor.GOLD + "["
							+ ChatColor.YELLOW + "Music" + ChatColor.GOLD + "]" + ChatColor.WHITE
							+ ChatColor.STRIKETHROUGH + "-----------------");
					sender.sendMessage(ChatColor.YELLOW + "'/musicadmin random'" + ChatColor.GOLD + " Random new song");
					sender.sendMessage(
							ChatColor.YELLOW + "'/musicadmin play [Song]'" + ChatColor.GOLD + " Play specific song");
					sender.sendMessage(ChatColor.YELLOW + "'/musicadmin list'" + ChatColor.GOLD
							+ " List of song(s) that available");
					sender.sendMessage(ChatColor.YELLOW + "'/musicadmin forcemute'" + ChatColor.GOLD
							+ " Force target player to mute song");
					sender.sendMessage(ChatColor.YELLOW + "'/musicadmin forceunmute'" + ChatColor.GOLD
							+ " Force target player to unmute song");
					sender.sendMessage(ChatColor.YELLOW + "'/musicadmin forceallmute'" + ChatColor.GOLD
							+ " Force all online player to mute song");
					sender.sendMessage(ChatColor.YELLOW + "'/musicadmin forceallunmute'" + ChatColor.GOLD
							+ " Force all online player to unmute song");
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
				} else {
					if (args[0].equalsIgnoreCase("play")) {
						if (args.length < 1) {
							sender.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Type:" + ChatColor.GREEN
									+ "/musicadmin play [Song]");
						} else {
							for (Player p : Bukkit.getOnlinePlayers()) {
								pluginMain.getMusicThread().getSongPlayer().removePlayer(p);
							}
							message = "";
							for (int i = 1; i != args.length; i++) {
								if (i == (args.length - 1)) {
									message += args[i];
								} else {
									message += args[i] + " ";
								}
							}
							pluginMain.getMusicThread().trySetSong(message);
						}
					}
					if (args[0].equalsIgnoreCase("random")) {
						for (Player p : Bukkit.getOnlinePlayers()) {
							pluginMain.getMusicThread().getSongPlayer().removePlayer(p);
						}
						pluginMain.getMusicThread().randomSong();
					}
					if (args[0].equalsIgnoreCase("reload")) {
						Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("SMDMusic");
						for (Player p : Bukkit.getOnlinePlayers()) {
							p.performCommand("music m");
							pluginMain.getMusicThread().getSongPlayer().removePlayer(p);
						}
						pluginMain.getMusicThread().getSongPlayer().setPlaying(false);
						plugin.onDisable();
						Bukkit.getServer().getScheduler().cancelTasks(plugin);
						plugin.onEnable();

					}
					if (args[0].equalsIgnoreCase("forcemute")) {
						if (args.length == 2) {
							if (Bukkit.getServer().getPlayer(args[1]) != null) {
								Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
								String targetPlayerName = targetPlayer.getName();
								targetPlayer.performCommand("music mute");
								sender.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "You forced "
										+ ChatColor.YELLOW + targetPlayerName + ChatColor.GRAY + " to mute");
							} else {
								sender.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Player "
										+ ChatColor.YELLOW + args[1] + ChatColor.GRAY + "not found.");
							}
						} else {
							sender.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Type: " + ChatColor.YELLOW
									+ "/musicadmin forcemute [player]");
						}
					}
					if (args[0].equalsIgnoreCase("forceunmute")) {
						if (args.length == 2) {
							if (Bukkit.getServer().getPlayer(args[1]) != null) {
								Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
								String targetPlayerName = targetPlayer.getName();
								targetPlayer.performCommand("music unmute");
								sender.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "You forced "
										+ ChatColor.YELLOW + targetPlayerName + ChatColor.GRAY + " to unmute");
							} else {
								sender.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Player "
										+ ChatColor.YELLOW + args[1] + ChatColor.GRAY + "not found.");
							}
						} else {
							sender.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "Type: " + ChatColor.YELLOW
									+ "/musicadmin forceunmute [player]");
						}
					}
					if (args[0].equalsIgnoreCase("forceallmute")) {
						sender.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "You forced "
								+ ChatColor.YELLOW + "all online player" + ChatColor.GRAY + " to mute");
						for (Player p : Bukkit.getOnlinePlayers()) {
							p.performCommand("music mute");
						}
					}
					if (args[0].equalsIgnoreCase("forceallunmute")) {
						sender.sendMessage(ChatColor.BLUE + "Music> " + ChatColor.GRAY + "You forced "
								+ ChatColor.YELLOW + "all online player" + ChatColor.GRAY + " to unmute");
						for (Player p : Bukkit.getOnlinePlayers()) {
							p.performCommand("music unmute");
						}
					}
					if (args[0].equalsIgnoreCase("list")) {
						StringBuffer buf = new StringBuffer();
						Player p = (Player) sender;
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
						buf.append(ChatColor.GOLD + "" + ChatColor.BOLD + "Loaded songs : ");
						Song[] songs = pluginMain.getMusicThread().getSongs();

						for (int i = 0; i < songs.length; i++) {

							if (i % 2 == 0) {
								buf.append(ChatColor.YELLOW);
							}

							buf.append(songs[i].getTitle());

							if (i < songs.length - 1) {
								buf.append(", ");
							}

						}
						sender.sendMessage(buf.toString());
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			}
		}
		return true;
	}
}
