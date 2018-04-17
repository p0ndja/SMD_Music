package me.palapon2545.music;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.palapon2545.music.NoteblockAPI.SongPlayer;
import me.palapon2545.music.api.MusicThread;
import me.palapon2545.music.api.tools.ActionBarAPI;
import me.palapon2545.music.cmd.adminCommands;
import me.palapon2545.music.cmd.playerCommands;
import me.palapon2545.music.listeners.playerJoinLeft;

public class pluginMain extends JavaPlugin {

	public static MusicThread mt;
	public static pluginMain instance;
	public static HashMap<String, ArrayList<SongPlayer>> playingSongs = new HashMap<String, ArrayList<SongPlayer>>();
	public static HashMap<String, Byte> playerVolume = new HashMap<String, Byte>();
	
	public static boolean isThisSongCheckLyric = false;
	public static boolean isThisSongHaveLyric = false;


	public void onEnable() {
		ActionBarAPI.run();
		File userfiles;
		File lyricfiles;
        try {
            userfiles = new File(getDataFolder() + File.separator + "/songs/");
			lyricfiles = new File(getDataFolder(), File.separator + "lyric/");
            if(!userfiles.exists()){
                userfiles.mkdirs();
            }
            if(!lyricfiles.exists()) {
            	lyricfiles.mkdirs();
            }
        } catch(SecurityException e) {
            return;
        }
		Bukkit.broadcastMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "SMDMusic System: " + ChatColor.GREEN
				+ ChatColor.BOLD + "Enable");
		instance = this;
		mt = new MusicThread(getSongFolder());
		if (mt.getSongs().length == 0) {
			getLogger().warning("Alert! No songs found.");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		} else {
			Bukkit.getScheduler().runTaskTimer(this, mt, 0, 20);
			getMusicThread().randomSong();
			pluginMain.getMusicThread().getSongPlayer().setPlaying(true);
		}
		regCmds();
		regEvents();
		
		BukkitScheduler s = getServer().getScheduler();
		s.scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				String currentSong = mt.getCurrentSong().getTitle();
				short length = mt.getCurrentSong().getLength();
				short nowLength = mt.getSongPlayer().getTick();
				float speed = mt.getCurrentSong().getSpeed();
				
				long lengthSecond = ((long) length) / ((long) speed);
				long nowLengthSecond = ((long) nowLength) / ((long) speed);
				
				if (isThisSongCheckLyric == false) {
					File lyricsFolder = new File(getDataFolder(), File.separator + "lyric/");
					File lyricsFile = new File(lyricsFolder, File.separator + currentSong + ".txt");
					if (lyricsFile.exists()) {
						isThisSongCheckLyric = true;
						isThisSongHaveLyric = true;
					} else {
						isThisSongCheckLyric = false;
						isThisSongHaveLyric = true;
					}
				} else {
					if (isThisSongHaveLyric == true) {
						File lyricsFolder = new File(getDataFolder(), File.separator + "lyric/");
						File lyricsFile = new File(lyricsFolder, File.separator + currentSong + ".txt");
						FileConfiguration lyricsData = YamlConfiguration.loadConfiguration(lyricsFile);
						String lyricDisplay = "";
						String lyricMessage = lyricsData.getString(nowLengthSecond + "");
						
						if (!lyricMessage.isEmpty()) {
							lyricDisplay = lyricMessage;
						} else {
							lyricDisplay = "";
						}
						
						if (!lyricDisplay.isEmpty()) {
							ActionBarAPI.sendToAll("[" + ChatColor.GREEN + "Lyric" + ChatColor.WHITE + "] " + lyricDisplay);
						}
					} else {
						//NOTHING
					}
				}
			}
		}, 0L, 10L);
		s.scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (getConfig().getString("Players." + p.getName() + ".music") == "true") {
						getMusicThread().getSongPlayer().addPlayer(p);
					} else {
						getMusicThread().getSongPlayer().removePlayer(p);
					}
				}
			}
		}, 0L, 10L);
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (getConfig().getString("Players." + p.getName() + ".music").equalsIgnoreCase("true")) {
				getMusicThread().getSongPlayer().addPlayer(p);
			} else {
				getMusicThread().getSongPlayer().removePlayer(p);
			}
		}
	}

	public void onDisable() {
		getMusicThread().getSongPlayer().setPlaying(false);
		Bukkit.broadcastMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "SMDMusic System: " + ChatColor.RED
					+ ChatColor.BOLD + "Disable");
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 0);
			getMusicThread().getSongPlayer().removePlayer(p);
		}
	}

	public void regCmds() {
		getCommand("musicadmin").setExecutor(new adminCommands());
		getCommand("music").setExecutor(new playerCommands(this));
	}

	public void regEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new playerJoinLeft(this), this);
	}

	public static boolean isReceivingSong(Player p) {
		return ((pluginMain.playingSongs.get(p.getName()) != null)
				&& (!pluginMain.playingSongs.get(p.getName()).isEmpty()));
	}

	public static void stopPlaying(Player p) {
		if (pluginMain.playingSongs.get(p.getName()) == null) {
			return;
		}
		for (SongPlayer s : pluginMain.playingSongs.get(p.getName())) {
			s.removePlayer(p);
		}
	}

	public static void setPlayerVolume(Player p, byte volume) {
		playerVolume.put(p.getName(), volume);
	}

	public static byte getPlayerVolume(Player p) {
		Byte b = playerVolume.get(p.getName());
		if (b == null) {
			b = 100;
			playerVolume.put(p.getName(), b);
		}
		return b;
	}

	public static pluginMain getInstance() {
		return instance;
	}

	public static MusicThread getMusicThread() {
		return pluginMain.mt;
	}

	public static File getSongFolder() {
		return new File(getInstance().getDataFolder(), "songs/");
	}

}
